/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalmazesolver;

import java.util.ArrayList;

/**
 *
 * @author Leeroy
 */
public class Noeud {
    
    int i, j;
    ArrayList<Noeud> listeFils;
    ArrayList<Noeud> chainnage;
    ArrayList<Noeud> chainnage2;
    double f, g, h;
    String type;
    int trou;
    Noeud parent;
    Noeud parent2;
    
    
    public Noeud(int a, int b, char c){
        listeFils = new ArrayList();
        chainnage = new ArrayList();
        chainnage2 = new ArrayList();
        i = a;
        j = b;
        trou = 0;
        switch(c){
            case 'f':
                type = "final";
                break;
            case 'i':
                type = "initial";
                break;
            case '1':
                type = "mur";
                break;
            case '0':
                type = "chemin";
                break;
        }
        
    }
    
    public void updateChainnage(){
        this.chainnage = new ArrayList();
        this.chainnage.addAll(this.parent.chainnage);
        this.chainnage.add(this);
    }

    public void afficherChainnage(){
        System.out.println("Chemin vers la sortie :");
        System.out.print(" ["+chainnage.get(0).getI()+","+chainnage.get(0).getJ()+"]");
        for (int i=1;i<chainnage.size();i++){
            Noeud act = chainnage.get(i);
            System.out.print(" ->["+act.getI()+","+act.getJ()+"]");
        }
        System.out.println("");
    }
    
    public ArrayList<Noeud> getChainnage() {
        return chainnage;
    }

    public void setChainnage(ArrayList<Noeud> chainnage) {
        this.chainnage = chainnage;
    }

    public Noeud getParent() {
        return parent;
    }

    public void setParent(Noeud parent) {
        this.parent = parent;
    }
    
    public void ajouterFils(Noeud n){
        listeFils.add(n);
    }

    public boolean equals(Noeud n){
        if (this.i == n.i && this.j == n.j) return true;
        else return false;
    }
    
    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public ArrayList<Noeud> getListeFils() {
        return listeFils;
    }

    public void setListeFils(ArrayList<Noeud> listeFils) {
        this.listeFils = listeFils;
    }

    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public String toString(){
        String s="";
        s+=" ["+i+" , "+j+"]";
        return s;
    }
}
