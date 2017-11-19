/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalmazesolver;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.FileChooser;

/**
 *
 * @author Salomon Donald N.B
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private JFXButton btn_resolve;

    @FXML
    private JFXButton btn_importer;
    
    @FXML
    private GridPane grille;
    
    @FXML
    private ScrollPane scrollP;

    @FXML
    private JFXTextField textF_affiche;
    
    @FXML
    private JFXButton btn_charger;
    
    private FinalMazeSolver Fstage = new FinalMazeSolver();
    
    private Labyrinthe lab;
    private double largeur;
    private double hauteur;
    
    private String path = "";
    private char[][] grid;
    private int nline, ncol;
    private FileChooser fileChooser = new FileChooser();
    
    private final String green = "-fx-background-color: GREEN";
    private final String red = "-fx-background-color: RED";
    private final String white = "-fx-background-color: WHITE";
    private final String blue = "-fx-background-color: BLUE";
    private final String marron = "-fx-background-color:  #e2a316";
    
    private ArrayList<Noeud> chainage;
    
    @FXML
    void importerFichier(ActionEvent event) {
        try {
            path = fileChooser.showOpenDialog(Fstage.getFstage()).getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        textF_affiche.setText(path);
        
    }
    
    private void ConstructGrid(ArrayList<Noeud>list, int nbline, int nbcol){
        
        grille.getRowConstraints().clear();
        grille.getColumnConstraints().clear();
        
        Pane tPane[][] = new Pane[nbline][nbcol];
        
        
        System.out.println(list.size());
        
        for(int i = 0; i < nbline; i++){
            for(int j = 0; j < nbcol; j++){
                tPane[i][j] = new Pane();
                tPane[i][j].setStyle(marron);
                
                }    
            }
        
        for (Noeud n : list) {
            switch (n.getType()) {
                case "final":
                    tPane[n.getI()][n.getJ()].setStyle(green);
                    break;
                case "initial":
                    tPane[n.getI()][n.getJ()].setStyle(blue);
                    break;
                default:
                    tPane[n.getI()][n.getJ()].setStyle(white);
                    break;
            }
        }
        
        for(int i = 0; i < nbline; i++){
            grille.getRowConstraints().add(new RowConstraints(5));
        }
        
        for(int i = 0; i < nbcol; i++){
            grille.getColumnConstraints().add(new ColumnConstraints(5));
        }
        
        for(int i = 0; i < nbline; i++){
            for(int j = 0; j < nbcol; j++){
                grille.add(tPane[i][j], j, i);
            }
        }
    }
    
    @FXML
    void chargerLaGrille(ActionEvent event) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        String line = "";
        int  i=0;
        try {
            line = br.readLine();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        nline = Integer.parseInt(line.split("x")[0]);
        ncol = Integer.parseInt(line.split("x")[1]);
        
        grid = new char[nline][ncol];
        try {
            while ((line = br.readLine()) != null) {
                line = line.trim();
                for (int j=0;j<line.length();j++) grid[i][j] = line.charAt(j);
                i++;
            }
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        lab = new Labyrinthe(nline, ncol, grid);
        lab.make();
        
        ConstructGrid(lab.getListeNoeuds(), nline, ncol);
//        grille.setHgap(0);
//        grille.setVgap(0);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        largeur = scrollP.getWidth();
        hauteur = scrollP.getHeight();
        
        System.out.println(scrollP.getMaxWidth());
        
    }    
    
    private Node getNodeFromGridPane(int col, int row) {
        for (Node node : grille.getChildren()) {
            if (grille.getColumnIndex(node) == col && grille.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }
    
    @FXML
    private void RedrawGrid(ActionEvent event) {
        Noeud derniernoeud = lab.Aetoile();
        
        
        chainage = new ArrayList<>();
        chainage = derniernoeud.getChainnage();
        
        Noeud finale = chainage.get(chainage.size() - 1);
        chainage.remove(chainage.size() - 1);
        Thread thread;
        
                for(Noeud n : chainage){
                    if(getNodeFromGridPane(n.getJ(), n.getI()) != null){
                        getNodeFromGridPane(n.getJ(), n.getI()).setStyle(red);
                        
                    }
                }
//        thread = new Thread(){
//            @Override
//            public void run(){
//                for(Noeud n : chainage){
//                    if(getNodeFromGridPane(n.getJ(), n.getI()) != null){
//                        Platform.runLater(new Runnable() {
//                            @Override
//                            public void run() {
//                                getNodeFromGridPane(n.getJ(), n.getI()).setStyle(red);
//                            }
//                        });try {
//                            Thread.sleep(20);
//                        } catch (InterruptedException ex) {
//                            
//                        }
//                        
//                    }
//                }
//            }
//        };
//        
//        thread.start();
        
        System.out.println("C'est arrivé jusque là");
    }
    
    
}
