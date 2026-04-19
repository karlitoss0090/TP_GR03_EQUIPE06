package com.vue;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.Vector;

/**
 * Un composant qui permet d'afficher des informations dans une table.
 * Ce composant peut être utilisé dans plusieurs panneaux.
 *
 */
public class ComposantTable extends JPanel {
    private final JTable table;
    private final String[] nomsColonnes;

    /**
     * Un tableau pour indiquer si les données de la colonne doivent être centrées (true) ou non (false)
     * Il doit être de la même taille que le tableau nomsColonnes
     *
     * L'utilisation de cet attribut est optionnel. Le but est seulement esthétique
     * Vous pouvez le laisser à null et ne pas l'utiliser dans votre code.
     *
     */
    private final boolean[] donneesCentrees;

    /**
     * Constructeur pour ComposantTable.
     *
     * @param titre   Le titre de la grille.
     * @param largeur La largeur de la grille.
     * @param hauteur La hauteur de la grille.
     * @param nomsColonnes Tableau des noms de colonnes
     * @param donneesCentrees La hauteur de la grille.
     */
    public ComposantTable(String titre, int largeur, int hauteur, String[] nomsColonnes, boolean[] donneesCentrees) {
        this.nomsColonnes = nomsColonnes;
        this.donneesCentrees = donneesCentrees;
        this.table = new JTable();
        this.table.setFont(Config.fonte); // considère la fonte définie dans Config

        // Modèle non-éditable
        DefaultTableModel modele = new DefaultTableModel(nomsColonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.table.setModel(modele);

        // Sélection d'un seul élément à la fois
        this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Empêcher la réorganisation des colonnes
        this.table.getTableHeader().setReorderingAllowed(false);

        // Layout du panneau
        this.setLayout(new BorderLayout());

        // Affichage du titre de la table
        JLabel labelTitre = new JLabel(titre, SwingConstants.CENTER);
        labelTitre.setFont(Config.fonte);
        this.add(labelTitre, BorderLayout.NORTH);

        // Panneau de défilement vertical
        JScrollPane scrollPane = new JScrollPane(this.table);
        scrollPane.setPreferredSize(new Dimension(largeur, hauteur));
        this.add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Constructeur (complété)
     *
     * @param titre
     * @param largeur
     * @param hauteur
     * @param nomsColonnes
     */
    public ComposantTable(String titre, int largeur, int hauteur, String[] nomsColonnes) {
        this(titre, largeur, hauteur, nomsColonnes , null) ;
    }

    /**
     * Enregistre un écouteur de souris sur la table
     *
     * @param ml L'écouteur de clic souris qui sera averti si un évènement de clic survient sur cette table.
     */
    public void enregistrerEcouteur(MouseListener ml) {
        this.table.addMouseListener(ml);
    }

    /**
     * Retourne l'index de la ligne qui est actuellement sélectionnée.
     * Les index commencent à 0.
     *
     * @return La ligne sélectionnée ou -1 síl n'y a aucune sélection.
     */
    public int ligneSelectionnee() {
        return this.table.getSelectedRow();
    }

    /**
     * Retourne la donnée se trouvant à une ligne/colonne données en paramètres.
     *
     * @param ligne La ligne où trouver la donnée.
     * @param colonne La colonne où trouver la donnée.
     * @return La donnée.
     */
    public String lireCase(int ligne, int colonne) {
        return (String) (((DefaultTableModel) this.table.getModel()).getDataVector().get(ligne).get(colonne));
    }

    /**
     * Met à jour les données de la table avec la matrice de données passée en paramètre.
     *
     * @param donnees La matrice de données.
     */
    public void mettreAJour(Vector<Vector<String>> donnees) {
        DefaultTableModel modele = (DefaultTableModel) this.table.getModel();
        modele.setRowCount(0);

        if (donnees != null) {
            for (Vector<String> ligne : donnees) {
                modele.addRow(ligne);
            }
        }
    }

}
