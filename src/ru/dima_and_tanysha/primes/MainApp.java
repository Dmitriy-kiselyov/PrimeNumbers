package ru.dima_and_tanysha.primes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.dima_and_tanysha.primes.model.Model;
import ru.dima_and_tanysha.primes.view.RootController;

import java.io.IOException;

public class MainApp extends Application {

    private Stage mStage;

    private Model mModel;

    public MainApp() {
        mModel = new Model();
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
}
