/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalmazesolver;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Salomon Donald N.B
 */
public class FinalMazeSolver extends Application {
    private Stage Fstage = new Stage();

    public Stage getFstage() {
        return Fstage;
    }

    public void setFstage(Stage Fstage) {
        this.Fstage = Fstage;
    }
    
    
    @Override
    public void start(Stage stage) throws Exception {
        this.Fstage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        
        Scene scene = new Scene(root);
        
        this.Fstage.setScene(scene);
        this.Fstage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
