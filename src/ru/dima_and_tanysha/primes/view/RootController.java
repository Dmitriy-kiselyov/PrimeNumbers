package ru.dima_and_tanysha.primes.view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import ru.dima_and_tanysha.primes.MainApp;
import ru.dima_and_tanysha.primes.model.*;

import java.io.File;
import java.io.IOException;

/**
 * Created by Pussy_penetrator on 12.03.2017.
 */
public class RootController {

    @FXML
    PrimesCanvas mCanvas;

    @FXML
    Pane mCanvasPane;

    @FXML
    TextField mPrimesPathTextField;

    @FXML
    Button mChoosePrimesPathButton;

    @FXML
    Label mPrimeCountLabel;

    @FXML
    TextField mShowToTextField;

    @FXML
    TextField mWidthTextField;

    @FXML
    TextField mHeightTextField;

    @FXML
    Slider mFilterSlider;

    @FXML
    Label mFilterLabel;

    @FXML
    TextField mImagePathTextField;

    @FXML
    Button mChooseSaveImageButton;

    @FXML
    Button mSaveButton;

    @FXML
    Label mTimeLabel;

    @FXML
    Button mApplyButton;

    private MainApp mMainApp;
    private Model mModel;

    private BooleanProperty mCanApply = new SimpleBooleanProperty(true);

    @FXML
    private void initialize() {
        mCanvas.widthProperty().bind(mCanvasPane.widthProperty());
        mCanvas.heightProperty().bind(mCanvasPane.heightProperty());
    }

    public void setMainApp(MainApp mainApp) {
        mMainApp = mainApp;
        mModel = mainApp.getModel();
        mCanvas.setModel(mModel);
        mCanvas.setStrategy(new CanvasCircleStrategy(mModel));

        mImagePathTextField.setText(mModel.getSaveImagePath());
        mTimeLabel.setVisible(false);

        setupListeners();
    }

    private void setupListeners() {
        setupApply();
        setupSlider();
        setupSizes();
        setupPrimePath();
        setupShowTo();
        setupImageSave();
        setupImageSave();
    }

    private void setupApply() {
        mApplyButton.disableProperty().bind(mCanApply.not());
    }

    private void setupSlider() {
        mFilterSlider.setMin(mModel.MIN_FILTER);
        mFilterSlider.setMax(mModel.MAX_FILTER);
        mFilterSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            mModel.setFilter(newValue.intValue());
            mFilterLabel.setText(String.valueOf(newValue.intValue()));
        });
        mFilterSlider.setValue(mModel.getFilter());
    }

    private void setupSizes() {
        mWidthTextField.setText(String.valueOf(mModel.getImageWidth()));
        mHeightTextField.setText(String.valueOf(mModel.getImageHeight()));
        mWidthTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int value = Integer.parseInt(newValue);
                mModel.setImageWidth(value);

                mHeightTextField.setText(String.valueOf(value));

                mWidthTextField.getStyleClass().remove("error");
            }
            catch (Exception e) {
                if (!mWidthTextField.getStyleClass().contains("error"))
                    mWidthTextField.getStyleClass().add("error");
            }
        });
        mHeightTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int value = Integer.parseInt(newValue);
                mModel.setImageHeight(value);

                mHeightTextField.getStyleClass().remove("error");
            }
            catch (Exception e) {
                if (!mHeightTextField.getStyleClass().contains("error"))
                    mHeightTextField.getStyleClass().add("error");
            }
        });
    }

    private void setupPrimePath() {
        mPrimesPathTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                PrimeFile primeFile = new PrimeFile(newValue);
                mModel.setPrimeFile(primeFile);
                mPrimeCountLabel.setText(String.format("Простые до %,d", primeFile.getMaxCount()));
            }
            catch (IOException e) {
                mPrimeCountLabel.setText("Файл не выбран");
            }
        });
        mPrimesPathTextField.setText("D:\\Prime numbers\\primes_s.data");
    }

    private void setupShowTo() {
        if (mModel.getShowToNumber() != 0)
            mShowToTextField.setText("" + mModel.getShowToNumber());

        mShowToTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                long n = Long.parseLong(newValue);
                mModel.setShowToNumber(n);
            }
            catch (NumberFormatException e) {
                //nothing
            }
        });
    }

    private void setupImageSave() {
        mModel.saveImagePathProperty().bind(mImagePathTextField.textProperty());
        mSaveButton.disableProperty()
                .bind(mModel.saveImagePathProperty().isEmpty().or(mCanvas.hasImageProperty().not()));
    }

    private void disableOrEnableApply() {
        //todo
    }

    @FXML
    private void handleChooseSaveImageDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();

        File file = directoryChooser.showDialog(mMainApp.getStage());
        if (file != null) {
            mImagePathTextField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void handleChoosePrimesFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Простые числа (*.data)", "*.data"));

        File file = fileChooser.showOpenDialog(mMainApp.getStage());
        if (file != null)
            mPrimesPathTextField.setText(file.getAbsolutePath());
    }

    @FXML
    private void handleSaveImage() {
        mCanvas.saveImage();
    }

    @FXML
    private void handleApply() {
        if (mCanApply.get()) {
            mWidthTextField.setText(String.valueOf(mModel.getImageWidth()));
            mHeightTextField.setText(String.valueOf(mModel.getImageHeight()));

            mTimeLabel.setVisible(false);
            long startTime = System.nanoTime();

            mMainApp.saveModel();
            applyCanvasStrategy();
            mCanvas.updateAndRedraw();

            long time = (System.nanoTime() - startTime) / 1_000_000;
            long sec = time / 1_000;
            long mil = time % 1_000;
            mTimeLabel.setText("Время работы: " + sec + "с " + mil + "мс");
            mTimeLabel.setVisible(true);
        }
    }

    private void applyCanvasStrategy() {
        double radius = mCanvas.calculateRadius(mModel.getImageWidth(), mModel.getImageHeight(), mModel.getShowToNumber());
        if (radius <= 1 && mCanvas.getStrategy().getClass() != CanvasMatrixStrategy.class)
            mCanvas.setStrategy(new CanvasMatrixStrategy(mModel));
        if (radius > 1 && mCanvas.getStrategy().getClass() != CanvasCircleStrategy.class)
            mCanvas.setStrategy(new CanvasCircleStrategy(mModel));
    }

    @FXML
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER)
            handleApply();
    }

}
