public class Pion extends Piece {
    public Pion(String couleur) {
        super(couleur);
    }

    @Override
    public boolean isDame() {
        return false;
    }
}