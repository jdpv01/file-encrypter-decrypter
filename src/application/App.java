package application;
import java.io.File;

import controller.MainController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.MasterCryptImp;

public class App extends Application {

    public static File targetFolder = new File("out/");

    public static void main(String[] args) {

        setup();

        launch(args);
    }

    private static void setup() {
        targetFolder.mkdirs();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/MainWindow.fxml"));
        MasterCryptImp master = new MasterCryptImp();
        MainController controller = new MainController(primaryStage, master);
        fxmlLoader.setController(controller);

        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Icesi crypter V0.1");
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/view/logo.jpg")));

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent arg0) {
                controller.renderCredits();
            };
        });

        primaryStage.show();
    }
}