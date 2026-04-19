package com.vue;

import com.atoudeft.serveur.Serveur;
import com.controleur.EcouteurListeLivreurs;
import com.controleur.EcouteurMenuApplication;
import com.gestionnaireLivraisons.GestionnaireLivraisons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * La classe principale de l'interface utilisateur de l'application MiniServer
 *
 */
public class MiniServerUI extends JFrame {
    final private Serveur serveur;
    final private GestionnaireLivraisons gestionnaireLivraisons;

    /**
     * Constructeur pour l'interface graphique
     *
     * @param serveur La référence du serveur utilisé.
     * @param gestionnaireLivraisons  Le gestionnaire de livraisons.
     */
    public MiniServerUI(Serveur serveur, GestionnaireLivraisons gestionnaireLivraisons) {
        this.serveur = serveur;
        this.gestionnaireLivraisons = gestionnaireLivraisons;
        this.initialiserComposants();
        this.configurerFenetrePrincipale();
    }

    /**
     * Getter pour le gestionnaire de livraisons.
     *
     * @return Le gestionnaire de livraisons.
     */
    public GestionnaireLivraisons getGestionnaireLivraisons() {
        return gestionnaireLivraisons;
    }

    /**
     * Configure la fenêtre graphique.
     *
     */
    private void configurerFenetrePrincipale() {
        // Configuration de la fenêtre
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setTitle("Gestionnaire de livraisons");
        this.setSize(980, 450);
        this.setLocation(100, 100);
        this.setVisible(true);
    }

    /**
     * Création et placement des composants de la fenêtre principale :
     *   - Création du menu Application est de ses items
     *   - Création des trois panneaux de la fenêtre
     */
    private void initialiserComposants() {

        EcouteurMenuApplication ecouteurMenu = new EcouteurMenuApplication(this);


        JMenuBar menuBar = new JMenuBar();
        JMenu menuApplication = new JMenu("Application");

        JMenuItem itemStatistiques = new JMenuItem("Afficher les statistiques");
        itemStatistiques.setActionCommand("STATISTIQUES");
        itemStatistiques.addActionListener(ecouteurMenu);

        JMenuItem itemAjouterLivraison = new JMenuItem("Ajouter Livraison");
        itemAjouterLivraison.setActionCommand("AJOUTER_LIVRAISON");
        itemAjouterLivraison.addActionListener(ecouteurMenu);

        JMenuItem itemQuitter = new JMenuItem("Quitter l'application");

        itemQuitter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quitter();
            }
        });

        menuApplication.add(itemStatistiques);
        menuApplication.add(itemAjouterLivraison);
        menuApplication.addSeparator();
        menuApplication.add(itemQuitter);

        menuBar.add(menuApplication);
        this.setJMenuBar(menuBar);


        PanneauLivreurs panneauLivreurs = new PanneauLivreurs(this, gestionnaireLivraisons);
        PanneauLivraisons panneauLivraisons = new PanneauLivraisons(gestionnaireLivraisons);
        PanneauConsole panneauConsole = new PanneauConsole(gestionnaireLivraisons);


        EcouteurListeLivreurs ecouteurListeLivreurs = new EcouteurListeLivreurs(panneauLivreurs);
        panneauLivreurs.enregisterEcouteur(ecouteurListeLivreurs);


        JSplitPane splitHaut = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panneauLivreurs, panneauLivraisons);
        JSplitPane splitPrincipal = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitHaut, panneauConsole);

        this.setLayout(new BorderLayout());
        this.add(splitPrincipal, BorderLayout.CENTER);
    }

    /**
     * Quitter l'application :
     * - arrêter le serveur
     * - quitter le gestionnaire de livraisons
     * - libérer la fenêtre
     * - quitter l'application
     */
    public void quitter() {
        this.serveur.arreter();
        this.gestionnaireLivraisons.quitter();
        this.dispose();
        System.exit(0);
    }
}
