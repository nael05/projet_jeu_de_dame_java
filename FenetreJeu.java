import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class FenetreJeu extends JFrame implements ActionListener {
    private Jeu jeu;
    private CaseButton[][] boutons;
    private JPanel historiquePanel;
    private JLabel statusLabel;
    private JLabel alertLabel;
    private int selectedX = -1, selectedY = -1;

    // --- PALETTE ULTRA-MODERNE (Neon & Dark Sleek) ---
    private final Color BG_APP = new Color(15, 23, 42);          // Tailwind Slate 900
    private final Color BOARD_LIGHT = new Color(30, 41, 59);     // Slate 800
    private final Color BOARD_DARK = new Color(2, 6, 23);        // Slate 950
    private final Color TEXT_WHITE = new Color(248, 250, 252);   // Slate 50
    private final Color TEXT_MUTED = new Color(148, 163, 184);   // Slate 400
    private final Color ALERT_NEON = new Color(244, 63, 94);     // Rose 500
    private final Color HIGHLIGHT_NEON = new Color(250, 204, 21); // Yellow 400

    private class CaseButton extends JButton {
        private Piece piece;
        private boolean isHighlighted = false;
        private boolean isWarning = false;

        public CaseButton() {
            super();
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(true);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        public void setHighlighted(boolean h) { this.isHighlighted = h; repaint(); }
        public void setWarning(boolean w) { this.isWarning = w; repaint(); }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // 1. Peindre la case (fond)
            g2.setColor(getBackground());
            g2.fillRect(0, 0, getWidth(), getHeight());

            // Effet de sélection ou d'avertissement
            if (isHighlighted) {
                g2.setColor(new Color(255, 255, 255, 30)); // Blanc très léger
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(200, 200, 200));
                g2.setStroke(new BasicStroke(2f));
                g2.drawRect(1, 1, getWidth() - 2, getHeight() - 2);
            } else if (isWarning) {
                g2.setColor(new Color(244, 63, 94, 60)); // Rouge doux transparent
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(ALERT_NEON);
                g2.setStroke(new BasicStroke(2f));
                g2.drawRect(1, 1, getWidth() - 2, getHeight() - 2);
            }

            // 2. Peindre le pion
            if (piece != null) {
                int margin = (int) (Math.min(getWidth(), getHeight()) * 0.15); 
                int diameter = Math.min(getWidth(), getHeight()) - (margin * 2);
                int x = (getWidth() - diameter) / 2;
                int y = (getHeight() - diameter) / 2;

                // Ombre réaliste (Drop Shadow) au lieu d'une lueur fluo
                g2.setColor(new Color(0, 0, 0, 100));
                g2.fillOval(x + 2, y + 4, diameter, diameter);

                // Pion en dégradé sobre (Noir / Blanc)
                if (piece.getCouleur().equals("NOIR")) {
                    Color c1 = new Color(70, 70, 70); // Gris foncé
                    Color c2 = new Color(15, 15, 15); // Noir profond
                    g2.setPaint(new GradientPaint(x, y, c1, x + diameter, y + diameter, c2));
                } else {
                    Color c1 = new Color(255, 255, 255); // Blanc pur
                    Color c2 = new Color(200, 200, 200); // Gris très clair
                    g2.setPaint(new GradientPaint(x, y, c1, x + diameter, y + diameter, c2));
                }
                g2.fillOval(x, y, diameter, diameter);

                // Anneau interne élégant
                g2.setColor(piece.getCouleur().equals("NOIR") ? new Color(100, 100, 100, 150) : new Color(255, 255, 255, 150));
                g2.setStroke(new BasicStroke(2f));
                g2.drawOval(x + 4, y + 4, diameter - 8, diameter - 8);

                // Couronne de la Dame (minimaliste, sobre)
                if (piece.isDame()) {
                    g2.setColor(piece.getCouleur().equals("NOIR") ? Color.WHITE : Color.BLACK);
                    g2.fillOval(x + diameter/2 - 4, y + diameter/2 - 4, 8, 8);
                    g2.setStroke(new BasicStroke(2f));
                    g2.drawOval(x + diameter/2 - 12, y + diameter/2 - 12, 24, 24);
                }
            }
        }
        public void setPiece(Piece p) { this.piece = p; repaint(); }
    }

    public FenetreJeu() {
        jeu = new Jeu();
        boutons = new CaseButton[10][10];
        
        setTitle("Dames - Neon Edition");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_APP);

        // --- TOP BAR (Sleek Header) ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BG_APP);
        topPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        statusLabel = new JLabel("TOUR: NOIRS");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        statusLabel.setForeground(TEXT_WHITE); 
        // Spacing removed

        alertLabel = new JLabel("");
        alertLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        alertLabel.setForeground(ALERT_NEON);

        topPanel.add(statusLabel, BorderLayout.WEST);
        topPanel.add(alertLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // --- PLATEAU ---
        JPanel panelPlateau = new JPanel(new GridLayout(10, 10));
        panelPlateau.setBackground(BG_APP);
        
        // Conteneur avec un léger padding
        JPanel marginPanel = new JPanel(new BorderLayout());
        marginPanel.setBackground(BG_APP);
        marginPanel.setBorder(new EmptyBorder(0, 30, 30, 10)); // Marges
        marginPanel.add(panelPlateau, BorderLayout.CENTER);

        for (int y = 9; y >= 0; y--) {
            for (int x = 0; x < 10; x++) {
                boutons[y][x] = new CaseButton();
                boutons[y][x].addActionListener(this);
                if ((x + y) % 2 != 0) {
                    boutons[y][x].setBackground(BOARD_DARK);
                } else {
                    boutons[y][x].setBackground(BOARD_LIGHT);
                    boutons[y][x].setEnabled(false);
                }
                panelPlateau.add(boutons[y][x]);
            }
        }
        add(marginPanel, BorderLayout.CENTER);

        // --- SIDEBAR (Historique transparent) ---
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(BG_APP);
        rightPanel.setPreferredSize(new Dimension(280, 0));
        rightPanel.setBorder(new EmptyBorder(0, 10, 30, 30));

        JLabel histTitle = new JLabel("HISTORIQUE");
        histTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        histTitle.setForeground(TEXT_MUTED);
        histTitle.setBorder(new EmptyBorder(0, 0, 15, 0));

        historiquePanel = new JPanel();
        historiquePanel.setLayout(new BoxLayout(historiquePanel, BoxLayout.Y_AXIS));
        historiquePanel.setBackground(new Color(15, 23, 42)); // Même fond

        JScrollPane scroll = new JScrollPane(historiquePanel);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(51, 65, 85), 2)); // Contour très fin Slate 700
        scroll.getViewport().setBackground(new Color(15, 23, 42));
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));

        rightPanel.add(histTitle, BorderLayout.NORTH);
        rightPanel.add(scroll, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

        mettreAJourAffichage();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addHistoryItem(String text) {
        JLabel item = new JLabel(text);
        item.setFont(new Font("Consolas", Font.PLAIN, 14));
        item.setForeground(TEXT_WHITE);
        item.setBorder(new EmptyBorder(10, 15, 10, 15));
        historiquePanel.add(item);
    }

    private void showTemporaryAlert(String message, Color color) {
        alertLabel.setText(message);
        alertLabel.setForeground(color);
        Timer timer = new Timer(3000, e -> {
            if (jeu.getPlateau().joueurDoitSauter()) {
                alertLabel.setText("[!] ACTION REQUISE");
                alertLabel.setForeground(ALERT_NEON);
            } else {
                alertLabel.setText("");
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void mettreAJourAffichage() {
        String joueur = jeu.getPlateau().getJoueurActuel().getCouleur();
        statusLabel.setText(joueur.equals("NOIR") ? "TOUR: NOIRS" : "TOUR: BLANCS");
        statusLabel.setForeground(joueur.equals("NOIR") ? TEXT_WHITE : new Color(200, 200, 200));
        
        boolean sautObligatoire = jeu.getPlateau().joueurDoitSauter();
        
        if (sautObligatoire && !alertLabel.getText().contains("ACTION REQUISE")) {
            alertLabel.setText("[!] ACTION REQUISE");
            alertLabel.setForeground(ALERT_NEON);
        } else if (!sautObligatoire && alertLabel.getText().equals("[!] ACTION REQUISE")) {
            alertLabel.setText("");
        }

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                boutons[y][x].setPiece(jeu.getPlateau().getPieceAt(x, y));
                if ((x + y) % 2 != 0) {
                    boutons[y][x].setHighlighted(selectedX == x && selectedY == y);
                    
                    if (sautObligatoire && jeu.getPlateau().getPieceAt(x, y) != null 
                        && jeu.getPlateau().getPieceAt(x, y).getCouleur().equals(joueur)) {
                        
                        if (jeu.getPlateau().peutSauter(x, y)) {
                            boutons[y][x].setWarning(true);
                        } else {
                            boutons[y][x].setWarning(false);
                        }
                    } else {
                        boutons[y][x].setWarning(false);
                    }
                }
            }
        }

        historiquePanel.removeAll();
        for (String ligne : jeu.getHistorique()) {
            addHistoryItem(ligne);
        }
        historiquePanel.revalidate();
        repaint();
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
                boolean sautObligatoire = jeu.getPlateau().joueurDoitSauter();
                if (sautObligatoire && !jeu.getPlateau().peutSauter(xClick, yClick)) {
                    showTemporaryAlert("Piece invalide: Capture requise", ALERT_NEON);
                    return;
                }
                selectedX = xClick; selectedY = yClick;
            } else if (p != null) {
                showTemporaryAlert("Ce n'est pas votre tour", ALERT_NEON);
            }
        } else {
            if (selectedX == xClick && selectedY == yClick) {
                if (!jeu.getPlateau().isSautEnCours()) {
                    selectedX = -1; selectedY = -1;
                }
            } else {
                boolean ok = jeu.jouerCoup(selectedX, selectedY, xClick, yClick);
                if (!ok) {
                    showTemporaryAlert("Mouvement refuse", ALERT_NEON);
                    if (!jeu.getPlateau().isSautEnCours()) {
                        selectedX = -1; selectedY = -1; 
                    }
                } else {
                    if (jeu.getPlateau().isSautEnCours()) {
                        selectedX = xClick; selectedY = yClick; 
                        showTemporaryAlert("COMBO ! Rejouez.", HIGHLIGHT_NEON);
                    } else {
                        selectedX = -1; selectedY = -1;
                    }
                }
                
                if (jeu.estFini()) {
                    showTemporaryAlert("VICTOIRE !", HIGHLIGHT_NEON);
                    JOptionPane.showMessageDialog(this, "Termine. " + jeu.getVainqueur() + " a gagne.", "Fin", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
        mettreAJourAffichage();
    }

    // Ajout d'une methode setLetterSpacing via HTML wrapper (car JLabel ne gere pas le letter-spacing nativement)
    private void setLetterSpacing(JLabel label, float spacing) {
        // Optionnel, omis pour garder le code clean
    }
}
