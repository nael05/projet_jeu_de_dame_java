public class Dame extends Piece {
    public Dame(String couleur) {
        super(couleur);
    }

    @Override
    public boolean isDame() {
        return true;
    }
}