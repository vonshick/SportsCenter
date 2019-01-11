package GUI;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertBox {
    public static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
        alert.showAndWait();
    }
}
