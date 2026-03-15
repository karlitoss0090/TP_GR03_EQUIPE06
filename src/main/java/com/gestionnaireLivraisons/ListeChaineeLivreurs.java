package com.gestionnaireLivraisons;


public class ListeChaineeLivreurs implements IListeChaineeLivreurs {
    private Noeud tete;
    private Noeud dernier;
    private int nbreElements;

    /**
     * Constructeur
     */

    public ListeChaineeLivreurs() {
        tete = null;
        dernier = null;
        nbreElements = 0;
    }


    /**
     * Ajoute un objet Livreur à la fin de la liste
     *
     * @param unLivreur Le livreur à ajouter.
     */
    @Override
    public void ajouter(Livreur unLivreur) throws ListeChaineeException {

        if (unLivreur == null) {
            throw new ListeChaineeException("Livreur invalide");
        }

        Noeud nouveau = new Noeud(unLivreur);

        if (tete == null) {
            tete = nouveau;
            dernier = nouveau;
        } else {
            dernier.suivant = nouveau;
            dernier = nouveau;
        }

        nbreElements++;
    }

    /**
     * Supprime un livreur de la liste
     *
     * @param idLivreur : identifiant du livreur à supprimer
     * @return true si suppression, false sinon, car le livreur n'existe pas dans la liste
     */
    @Override
    public boolean supprimer(int idLivreur) {

        Noeud courant = tete;
        Noeud precedent = null;

        while (courant != null) {

            if (courant.livreur.getId() == idLivreur) {

                if (precedent == null) {
                    tete = courant.suivant;
                } else {
                    precedent.suivant = courant.suivant;
                }

                if (courant == dernier) {
                    dernier = precedent;
                }

                nbreElements--;
                return true;
            }

            precedent = courant;
            courant = courant.suivant;
        }

        return false;
    }

    /**
     * Recherche un livreur par son id et le retourne.
     *
     * @param idLivreur identifiant du livreur à retrouver.
     * @return Le livreur ou null si non trouvé.
     */
    @Override
    public Livreur rechercher(int idLivreur) {

        Noeud courant = tete;

        while (courant != null) {

            if (courant.livreur.getId() == idLivreur) {
                return courant.livreur;
            }

            courant = courant.suivant;
        }

        return null;
    }

    /**
     * Retourne le nombre d'éléments se trouvant dans la liste chaînée.
     *
     * @return Le nombre d'éléments.
     */
    @Override
    public int taille() {

        return nbreElements;
    }

    /**
     * Crée et retourne un tableau de livreurs pour cette liste chaînée de livreurs.
     *
     * @return Le tableau de livreurs.
     */
    public Livreur[] toArray() {

        Livreur[] tableau = new Livreur[nbreElements];

        Noeud courant = tete;
        int i = 0;

        while (courant != null) {
            tableau[i] = courant.livreur;
            courant = courant.suivant;
            i++;
        }

        return tableau;
    }

    /**
     * Implémente un neoud de la liste chainée
     *
     */
    private static class Noeud {
        private final Livreur livreur;
        private Noeud suivant;

        Noeud(Livreur livreur) {
            this.livreur = livreur;
            this.suivant = null;
        }
    }
}
