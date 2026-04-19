package com.vue;

import com.gestionnaireLivraisons.GestionnaireLivraisons;
import com.gestionnaireLivraisons.Livraison;
import com.gestionnaireLivraisons.Priorite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * La classe pour les boites de dialogues d'ajout d'une livraison.
 *
 */
public class AjoutLivraisonDialogue extends JDialog {
    final private MiniServerUI miniServerUI;
    final private GestionnaireLivraisons gestionnaireLivraisons;

    private JTextField champLot;
    private JComboBox<String> comboPriorite;

    /**
     * Le constructeur pour la boite de dialogue d'ajout d'une livrason.
     *
     * @param miniServerUI La fenêtre propriétaire de cette boite de dialogue.
     * @param gestionnaireLivraisons Le gestionnaire de livraisons associé.
     */
    public AjoutLivraisonDialogue(MiniServerUI miniServerUI, GestionnaireLivraisons gestionnaireLivraisons) {
        super(miniServerUI, "Ajout d'une livraison", true);
        this.miniServerUI = miniServerUI;
        this.gestionnaireLivraisons = gestionnaireLivraisons;
        this.initialiserComposants();
    }

    /**
     * Affichage et gestion de la boite de dialogue pour l'ajout d'une livraison.
     *
     */
    public void initialiserComposants() {
        this.setLayout(new BorderLayout());


        JPanel panneauSaisie = new JPanel(new GridLayout(2, 2, 10, 10));
        panneauSaisie.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panneauSaisie.add(new JLabel("Numéro du lot :"));
        this.champLot = new JTextField();
        panneauSaisie.add(this.champLot);

        panneauSaisie.add(new JLabel("Priorité :"));
        this.comboPriorite = new JComboBox<>(new String[]{"NORMALE", "URGENTE"});
        panneauSaisie.add(this.comboPriorite);

        this.add(panneauSaisie, BorderLayout.CENTER);


        JPanel panneauBoutons = new JPanel();

        JButton boutonAjouter = new JButton("Ajouter");
        boutonAjouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                traiterAjout();
            }
        });

        JButton boutonAnnuler = new JButton("Annuler");
        boutonAnnuler.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        panneauBoutons.add(boutonAjouter);
        panneauBoutons.add(boutonAnnuler);
        this.add(panneauBoutons, BorderLayout.SOUTH);

        this.setSize(350, 180);
        this.setLocationRelativeTo(miniServerUI);
        this.setVisible(true);
    }


    private void traiterAjout() {
        try {
            int lot = Integer.parseInt(this.champLot.getText().trim());

            if (!Livraison.validerLotLivraison(lot)) {
                afficherErreur("Numéro de lot invalide. Le lot doit être entre 1 et "
                        + (Livraison.lotMaximal() + 1) + ".");
                return;
            }

            String choix = (String) this.comboPriorite.getSelectedItem();
            Priorite priorite = choix.equals("URGENTE") ? Priorite.URGENTE : Priorite.NORMALE;

            ajouterLivraison(lot, priorite);

            JOptionPane.showMessageDialog(this, "Livraison ajoutée avec succès.",
                    "Confirmation", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (NumberFormatException ex) {
            afficherErreur("Le numéro de lot doit être un nombre entier.");
        }
    }

    /**
     * Affiche une boite de dialogue pour les erreurs de saisies.
     *
     * @param msg Le message à afficher dans la boite.
     */
    private void afficherErreur(String msg) {
        String[] erreurOptions = {"Opps..."};
        JOptionPane.showOptionDialog(this.miniServerUI, msg,
                "Erreur", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, erreurOptions, erreurOptions[0]);
    }

    /**
     * Ajoute une livraison à la liste des livraisons à effectuer (mise à jour du modèle)
     *
     * @param lot      Le lot de la livraison.
     * @param priorite La priorité de la livraison.
     */
    private void ajouterLivraison(int lot, Priorite priorite) {
        Livraison livraison = new Livraison(priorite, lot);
        this.gestionnaireLivraisons.getLivraisonsAEffectuer().ajouter(livraison);
        this.gestionnaireLivraisons.notifierObservateurs();
    }
}
