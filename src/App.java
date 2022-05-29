import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.MasterCrypt;
import model.MasterCryptImp;

public class App extends Application {
    public static void main(String[] args) {
        /*
         * MasterCrypt master = new MasterCryptImp();
         * master.init();
         */

        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Label l = new Label("CRYPTER");
        Scene scene = new Scene(new StackPane(l), 640, 480);
        stage.setScene(scene);
        stage.show();
    }
}