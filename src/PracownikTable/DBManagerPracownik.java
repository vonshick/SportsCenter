package PracownikTable;

import GUI.AlertBox;
import TrenerTable.AddTrenerController;
import java.io.IOException;
import sportscenter.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class DBManagerPracownik {
    
    private DBManager dBManager;

    public DBManagerPracownik(DBManager dBManager) {
        this.dBManager = dBManager;
    }
    
    public void editPracownik(String oldPESEL, String name, String surname, String newPESEL, String profession, String salary) {
        try {
            PreparedStatement pstmt = SportsCenter.dBManager.getConnection().prepareStatement("update pracownik set pesel = ?, nazwisko = ?, imie = ?, funkcja = ?, placa = ? where pesel = ?");
            pstmt.setString(1, newPESEL);
            pstmt.setString(2, surname);
            pstmt.setString(3, name);
            pstmt.setString(4, profession);
            pstmt.setFloat(5, Float.parseFloat(salary));
            pstmt.setString(6, oldPESEL);
            pstmt.executeQuery();
            SportsCenter.dBManager.getConnection().commit();
            System.out.println("Employee updated!");
        } catch (SQLException e) {
            System.out.println("Employee update error");
        }
    }
    
    public void insertNewPracownik(String name, String surname, String PESEL, String profession, String salary, ActionEvent event) throws IOException {
        try {
            PreparedStatement pstmt = SportsCenter.dBManager.getConnection().prepareStatement("INSERT INTO pracownik VALUES(?, ?, ?, ?, ?)");
            SportsCenter.dBManager.getConnection().setAutoCommit(false); // if coach adding fails we have to rollback
            pstmt.setString(1, PESEL);
            pstmt.setString(2, surname);
            pstmt.setString(3, name);
            pstmt.setString(4, profession);
            pstmt.setFloat(5, Float.parseFloat(salary));
            pstmt.executeUpdate();
            System.out.println("Employee added!");
            if (profession.toLowerCase().equals("trener") || profession.toLowerCase().equals("trenerka")) {
                openAddTrenerWindow(event, PESEL);
            }
        } catch (SQLException e) {
            System.out.println("Employee inserting error");
        }
    }
    
    private void openAddTrenerWindow(ActionEvent event, String PESEL) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/TrenerTable/AddTrener.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene((Pane) loader.load()));
        stage.setTitle("Log in");
        stage.setOnCloseRequest((WindowEvent event1) -> {
            AlertBox.showAlert("Employee not added!");
            try {
                SportsCenter.dBManager.getConnection().rollback(); // if coach adding fails we have to rollback
                SportsCenter.dBManager.getConnection().setAutoCommit(true);
            } catch (SQLException ex) {
                Logger.getLogger(AddPracownikController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        AddTrenerController controller = loader.<AddTrenerController>getController();
        controller.initData(PESEL);
        stage.show();
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
    
    public DBManager getdBManager() {
        return dBManager;
    }
    
}