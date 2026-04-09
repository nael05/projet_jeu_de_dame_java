public class Joueur {
    private String nom;
    private String couleur;

    public Joueur(String nom, String couleur) {
        this.nom = nom;
        this.couleur = couleur;
    }

    public String getNom() {
        return nom;
    }

    public String getCouleur() {
        return couleur;
    }
}