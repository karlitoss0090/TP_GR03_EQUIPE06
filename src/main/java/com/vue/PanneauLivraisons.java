package com.vue;

import com.gestionnaireLivraisons.*;
import com.observer.Observable;
import com.observer.Observateur;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

/**
 * La classe qui constitue la table des livraisons.
 *
 */
public class PanneauLivraisons extends JPanel implements Observateur {

    private ComposantTable tableLivraisons;

    final private String[] nomsColonnes = {"Id", "Lot", "Priorité", "Tentatives", "Statut"};
    final private boolean[] donneesCentrees = {true, true, true, true, true};

    private GestionnaireLivraisons gestionnaireLivraisons;

    /**
     * Constructeur pour la classe PanneauLivraisons
     *
     * @param gestionnaireLivraisons Le gestionnaire de livraisons associé.
     */
    public PanneauLivraisons(GestionnaireLivraisons gestionnaireLivraisons) {
        this.gestionnaireLivraisons = gestionnaireLivraisons;

        this.setLayout(new BorderLayout());

        this.tableLivraisons = new ComposantTable("Liste des livraisons", 400, 300, nomsColonnes);
        this.add(this.tableLivraisons, BorderLayout.CENTER);

        // S'enregistrer comme observateur du gestionnaire de livraisons
        this.gestionnaireLivraisons.ajouterObservateur(this);
    }

    @Override
    public void seMettreAJour(Observable observable) {
        Vector<Vector<String>> donnees = new Vector<>();

        // Livraisons à effectuer (en attente)
        for (Livraison livraison : this.gestionnaireLivraisons.getLivraisonsAEffectuer()) {
            donnees.add(creerLigneLivraison(livraison));
        }

        // Livraisons en cours et effectuées par chaque livreur
        for (Livreur livreur : this.gestionnaireLivraisons.getLivreursEnregistres()) {
            for (Livraison livraison : livreur.getLivraisonsEnCours()) {
                donnees.add(creerLigneLivraison(livraison));
            }
            for (Livraison livraison : livreur.getLivraisonsEffectuees()) {
                donnees.add(creerLigneLivraison(livraison));
            }
        }

        // Livraisons échouées
        for (Livraison livraison : this.gestionnaireLivraisons.getLivraisonsEchouees()) {
            donnees.add(creerLigneLivraison(livraison));
        }

        this.tableLivraisons.mettreAJour(donnees);
    }

    /**
     * Crée une ligne de données pour une livraison.
     *
     * @param livraison La livraison.
     * @return Le vecteur représentant la ligne.
     */
    private Vector<String> creerLigneLivraison(Livraison livraison) {
        Vector<String> ligne = new Vector<>();
        ligne.add(String.valueOf(livraison.getId()));
        ligne.add(String.valueOf(livraison.getLot()));
        ligne.add(livraison.getPriorite().toString());
        ligne.add(String.valueOf(livraison.getTentative()));
        ligne.add(livraison.getStatut().toString());
        return ligne;
    }
}
