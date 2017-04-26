package ru.dima_and_tanysha.primes.view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import ru.dima_and_tanysha.primes.MainApp;
import ru.dima_and_tanysha.primes.model.Model;
import ru.dima_and_tanysha.primes.model.PrimeFile;
import ru.dima_and_tanysha.primes.model.PrimesImage;

import java.io.File;

/**
 * Created by Pussy_penetrator on 12.03.2017.
 */
public class RootController {

    @FXML
    PrimesImage mCanvas;

    @FXML
    Pane mCanvasPane;

    @FXML
    ComboBox<PrimeFile> mFileComboBox;

    @FXML
    TextField mWidthTextField;

    @FXML
    TextField mHeightTextField;

    @FXML
    Slider mFilterSlider;

    @FXML
    Label mFilterLabel;

    @FXML
    TextField mSaveImageTextField;

    @FXML
    Button mChooseSaveImageButton;

    @FXML
    Button mSaveButton;

    @FXML
    Label mTimeLabel;

    @FXML
    Button mApplyButton;

    private MainApp mMainApp;
    private Model   mModel;

    private BooleanProperty mCanApply = new SimpleBooleanProperty(true);

    @FXML
    private void initialize() {
        mCanvas.widthProperty().bind(mCanvasPane.widthProperty());
        mCanvas.heightProperty().bind(mCanvasPane.heightProperty());
    }

    public void setMainApp(MainApp mainApp) {
        mMainApp = mainApp;
        mModel = mainApp.getModel();
        mFileComboBox.setItems(mMainApp.getPrimeFiles());
        mCanvas.setModel(mModel);

        mFileComboBox.getSelectionModel().select(mModel.getPrimeFile());
        mTimeLabel.setVisible(false);

        setupListeners();
    }

    private void setupListeners() {
        mFileComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            mModel.setPrimeFile(newValue);
            disableOrEnableApply();
        });

        mApplyButton.disableProperty().bind(mCanApply.not());

        mFilterSlider.setMin(mModel.MIN_FILTER);
        mFilterSlider.setMax(mModel.MAX_FILTER);
        mFilterSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            mModel.setFilter(newValue.intValue());
            mFilterLabel.setText(String.valueOf(newValue.intValue()));
        });
        mFilterSlider.setValue(mModel.getFilter());

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

        mSaveImageTextField.setText(mModel.getSaveImagePath());
        mModel.saveImagePathProperty().bind(mSaveImageTextField.textProperty());
    }

    private void disableOrEnableApply() {
        if (mFileComboBox.getSelectionModel().getSelectedIndex() < 0) {
            mCanApply.setValue(false);
        } else
            mCanApply.setValue(true);
    }

    @FXML
    private void handleChooseDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();

        File file = directoryChooser.showDialog(mMainApp.getStage());
        if (file != null) {
            mSaveImageTextField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void handleSaveImage() {
        mCanvas.saveImage();
    }

    @FXML
    private void handleApply() {
        if (mCanApply.get()) {
            mTimeLabel.setVisible(false);
            long startTime = System.nanoTime();

            mCanvas.updateAndRedraw();

            long time = (System.nanoTime() - startTime) / 1_000_000;
            long sec = time / 1_000;
            long mil = time % 1_000;
            mTimeLabel.setText("Время работы: " + sec + "с " + mil + "мс");
            mTimeLabel.setVisible(true);
        }
    }

    @FXML
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER)
            handleApply();
    }

}
