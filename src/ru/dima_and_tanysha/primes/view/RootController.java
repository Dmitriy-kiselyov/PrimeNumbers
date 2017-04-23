package ru.dima_and_tanysha.primes.view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import ru.dima_and_tanysha.primes.MainApp;
import ru.dima_and_tanysha.primes.model.Model;
import ru.dima_and_tanysha.primes.model.PrimeFile;
import ru.dima_and_tanysha.primes.model.PrimesImage;

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
    CheckBox mOnlyPrimeCheckBox;

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

        mFileComboBox.getSelectionModel().select(0);
        mTimeLabel.setVisible(false);

        setupListeners();
    }

    private void setupListeners() {
        mFileComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            mModel.setPrimeFile(newValue);
            disableOrEnableApply();
        });

        mApplyButton.disableProperty().bind(mCanApply.not());

        mOnlyPrimeCheckBox.selectedProperty().bindBidirectional(mModel.onlyPrimeProperty());
    }

    private void disableOrEnableApply() {
        if (mFileComboBox.getSelectionModel().getSelectedIndex() < 0) {
            mCanApply.setValue(false);
        } else
            mCanApply.setValue(true);
    }

    @FXML
    private void handleApply() {
        if (mCanApply.get()) {
            mTimeLabel.setVisible(false);
            long startTime = System.nanoTime();

            mCanvas.redraw();

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
