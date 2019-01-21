package PracownikTable;

import GUI.AlertBox;
import TrenerTable.AddTrenerController;
import java.io.IOException;
import sportscenter.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    
    public void deletePracownik(String PESEL) {
//        if(isForeignKey(PESEL)) {
//            GUI.AlertBox.showAlert("Naruszenie więzów intgralności: By usunąć pracownika, najpierw usuń powiązanego z nim trenera!");
//        } else {
        try {
            PreparedStatement pstmt = SportsCenter.dBManager.getConnection().prepareStatement("DELETE FROM pracownik WHERE PESEL = ?");
            pstmt.setString(1, PESEL);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ValidateData.printSQLException(ex, PESEL);
        }
//        }
    }
    
//    public boolean isForeignKey(String PESEL) {
//        try {
//            PreparedStatement pstmt = SportsCenter.dBManager.getConnection().prepareStatement("SELECT * FROM trener WHERE PESEL =  ?");
//            pstmt.setString(1, PESEL);
//            ResultSet rs = pstmt.executeQuery();
//            return rs.next();
//        } catch (SQLException ex) {
//            System.out.println("isForeignKey error");
//            return true;
//        }
//    }
    
    public void editPracownik(String oldPESEL, String name, String surname, String newPESEL, String profession, Float salary) throws SQLException {
        Statement stmt = SportsCenter.dBManager.getConnection().createStatement();
        String dropConstraintTrener = "ALTER TABLE trener DROP CONSTRAINT trener_pracownik_fk";
        String dropConstraintZajecia = "ALTER TABLE zajecia DROP CONSTRAINT zajecia_trener_fk";
        String addConstraintTrener = "ALTER TABLE trener\n" +
                    "    ADD CONSTRAINT trener_pracownik_fk FOREIGN KEY ( pesel )\n" +
                    "        REFERENCES pracownik ( pesel )";
        String addConstraintZajecia ="ALTER TABLE zajecia\n" +
                    "    ADD CONSTRAINT zajecia_trener_fk FOREIGN KEY ( trener_pesel )\n" +
                    "        REFERENCES trener ( pesel )";

    
        try {
            PreparedStatement pstmt = SportsCenter.dBManager.getConnection().prepareStatement("update pracownik set pesel = ?, nazwisko = ?, imie = ?, funkcja = ?, placa = ? where pesel = ?");
            SportsCenter.dBManager.getConnection().setAutoCommit(false);
            stmt.executeQuery(dropConstraintTrener);
            stmt.executeQuery(dropConstraintZajecia);
            pstmt.setString(1, newPESEL);
            pstmt.setString(2, surname);
            pstmt.setString(3, name);
            pstmt.setString(4, profession);
            pstmt.setFloat(5, salary);
            pstmt.setString(6, oldPESEL);
            pstmt.executeQuery();
            
            pstmt = SportsCenter.dBManager.getConnection().prepareStatement("update trener set pesel = ? where pesel = ?");
            pstmt.setString(1, newPESEL);
            pstmt.setString(2, oldPESEL);
            pstmt.executeQuery();
            
            pstmt = SportsCenter.dBManager.getConnection().prepareStatement("update zajecia set trener_pesel = ? where trener_pesel = ?");
            pstmt.setString(1, newPESEL);
            pstmt.setString(2, oldPESEL);
            pstmt.executeQuery();
            
            stmt.executeQuery(addConstraintTrener);
            stmt.executeQuery(addConstraintZajecia);
            SportsCenter.dBManager.getConnection().commit();
            SportsCenter.dBManager.getConnection().setAutoCommit(true);
            System.out.println("Employee updated!");
        } catch (SQLException e) {
            SportsCenter.dBManager.getConnection().rollback();
            stmt.executeQuery(addConstraintTrener);
            stmt.executeQuery(addConstraintZajecia);
            SportsCenter.dBManager.getConnection().setAutoCommit(true);
            ValidateData.printSQLException(e, "PESEL");
            System.out.println("Employee update error");
        }
    }
    
    public void insertNewPracownik(String name, String surname, String PESEL, String profession, Float salary, ActionEvent event) throws IOException {
        try {
            PreparedStatement pstmt = SportsCenter.dBManager.getConnection().prepareStatement("INSERT INTO pracownik VALUES(?, ?, ?, ?, ?)");
            SportsCenter.dBManager.getConnection().setAutoCommit(false); // if coachadding fails we have to rollback
            pstmt.setString(1, PESEL);
            pstmt.setString(2, surname);
            pstmt.setString(3, name);
            pstmt.setString(4, profession);
            pstmt.setFloat(5, salary);
            pstmt.executeUpdate();
            System.out.println("Employee added!");
            if (profession.toLowerCase().equals("trener") || profession.toLowerCase().equals("trenerka")) {
                openAddTrenerWindow(event, PESEL);
            }
        } catch (SQLException e) {
            ValidateData.printSQLException(e, "PESEL");
            System.out.println("Employee inserting error");
        }
    }
    
    public void openAddTrenerWindow(ActionEvent event, String PESEL) throws IOException {
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
    
    public float countAnnualTax(String PESEL) throws SQLException{
        PreparedStatement pstmt = SportsCenter.dBManager.getConnection().prepareStatement("SELECT podatek(?) FROM DUAL");
        pstmt.setString(1, PESEL);
        
        ResultSet rs = pstmt.executeQuery();
        float tax = (float) 0;
        if(rs.next()){
            tax = rs.getFloat(1);
        }
        return tax;
    }
    
    public DBManager getdBManager() {
        return dBManager;
    }
    
}