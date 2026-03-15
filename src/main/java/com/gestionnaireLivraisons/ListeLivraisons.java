package com.gestionnaireLivraisons;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * La classe qui liste des livraisons.
 */
public class ListeLivraisons implements IListeLivraisons {
    // Les livraisons stockées dans une ArrayList
    private ArrayList<Livraison> livraisons = new ArrayList<>();

    /**
     * Ajout d'une livraison à la liste de livraisons.
     *
     * @param livraison La livraison à ajouter.
     */
    public void ajouter(Livraison livraison) {

        livraisons.add(livraison);

    }

    /**
     * Supprime une livraison par son id.
     *
     * @param idLivraison L'id de la livraison à supprimer.
     * @return La livraison supprimée ou null si non trouvée.
     */
    public Livraison supprimer(int idLivraison) {
        int index = chercher(idLivraison);

        if (index != -1) {
            return livraisons.remove(index);
        }
        return null;
    }

    /**
     * Recherche une livraison par son id et la retourne.
     *
     * @param idLivraison L'id de la livraison à chercher.
     * @return La livraison trouvée ou null si non trouvée.
     */
    public Livraison rechercher(int idLivraison) {

        int index = chercher(idLivraison);

        if (index != -1) {
            return livraisons.get(index);
        }
        return null;
    }

    /**
     * Supprime toutes les livraisons en cours pour ce livreur.
     *
     */
    public void vider() {
        livraisons.clear();
    }

    /**
     * Teste si la liste est vide.
     *
     * @return true si la liste est vide, false sinon.
     */
    public boolean estVide() {
        return livraisons.isEmpty();

    }

    /**
     * Retourne le nombre de livraisons présentes dans cette liste.
     *
     * @return Le nombre de livraisons.
     */
    public int taille() {
        return livraisons.size();
    }

    /**
     * Retourne un itérateur pour cette liste.
     *
     * @return Un itérateur pour cette liste.
     */
    @Override
    public ListIterator<Livraison> iterator() {
        return livraisons.listIterator();
    }

    /**
     * Méthode privée pour chercher l'indice d'une livraison.
     *
     * @param idLivraison L'id de la livraison à chercher.
     * @return L'indice de la livraison trouvée ou -1 si non trouvée.
     */
    private int chercher(int idLivraison) {

        for (int i = 0; i < livraisons.size(); i++) {
            if (livraisons.get(i).getId() == idLivraison) {
                return i;
            }
        }
        return -1;
    }
}
