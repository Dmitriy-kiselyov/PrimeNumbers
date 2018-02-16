package ru.dima_and_tanysha.primes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.dima_and_tanysha.primes.model.Model;
import ru.dima_and_tanysha.primes.view.RootController;

import java.io.IOException;
import java.util.prefs.Preferences;

public class MainApp extends Application {

    private Stage mStage;

    private Model mModel;

    public MainApp() {
        mModel = new Model();
        loadModel();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        mStage = primaryStage;
        primaryStage.setTitle("Primes");
        initRootLayout();
    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/root.fxml"));
            Parent root = loader.load();

            RootController controller = loader.getController();
            controller.setMainApp(this);

            mStage.setScene(new Scene(root));
            mStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getStage() {
        return mStage;
    }

    public Model getModel() {
        return mModel;
    }

    private void loadModel() {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);

        long showToNumber = prefs.getLong("showToNumber", 0);
        if (showToNumber != 0)
            mModel.setShowToNumber(showToNumber);
        mModel.setImageWidth(prefs.getInt("imageWidth", 1000));
        mModel.setImageHeight(prefs.getInt("imageHeight", 1000));
        mModel.setFilter(prefs.getInt("filter", 255));
        mModel.setSaveImagePath(prefs.get("imagePath", ""));
    }

    public void saveModel() {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);

        prefs.putLong("showToNumber", mModel.getShowToNumber());
        prefs.putInt("imageWidth", mModel.getImageWidth());
        prefs.putInt("imageHeight", mModel.getImageHeight());
        prefs.putInt("filter", mModel.getFilter());
        prefs.put("imagePath", mModel.getSaveImagePath());
    }
}
