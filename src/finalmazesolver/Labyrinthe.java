/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalmazesolver;

import java.util.ArrayList;
import java.util.Comparator;

public class Labyrinthe {
    
    char[][] grille;
    int nombreLigne;
    int nombreColone;
    ArrayList<Noeud> listeNoeuds;
    ArrayList<Noeud> listeFinaux;
    Noeud initial;

    public ArrayList<Noeud> getListeNoeuds() {
        return listeNoeuds;
    }
    
    
    public Labyrinthe(int n, int m, char[][] grid){
        nombreLigne = n;
        nombreColone = m;
        grille = new char[n][m];
        grille = grid;
        listeNoeuds = new ArrayList();
        listeFinaux = new ArrayList();
    }
    
    public void make(){
        for (int i=0;i<nombreLigne;i++){
            for (int j=0;j<nombreColone;j++){
                if (grille[i][j] != '1'){
                    Noeud n = new Noeud(i, j, grille[i][j]);
                    listeNoeuds.add(n);
                    if (n.getType().equals("initial")) initial = n;
                    if (n.getType().equals("final")) listeFinaux.add(n);
                }
            }
        }
        
        Noeud noeudCourrant;
        Noeud noeudCourrant2;
        
        for (int i=0;i<listeNoeuds.size();i++){
            noeudCourrant = listeNoeuds.get(i);
            for (int j=0;j<listeNoeuds.size();j++){
                if (j==i) continue;
                noeudCourrant2 = listeNoeuds.get(j);
                if ( ( (noeudCourrant2.getI() == noeudCourrant.getI()) && 
                      ( noeudCourrant2.getJ() == noeudCourrant.getJ()+1 || 
                        noeudCourrant2.getJ() == noeudCourrant.getJ()-1 ) ) ||
                      ( (noeudCourrant2.getJ() == noeudCourrant.getJ()) && 
                      ( noeudCourrant2.getI() == noeudCourrant.getI()+1 || 
                        noeudCourrant2.getI() == noeudCourrant.getI()-1 ) ))
                    noeudCourrant.ajouterFils(noeudCourrant2);
            }
            
            noeudCourrant.setG(distance(noeudCourrant.getI(), noeudCourrant.getJ()
                                           , initial.getI(), initial.getJ()));
            noeudCourrant.setH(distance(noeudCourrant.getI(), noeudCourrant.getJ()
                                           , listeFinaux.get(0).getI(), listeFinaux.get(0).getJ()));
            for (int j=0;j<listeFinaux.size();j++){
                double h = distance(noeudCourrant.getI(), noeudCourrant.getJ()
                                           , listeFinaux.get(j).getI(), listeFinaux.get(j).getJ());
                if (noeudCourrant.getH() > h) noeudCourrant.setH(h);
            }
            
            noeudCourrant.setF(noeudCourrant.getH()+noeudCourrant.getG());
            
        }
        remplirLesTrous();
        
    }
    
    void remplirLesTrous(){
        Noeud n;
        for (int i=0;i<listeNoeuds.size();i++){
            n = listeNoeuds.get(i);
            if (listeFinaux.contains(n) || n.equals(initial)) continue;
            if (n.getListeFils().size() <= 1) remplirCeTouJusquaSurface(n);
        }
    }    
    
    void remplirCeTouJusquaSurface(Noeud n){
        n.trou = 1;
        for (int j=0;j<n.getListeFils().size();j++){
            Noeud n2 = n.getListeFils().get(j);
            if (listeFinaux.contains(n2) || n2.equals(initial)) break;
            if (n2.getListeFils().size() == 2 && n2.trou == 0) remplirCeTouJusquaSurface(n2);
        }
    }
    
    public void afficherListe(ArrayList<Noeud> liste){
        System.out.print(" { ");
        for (int i=0;i<liste.size();i++){
            System.out.print(liste.get(i));
            System.out.print(" , ");
        }
        System.out.println(" } ");
    }
    
    public void mettreAjourDansOpen(ArrayList<Noeud> open, Noeud n){
        for (int i=0;i<open.size();i++){
            Noeud act = open.get(i);
            if (n.equals(act)){
                if (act.f > n.f) {
                    act.f = n.f;
                    act.g = n.g;
                    act.h = n.h;
                    act.chainnage = n.chainnage;
                    act.parent = n.parent;
                }
            }
        }
    }
    //on doit classer plutard la liste des noeuds si
    public void ajouterDansOpen(ArrayList<Noeud> open, ArrayList<Noeud> close, Noeud paren, ArrayList<Noeud> listeEnfant){
        for (int i=0;i<listeEnfant.size();i++){
            Noeud act = listeEnfant.get(i);
            if (close.contains(act)) continue;
            if (act.trou == 1) {
                //ln(act+" C'est un trou !");
                continue;
            }
            act.setParent(paren);
            act.updateChainnage();
            if (open.contains(act)){
                mettreAjourDansOpen(open, act);
            }
            else open.add(act);
        }
        open.sort(new ComparerateurNoeud());
    }
    
    public Noeud Aetoile(){
        
        long debut = System.currentTimeMillis();
        Noeud actD, actF, par1, par2, finalAtteint = initial;
        ArrayList<Noeud> open1 = new ArrayList();
        open1.add(initial);
        ArrayList<Noeud> open2 = new ArrayList();
        open2.add(listeFinaux.get(0)); // on choisira mieux le noeud a mettre ici
        ArrayList<Noeud> close1 = new ArrayList();
        ArrayList<Noeud> close2 = new ArrayList();
        boolean trouve = false;
        int line = 1;
        
        while(!trouve){//afficherListe(open1);
            actD = open1.get(open1.size()-1);//ln(line+" -> On etudie "+actD);
            
            if (listeFinaux.contains(actD)){
                    trouve = true;
                    finalAtteint = actD;
            }
            if (!actD.getListeFils().isEmpty()){
                ajouterDansOpen(open1, close1, actD, actD.getListeFils());
            } 
           
            open1.remove(actD);
            close1.add(actD);
            line++;
        }
        long fin = System.currentTimeMillis();
        System.out.println("temps de A* : "+(fin-debut)+" finalAtteint "+finalAtteint.toString());
        if (!finalAtteint.equals(initial)) finalAtteint.afficherChainnage();
        else System.out.println("Aucune sortie acceccible dans ce labyrinthe !!!");
        
        return finalAtteint;
    }
    
    public double distance(int a, int b, int c, int d){
        return (Math.abs(c-a) + Math.abs(d-b));
    }
    
   
    
}


class ComparerateurNoeud implements Comparator<Noeud>{
 
  public int compare(Noeud n,Noeud m){
     return Integer.parseInt(Math.round(m.f-n.f)+"");
  }
}