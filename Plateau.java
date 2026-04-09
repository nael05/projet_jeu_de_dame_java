public class Plateau {
    private Case[][] cases = new Case[10][10];
    private Joueur joueurActuel;
    private Joueur joueurNoir;
    private Joueur joueurBlanc;
    private boolean sautEnCours = false;
    private int pieceEnSautX = -1;
    private int pieceEnSautY = -1;

    public Plateau(Joueur jNoir, Joueur jBlanc) {
        this.joueurNoir = jNoir;
        this.joueurBlanc = jBlanc;
        this.joueurActuel = jNoir;
        initialiser();
    }

    public void initialiser() {
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                cases[y][x] = new Case(x, y);
            }
        }
        // 4 rangées de pions NOIRS (lignes 0 à 3)
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 10; x++) {
                if ((x + y) % 2 != 0) cases[y][x].setPiece(new Pion("NOIR"));
            }
        }
        // 4 rangées de pions BLANCS (lignes 6 à 9)
        for (int y = 6; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                if ((x + y) % 2 != 0) cases[y][x].setPiece(new Pion("BLANC"));
            }
        }
    }

    public Piece getPieceAt(int x, int y) {
        if (x < 0 || x > 9 || y < 0 || y > 9) return null;
        return cases[y][x].getPiece();
    }

    public boolean deplacer(int x1, int y1, int x2, int y2) {
        Piece p = getPieceAt(x1, y1);
        if (p == null || !p.getCouleur().equals(joueurActuel.getCouleur())) return false;

        boolean sautObligatoire = joueurDoitSauter();
        boolean estUnSaut = validerSaut(p, x1, y1, x2, y2);
        boolean estUnMouvementSimple = validerMouvementSimple(p, x1, y1, x2, y2);

        // Si un saut est en cours, on doit continuer avec la même pièce
        if (sautEnCours && (x1 != pieceEnSautX || y1 != pieceEnSautY)) return false;
        // La prise est obligatoire
        if (sautObligatoire && !estUnSaut) return false;

        if (estUnMouvementSimple && !sautObligatoire && !sautEnCours) {
            effectuerDeplacement(x1, y1, x2, y2);
            verifierPromotion(x2, y2);
            changerJoueur();
            return true;
        } else if (estUnSaut) {
            // Identifier et supprimer la pièce capturée
            int dx = Integer.signum(x2 - x1);
            int dy = Integer.signum(y2 - y1);
            int cx = x1 + dx;
            int cy = y1 + dy;
            while (cx != x2) {
                if (getPieceAt(cx, cy) != null) {
                    cases[cy][cx].setPiece(null);
                    break;
                }
                cx += dx; cy += dy;
            }

            effectuerDeplacement(x1, y1, x2, y2);
            verifierPromotion(x2, y2);

            // Vérifier si une autre prise est possible (Rafale)
            if (peutSauter(x2, y2)) {
                sautEnCours = true;
                pieceEnSautX = x2;
                pieceEnSautY = y2;
            } else {
                sautEnCours = false;
                changerJoueur();
            }
            return true;
        }
        return false;
    }

    private void effectuerDeplacement(int x1, int y1, int x2, int y2) {
        Piece p = cases[y1][x1].getPiece();
        cases[y1][x1].setPiece(null);
        cases[y2][x2].setPiece(p);
    }

    private boolean validerMouvementSimple(Piece p, int x1, int y1, int x2, int y2) {
        if (x2 < 0 || x2 > 9 || y2 < 0 || y2 > 9 || getPieceAt(x2, y2) != null) return false;
        int dist = Math.abs(x2 - x1);
        if (dist != Math.abs(y2 - y1)) return false;

        if (p.isDame()) {
            int dx = Integer.signum(x2 - x1);
            int dy = Integer.signum(y2 - y1);
            for (int i = 1; i < dist; i++) {
                if (getPieceAt(x1 + i * dx, y1 + i * dy) != null) return false;
            }
            return true;
        } else {
            // Pion : 1 case en avant seulement
            if (dist != 1) return false;
            return p.getCouleur().equals("NOIR") ? y2 == y1 + 1 : y2 == y1 - 1;
        }
    }

    private boolean validerSaut(Piece p, int x1, int y1, int x2, int y2) {
        if (x2 < 0 || x2 > 9 || y2 < 0 || y2 > 9 || getPieceAt(x2, y2) != null) return false;
        int dist = Math.abs(x2 - x1);
        if (dist != Math.abs(y2 - y1)) return false;

        int dx = Integer.signum(x2 - x1);
        int dy = Integer.signum(y2 - y1);

        if (p.isDame()) {
            int ennemis = 0;
            for (int i = 1; i < dist; i++) {
                Piece inter = getPieceAt(x1 + i * dx, y1 + i * dy);
                if (inter != null) {
                    if (inter.getCouleur().equals(p.getCouleur())) return false;
                    ennemis++;
                }
            }
            return ennemis == 1;
        } else {
            // Pion : Saut de 2 cases obligatoire, MAIS direction libre (avant ou arrière)
            if (dist != 2) return false;
            Piece capturee = getPieceAt(x1 + dx, y1 + dy);
            return capturee != null && !capturee.getCouleur().equals(p.getCouleur());
        }
    }

    public boolean peutSauter(int x, int y) {
        Piece p = getPieceAt(x, y);
        if (p == null) return false;
        int[][] dirs = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        for (int[] d : dirs) {
            // Pour un pion, distance fixe de 2. Pour une dame, de 2 à 9.
            int maxDist = p.isDame() ? 10 : 3; 
            for (int dist = 2; dist < maxDist; dist++) {
                if (validerSaut(p, x, y, x + d[0] * dist, y + d[1] * dist)) return true;
            }
        }
        return false;
    }

    private boolean joueurDoitSauter() {
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                Piece p = getPieceAt(x, y);
                if (p != null && p.getCouleur().equals(joueurActuel.getCouleur())) {
                    if (peutSauter(x, y)) return true;
                }
            }
        }
        return false;
    }

    private void verifierPromotion(int x, int y) {
        Piece p = getPieceAt(x, y);
        if (p != null && !p.isDame()) {
            if ((p.getCouleur().equals("NOIR") && y == 9) || (p.getCouleur().equals("BLANC") && y == 0)) {
                cases[y][x].setPiece(new Dame(p.getCouleur()));
            }
        }
    }

    public Joueur getJoueurActuel() { return joueurActuel; }
    public boolean isSautEnCours() { return sautEnCours; }
    private void changerJoueur() { joueurActuel = (joueurActuel == joueurNoir) ? joueurBlanc : joueurNoir; }

    public boolean estPartieTerminee() {
        boolean noirPeutJouer = false, blancPeutJouer = false;
        int n = 0, b = 0;
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                Piece p = getPieceAt(x, y);
                if (p == null) continue;
                if (p.getCouleur().equals("NOIR")) {
                    n++;
                    if (aUnCoupPossible(x, y)) noirPeutJouer = true;
                } else {
                    b++;
                    if (aUnCoupPossible(x, y)) blancPeutJouer = true;
                }
            }
        }
        return n == 0 || b == 0 || (joueurActuel == joueurNoir ? !noirPeutJouer : !blancPeutJouer);
    }

    private boolean aUnCoupPossible(int x, int y) {
        if (peutSauter(x, y)) return true;
        Piece p = getPieceAt(x, y);
        int[][] dirs = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        for (int[] d : dirs) {
            int maxDist = p.isDame() ? 10 : 2;
            for (int i = 1; i < maxDist; i++) {
                if (validerMouvementSimple(p, x, y, x + d[0] * i, y + d[1] * i)) return true;
            }
        }
        return false;
    }
}