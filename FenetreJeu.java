import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class FenetreJeu extends JFrame implements ActionListener {
    private Jeu jeu;
    private CaseButton[][] boutons;
    private JTextArea zoneHistorique;
    private int selectedX = -1, selectedY = -1;

    private class CaseButton extends JButton {
        private Piece piece;
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (piece != null) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int margin = 8;
                int diameter = Math.min(getWidth(), getHeight()) - (margin * 2);
                int x = (getWidth() - diameter) / 2;
                int y = (getHeight() - diameter) / 2;
                g2.setColor(piece.getCouleur().equals("NOIR") ? Color.BLACK : Color.WHITE);
                g2.fillOval(x, y, diameter, diameter);
                g2.setColor(Color.LIGHT_GRAY);
                g2.drawOval(x, y, diameter, diameter);
                if (piece.isDame()) {
                    g2.setColor(Color.RED);
                    g2.setStroke(new BasicStroke(3));
                    g2.drawOval(x + diameter/4, y + diameter/4, diameter/2, diameter/2);
                }
            }
        }
        public void setPiece(Piece p) { this.piece = p; }
    }

    public FenetreJeu() {
        jeu = new Jeu();
        boutons = new CaseButton[10][10];
        setTitle("Jeu de Dames International 10x10");
        setSize(900, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelPlateau = new JPanel(new GridLayout(10, 10));
        for (int y = 9; y >= 0; y--) {
            for (int x = 0; x < 10; x++) {
                boutons[y][x] = new CaseButton();
                boutons[y][x].addActionListener(this);
                if ((x + y) % 2 != 0) boutons[y][x].setBackground(Color.DARK_GRAY);
                else {
                    boutons[y][x].setBackground(Color.LIGHT_GRAY);
                    boutons[y][x].setEnabled(false);
                }
                panelPlateau.add(boutons[y][x]);
            }
        }
        add(panelPlateau, BorderLayout.CENTER);

        zoneHistorique = new JTextArea();
        zoneHistorique.setEditable(false);
        JScrollPane scroll = new JScrollPane(zoneHistorique);
        scroll.setPreferredSize(new Dimension(200, 750));
        add(scroll, BorderLayout.EAST);

        mettreAJourAffichage();
        setVisible(true);
    }

    private void mettreAJourAffichage() {
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                boutons[y][x].setPiece(jeu.getPlateau().getPieceAt(x, y));
                boutons[y][x].repaint();
            }
        }
        zoneHistorique.setText("--- HISTORIQUE ---\n");
        for (String ligne : jeu.getHistorique()) zoneHistorique.append(ligne + "\n");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        CaseButton src = (CaseButton) e.getSource();
        int xClick = -1, yClick = -1;
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                if (boutons[y][x] == src) { xClick = x; yClick = y; }
            }
        }

        if (selectedX == -1) {
            Piece p = jeu.getPlateau().getPieceAt(xClick, yClick);
            if (p != null && p.getCouleur().equals(jeu.getPlateau().getJoueurActuel().getCouleur())) {
                selectedX = xClick; selectedY = yClick;
                boutons[yClick][xClick].setBorder(BorderFactory.createLineBorder(Color.RED, 3));
            }
        } else {
            boolean ok = jeu.jouerCoup(selectedX, selectedY, xClick, yClick);
            boutons[selectedY][selectedX].setBorder(UIManager.getBorder("Button.border"));
            if (!jeu.getPlateau().isSautEnCours() || !ok) { selectedX = -1; selectedY = -1; }
            else { selectedX = xClick; selectedY = yClick; boutons[yClick][xClick].setBorder(BorderFactory.createLineBorder(Color.RED, 3)); }
            mettreAJourAffichage();
            if (jeu.estFini()) JOptionPane.showMessageDialog(this, "Fini ! Gagnant : " + jeu.getVainqueur());
        }
    }
}
