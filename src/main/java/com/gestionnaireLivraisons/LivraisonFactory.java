package com.gestionnaireLivraisons;

import java.util.ArrayList;

/**
 * Implémente la création d'objets Livraison pour l'initialisation du système.
 * Au démarrage du serveur, un nombre d'objets Livraisons sont créés.
 *
 */
public class LivraisonFactory {

    // Pour stocker les données des livraisons, sous la forme de chaines de caractères.
    // Chaque chaine de caractères contient les données d'une livraison
    // Les données d'une livraison sont séparées par le 2 points ":"
    private static String[] dataLivraisons;

    /**
     * Initialisation d'un tableau des données de livraison
     */
    static {
        // Possibilité 1 : pour l'initialisation des données de livraison
        livraisonsSerieI();

        // Possibilité 2 : pour l'initialisation des données de livraison
        // livraisonsSerieII() ;
    }

    /**
     * Données pour 6 livraisons
     * Le format : id:lot:priorité
     */
    private static void livraisonsSerieI() {
        dataLivraisons = new String[6];
        dataLivraisons[0] = "1:NORMALE";
        dataLivraisons[1] = "1:URGENTE";
        dataLivraisons[2] = "1:NORMALE";
        dataLivraisons[3] = "2:URGENTE";
        dataLivraisons[4] = "2:NORMALE";
        dataLivraisons[5] = "2:NORMALE";
    }

    /**
     * Données pour 10 livraisons
     * Le format : id:lot:priorité
     */
    private static void livraisonsSerieII() {
        dataLivraisons = new String[10];
        dataLivraisons[0] = "1:NORMALE";
        dataLivraisons[1] = "1:URGENTE";
        dataLivraisons[2] = "1:NORMALE";
        dataLivraisons[3] = "1:URGENTE";
        dataLivraisons[4] = "1:NORMALE";
        dataLivraisons[5] = "2:NORMALE";
        dataLivraisons[6] = "2:URGENTE";
        dataLivraisons[7] = "2:URGENTE";
        dataLivraisons[8] = "2:NORMALE";
        dataLivraisons[9] = "2:NORMALE";
    }

    /**
     * Méthode pour la création d'objets Livraison à partir de données prédéfinies
     * sous la forme de chaines de caractères
     * 0
     *
     * @return
     */
    // new//  FilePrioriteLivraisons();
    public static FilePrioriteLivraisons populateFileLivraisons() {
        // System.err.println ("Méthode non implémentée : LivraisonFactory::populateLivraison() - À Compléter") ;
        FilePrioriteLivraisons filePrioriteLivraisons = null;

        for (String donnee : dataLivraisons) {

            String[] parties = donnee.split(":");

            int lot = Integer.parseInt(parties[0]);
            Priorite priorite = Priorite.valueOf(parties[1]);

            Livraison livraison = new Livraison(priorite, lot);

            filePrioriteLivraisons.ajouter(livraison);
        }

        return filePrioriteLivraisons;
    }

    /**
     *
     */
    private static void creationLivraisonTest1() {
        FilePrioriteLivraisons res = populateFileLivraisons();
        while (!res.estVide()) {
            System.out.println(res.retirer());
        }
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        creationLivraisonTest1();
    }
}
