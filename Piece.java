public abstract class Piece {
    protected String couleur;

    public Piece(String couleur) {
        this.couleur = couleur;
    }

    public String getCouleur() {
        return couleur;
    }

    public abstract boolean isDame();
}