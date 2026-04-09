import java.util.*;

public class Jeu {
    private Plateau plateau;
    private List<String> historique = new ArrayList<>();

    public Jeu() 
        plateau = new Plateau(new Joueur("Noir", "NOIR"), new Joueur("Blanc", "BLANC"));
    }

    public boolean jouerCoup(int x1, int y1, int x2, int y2) {
        String j = plateau.getJoueurActuel().getCouleur();
        boolean ok = plateau.deplacer(x1, y1, x2, y2);
        if (ok) historique.add(j + " : " + (char)('A'+x1) + (y1+1) + "->" + (char)('A'+x2) + (y2+1));
        return ok;
    }

    public Plateau getPlateau() { return plateau; }
    public boolean estFini() { return plateau.estPartieTerminee(); }
    public String getVainqueur() { return plateau.getJoueurActuel().getCouleur().equals("NOIR") ? "BLANC" : "NOIR"; }
    public List<String> getHistorique() { return historique; }
}
