package ru.dima_and_tanysha.primes;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.dima_and_tanysha.primes.model.Model;
import ru.dima_and_tanysha.primes.model.PrimeFile;
import ru.dima_and_tanysha.primes.view.RootController;

import java.io.IOException;

public class MainApp extends Application {

    private Stage mStage;

    private Model                     mModel;
    private ObservableList<PrimeFile> mPrimeFiles;

    public MainApp() {
        mModel = new Model();

        mPrimeFiles = FXCollections.observableArrayList();
        mPrimeFiles.add(new PrimeFile("(25 primes out of 100).data", 25, 100));
        mPrimeFiles.add(new PrimeFile("(168 primes out of 1 000).data", 168, 1_000));
        mPrimeFiles.add(new PrimeFile("(1 229 primes out of 10 000).data", 1_229, 10_000));
        mPrimeFiles.add(new PrimeFile("(9 592 primes out of 100 000).data", 9_592, 100_000));
        mPrimeFiles.add(new PrimeFile("(78 498 primes out of 1 000 000).data", 78_498, 1_000_000));
        mPrimeFiles.add(new PrimeFile("(664 579 primes out of 10 000 000).data", 664_579, 10_000_000));
        mPrimeFiles.add(new PrimeFile("(5 761 455 primes out of 100 000 000).data", 5_761_455, 100_000_000));

        mModel.setPrimeFile(mPrimeFiles.get(6));
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

    public Model getModel() {
        return mModel;
    }

    public ObservableList<PrimeFile> getPrimeFiles() {
        return mPrimeFiles;
    }
}
