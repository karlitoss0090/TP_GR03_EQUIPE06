package com.vue;

import com.gestionnaireLivraisons.*;
import com.controleur.EcouteurListeLivreurs;
import com.observer.Observable;
import com.observer.Observateur;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

/**
 * Classe de type JPanel pour lister les livreurs enregistrés.
 *
 *
 */
public class PanneauLivreurs extends JPanel implements Observateur {
    // private final JTable table;
    private ComposantTable tableLivreurs;

    final private String[] nomsColonnes = {"Id", "Nom", "Type", "Authentifié"};
    final private boolean[] donneesCentrees = new boolean[]{true, false, true, true};

    private MiniServerUI miniServerUI;
    private GestionnaireLivraisons gestionnaireLivraisons;

    /**
     * Constructeur pour cette classe.
     *
     * @param miniServerUI                La fenêtre qui contient cette gruille.
     * @param gestionnaireLivraisons Le gestionnaire de livraisons associé.
     */
    public PanneauLivreurs(MiniServerUI miniServerUI, GestionnaireLivraisons gestionnaireLivraisons) {
        this.miniServerUI = miniServerUI;
        this.gestionnaireLivraisons = gestionnaireLivraisons;

        this.setLayout(new BorderLayout());

        this.tableLivreurs = new ComposantTable("Liste des livreurs", 300, 300, nomsColonnes);
        this.add(this.tableLivreurs, BorderLayout.CENTER);


        this.gestionnaireLivraisons.ajouterObservateur(this);
    }

    /**
     * Getter pour l'attribut MiniServerUI de cette classe.
     *
     * @return La fenêtre graphique principale dans laquelle se trouve ce panneau.
     */
    public MiniServerUI getMiniServerUI() {
        return this.miniServerUI;
    }

    /**
     * Enregistrer un écouteur pour ce panneau de livreurs.
     *
     * @param ecouteurLL L'écouteur à ajouter.
     */
    public void enregisterEcouteur(EcouteurListeLivreurs ecouteurLL) {
        this.tableLivreurs.enregistrerEcouteur(ecouteurLL);
    }

    /**
     * Retourne l'objet Livreur sélectionné dans la Table,
     * Null si aucun livreur n'est sélectionné
     *
     * @return Le livreur sélectionné dans cette table.
     */
    public Livreur livreurSelectionne() {
        int ligne = this.tableLivreurs.ligneSelectionnee();
        if (ligne == -1) {
            return null;
        }
        // Lire l'id du livreur dans la première colonne
        String idStr = this.tableLivreurs.lireCase(ligne, 0);
        int id = Integer.parseInt(idStr);
        return this.gestionnaireLivraisons.getLivreursEnregistres().rechercher(id);
    }


    @Override
    public void seMettreAJour(Observable observable) {
        Vector<Vector<String>> donnees = new Vector<>();

        for (Livreur livreur : this.gestionnaireLivraisons.getLivreursEnregistres()) {
            Vector<String> ligne = new Vector<>();
            ligne.add(String.valueOf(livreur.getId()));
            ligne.add(livreur.getNom());
            ligne.add(livreur.type());
            ligne.add(livreur.isAuthentifie() ? "✔" : "✘");
            donnees.add(ligne);
        }

        this.tableLivreurs.mettreAJour(donnees);
    }
}

