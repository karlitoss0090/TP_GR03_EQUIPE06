package com.gestionnaireLivraisons;

import java.util.PriorityQueue;

// import static com.gestionnaireLivraisons.Livraison;

/**
 * La classe générique des files de priorité.
 *
 */
public class FilePrioriteLivraisons {
    // Les livraisons stockées dans une file de priorité
    private PriorityQueue<Livraison> file;

    /**
     * Construit une file de priorité.
     */
    public FilePrioriteLivraisons() {
        file = new PriorityQueue<>();
    }

    /**
     * Retire et retourne l'élément le plus prioritaire de la file.
     *
     * @return L'élément le plus prioritaire.
     */
    public Livraison retirer() {

        return file.poll();

    }

    /**
     * Ajoute un élément à la file.
     *
     * @param element L'élément à ajouter à la file.
     */
    public void ajouter(Livraison element) {
        file.add(element);
    }

    /**
     * Ajoute un ensemble d'éléments à la file.
     *
     * @param elements L'ensemble des éléments à ajouter à la file.
     */
    public void ajouterTout(Iterable<Livraison> elements) {
        for (Livraison l : elements) {
            file.add(l);
        }
    }

    /**
     * Indique si la file est vide.
     *
     * @return True si la file est vide. False sinon.
     */
    public boolean estVide() {

        return file.isEmpty();
    }

    /**
     * Retourne le nombre d'éléments dans cette file de priorité.
     *
     * @return Le nombre d'éléments de la file.
     */
    public int taille() {

        return file.size();
    }

    /**
     * Affiche les éléments contenus dans la file.
     *
     */
    public void afficher() {

        for (Livraison l : file) {
            System.out.println(l);
        }
    }
}
