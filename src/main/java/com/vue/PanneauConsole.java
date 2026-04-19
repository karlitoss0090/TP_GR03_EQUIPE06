package com.vue;

import com.gestionnaireLivraisons.GestionnaireLivraisons;
import com.observer.Observable;
import com.observer.Observateur;

import javax.swing.*;
import java.awt.*;

/**
 * La classe qui constitue la console dans l'interface graphique.
 *
 */
public class PanneauConsole extends JPanel implements Observateur
{

    private JTextArea console;
    private GestionnaireLivraisons gestionnaireLivraisons;

    // TODO : À compléter/modifier

    /**
     * Constructeur pour cette classe.
     *
     * @param gestionnaireLivraisons Le gestionnaire de livraisons associé.
     */
    public PanneauConsole(GestionnaireLivraisons gestionnaireLivraisons) {
        this.gestionnaireLivraisons = gestionnaireLivraisons;

        this.setLayout(new BorderLayout());


        JLabel labelTitre = new JLabel("Console", SwingConstants.CENTER);
        labelTitre.setFont(Config.fonte);
        this.add(labelTitre, BorderLayout.NORTH);


        this.console = new JTextArea();
        this.console.setEditable(false);
        this.console.setBackground(Color.BLACK);
        this.console.setForeground(Color.GREEN);
        this.console.setFont(Config.fonte);


        JScrollPane scrollPane = new JScrollPane(this.console);
        this.add(scrollPane, BorderLayout.CENTER);


        this.gestionnaireLivraisons.ajouterObservateur(this);
    }

    @Override
    public void seMettreAJour(Observable observable) {
        String trace = this.gestionnaireLivraisons.consommerTrace();
        if (trace != null && !trace.isEmpty()) {
            this.console.append(trace + "\n");

            this.console.setCaretPosition(this.console.getDocument().getLength());
        }
    }
}

