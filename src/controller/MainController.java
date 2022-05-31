package controller;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.MasterCryptImp;

public class MainController {

    private final Stage primaryStage;
    private final MasterCryptImp master;

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

    @FXML
    private Pane mainPane;

    public MainController(Stage primaryStage, MasterCryptImp master) {
        this.primaryStage = primaryStage;
        this.master = master;
    }

    @FXML
    void closeProgram(ActionEvent event) {
        renderCredits();
        primaryStage.close();
    }

    @FXML
    void runCrypt(ActionEvent event) throws Exception {

        if (!passwordTF.getText().isEmpty()) {
            msgT.setVisible(false);
            openFileBtn.setDisable(true);

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Selecciona archivo a encriptar");

            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

            File fileToCrypt = fileChooser.showOpenDialog(primaryStage);

            if (fileToCrypt != null) {
                master.startEncrypting(fileToCrypt, passwordTF.getText());

                openFileBtn.setDisable(false);

                // Decrypt view

                mainPane.getChildren().clear();

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/EncryptPane.fxml"));
                fxmlLoader.setController(this);
                Parent mainApp = fxmlLoader.load();

                mainPane.getChildren().add(mainApp);
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

    @FXML
    void runDecrypt(ActionEvent event) {

        if (!passwordTF.getText().isEmpty()) {
            msgT.setVisible(false);
            openFileBtn.setDisable(true);

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Selecciona archivo a desencripar");

            fileChooser.setInitialDirectory(new File("./out"));

            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("ENC", "*.enc"));

            File fileTodecrypt = fileChooser.showOpenDialog(primaryStage);

            if (fileTodecrypt != null) {
                System.out.println(passwordTF.getText());

                try {
                    String shas = master.startDecrypt(fileTodecrypt, passwordTF.getText());

                    resultMsg.setText(shas);

                    resultMsg.setVisible(true);
                    resultadoLabel.setVisible(true);

                    msgT.setVisible(false);

                } catch (Exception e) {

                    msgT.setText("Contraseña incorrecta");

                    msgT.setVisible(true);

                }

            }
        } else {
            msgT.setVisible(true);
        }

        openFileBtn.setDisable(false);
    }

}
