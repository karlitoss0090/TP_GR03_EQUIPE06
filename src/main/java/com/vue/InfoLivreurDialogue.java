package com.vue;

import com.gestionnaireLivraisons.*;
import com.observer.Observable;
import com.observer.Observateur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 * Classe qui affiche la boite de dialogue pour les informations d'un livreur.
 *
 */
public class InfoLivreurDialogue extends JDialog implements Observateur {

    private Livreur livreur;

    private ComposantTable grilleLivraisonsEnCours;
    private ComposantTable grilleLivraisonsEffectuees;

    private final String[] nomsColonnesLivraisons = {"Id", "Lot", "Priorité", "Tentative", "Statut"};


    /**
     * Le constructeur de cette boite de dialogue.
     *
     * @param miniServerUI           La fenêtre contenant cette boite de dialogue.
     * @param livreur                Le livreur dont on veut afficher les données (infos personnelles et livraisons).
     */
    public InfoLivreurDialogue(MiniServerUI miniServerUI, Livreur livreur) {
        super(miniServerUI, "Informations du livreur", true);
        this.livreur = livreur;

        this.initialiserComposants();

        // S'enregistrer comme observateur du livreur (Q2.5)
        this.livreur.ajouterObservateur(this);

        this.setSize(600, 500);
        this.setLocationRelativeTo(miniServerUI);
        this.setVisible(true);
    }

    /**
     * Afficher la boite de dialogue contenant les infos du livreur.
     * Cette méthode est invoquée par le constructeur
     *
     */
    private void initialiserComposants() {
        this.setLayout(new BorderLayout());

        // Panneau des labels (id, nom, type, capacité)
        JPanel panneauInfos = new JPanel(new GridLayout(4, 1));
        panneauInfos.add(new JLabel("  Id : " + livreur.getId()));
        panneauInfos.add(new JLabel("  Nom : " + livreur.getNom()));
        panneauInfos.add(new JLabel("  Moyen de livraison : " + livreur.type()));
        panneauInfos.add(new JLabel("  Capacité : " + livreur.capaciteLivraison()));
        this.add(panneauInfos, BorderLayout.NORTH);

        // Panneau central avec les deux tables
        JPanel panneauTables = new JPanel(new GridLayout(2, 1));

        this.grilleLivraisonsEnCours = new ComposantTable(
                "Livraisons en cours", 550, 150, nomsColonnesLivraisons);
        panneauTables.add(this.grilleLivraisonsEnCours);

        this.grilleLivraisonsEffectuees = new ComposantTable(
                "Livraisons effectuées", 550, 150, nomsColonnesLivraisons);
        panneauTables.add(this.grilleLivraisonsEffectuees);

        this.add(panneauTables, BorderLayout.CENTER);

        // Bouton Fermer avec classe anonyme
        JButton boutonFermer = new JButton("Fermer");
        boutonFermer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        JPanel panneauBouton = new JPanel();
        panneauBouton.add(boutonFermer);
        this.add(panneauBouton, BorderLayout.SOUTH);

        // Remplir les tables avec les données actuelles
        this.grilleLivraisonsEnCours.mettreAJour(calculerDonnees(livreur.getLivraisonsEnCours()));
        this.grilleLivraisonsEffectuees.mettreAJour(calculerDonnees(livreur.getLivraisonsEffectuees()));
    }

    /**
     * Prépare les données à afficher pour une liste de livraisons.
     *
     * @param livraisons La liste de livraisons.
     * @return La matrice des données relatives à la liste de livraisons.
     */
    private Vector<Vector<String>> calculerDonnees(IListeLivraisons livraisons) {
        Vector<Vector<String>> donnees = new Vector<>();
        for (Livraison livraison : livraisons) {
            Vector<String> ligne = new Vector<>();
            ligne.add(String.valueOf(livraison.getId()));
            ligne.add(String.valueOf(livraison.getLot()));
            ligne.add(livraison.getPriorite().toString());
            ligne.add(String.valueOf(livraison.getTentative()));
            ligne.add(livraison.getStatut().toString());
            donnees.add(ligne);
        }
        return donnees;
    }

    /**
     * Mise à jour lorsque le livreur (observable) notifie un changement (Q2.5).
     *
     * @param observable L'objet observable qui a changé.
     */
    @Override
    public void seMettreAJour(Observable observable) {
        this.grilleLivraisonsEnCours.mettreAJour(calculerDonnees(livreur.getLivraisonsEnCours()));
        this.grilleLivraisonsEffectuees.mettreAJour(calculerDonnees(livreur.getLivraisonsEffectuees()));
    }
}
