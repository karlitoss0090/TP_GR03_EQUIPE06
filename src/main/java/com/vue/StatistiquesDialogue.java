package com.vue;

import com.gestionnaireLivraisons.GestionnaireLivraisons;
import com.gestionnaireLivraisons.Livreur;
import com.observer.Observable;
import com.observer.Observateur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * La classe des boites de dialogues pour l'affichage des statistiques.
 *
 */
public class StatistiquesDialogue extends JDialog implements Observateur {

    final private GestionnaireLivraisons gestionnaireLivraisons;
    private JPanel infos;   // regroupe les informations de statistiques à afficher

    private JLabel labelTotal;
    private JLabel labelEffectuees;
    private JLabel labelEnCours;
    private JLabel labelEchouees;

    /**
     * Le constructeur pour une boite de dialogue de statistiques.
     *
     * @param miniServerUI La fenêtre propriétaire de cette boite de dialogue.
     * @param gestionnaireLivraisons Le gestionnaire de livraisons associé.
     */
    public StatistiquesDialogue(MiniServerUI miniServerUI, GestionnaireLivraisons gestionnaireLivraisons) {
        super(miniServerUI, "Statistiques", true);
        this.gestionnaireLivraisons = gestionnaireLivraisons;
        this.initialiserComposants();

        this.gestionnaireLivraisons.ajouterObservateur(this);

        this.setSize(350, 220);
        this.setLocationRelativeTo(miniServerUI);
        this.setVisible(true);
    }

    /**
     * Prépare la boite de dialogue pour les statistiques
     */
    private void initialiserComposants() {
        this.setLayout(new BorderLayout());

        JPanel panneauInfos = new JPanel(new GridLayout(4, 1, 5, 5));
        panneauInfos.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        this.labelTotal = new JLabel();
        this.labelEffectuees = new JLabel();
        this.labelEnCours = new JLabel();
        this.labelEchouees = new JLabel();

        panneauInfos.add(this.labelTotal);
        panneauInfos.add(this.labelEffectuees);
        panneauInfos.add(this.labelEnCours);
        panneauInfos.add(this.labelEchouees);

        this.add(panneauInfos, BorderLayout.CENTER);

        // Bouton Fermer
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

        // Afficher les statistiques initiales
        mettreAJourStatistiques();
    }

    /**
     * Calcule et affiche les statistiques.
     */
    private void mettreAJourStatistiques() {
        int enAttente = this.gestionnaireLivraisons.getLivraisonsAEffectuer().taille();
        int echouees = this.gestionnaireLivraisons.getLivraisonsEchouees().taille();

        int enCours = 0;
        int effectuees = 0;
        for (Livreur livreur : this.gestionnaireLivraisons.getLivreursEnregistres()) {
            enCours += livreur.nbLivraisonsEnCours();
            effectuees += livreur.nbLivraisonsEffectuees();
        }

        int total = enAttente + enCours + effectuees + echouees;

        this.labelTotal.setText("Nombre total des livraisons : " + total);
        this.labelEffectuees.setText("Nombre des livraisons effectuées : " + effectuees);
        this.labelEnCours.setText("Nombre des livraisons en cours : " + enCours);
        this.labelEchouees.setText("Nombre des livraisons échouées : " + echouees);
    }


    @Override
    public void seMettreAJour(Observable observable) {
        mettreAJourStatistiques();
    }
}
