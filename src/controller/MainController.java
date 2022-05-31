package controller;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.MasterCrypt;

public class MainController {

    private final Stage primaryStage;
    private final MasterCrypt master;

    @FXML
    private Button openFileBtn;

    @FXML
    private Text msgT;

    @FXML
    private PasswordField passwordTF;

    @FXML
    private Text resultMsg;

    @FXML
    private Text resultadoLabel;

    public MainController(Stage primaryStage, MasterCrypt master) {
        this.primaryStage = primaryStage;
        this.master = master;
    }

    @FXML
    void closeProgram(ActionEvent event) {
        renderCredits();
        primaryStage.close();
    }

    @FXML
    void runCrypt(ActionEvent event) throws InterruptedException {

        if (!passwordTF.getText().isEmpty()) {
            msgT.setVisible(false);
            openFileBtn.setDisable(true);

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Selecciona archivo a encriptar");

            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

            File fileToCrypt = fileChooser.showOpenDialog(primaryStage);

            if (fileToCrypt != null) {
                resultMsg.setText(master.init(fileToCrypt, passwordTF.getText()));
                resultMsg.setVisible(true);
                resultadoLabel.setVisible(true);

                openFileBtn.setDisable(false);
            }
        } else {
            msgT.setVisible(true);
        }

    }

    @FXML
    void showCredits(ActionEvent event) {
        renderCredits();
    }

    public void renderCredits() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Icesi crypt");
        alert.setHeaderText(null);
        alert.setContentText(
                "Gracias por usar Icesi Crypts\n\nDesarrolladores:\n    -Juan David Pelaez\n    -Jessica Daniela Otero\n    -Cristhian Camilo Gutierrez");

        alert.showAndWait();
    }

}
