package com.gestionnaireLivraisons;

public interface IListeChaineeLivreurs {
    /**
     * Ajouter un nouvel objet livreur à la liste.
     * L'ajout se fait en fin de liste.
     *
     * Précondition : Il ne faut pas avoir 2 livreurs ayant le même ID
     * dans la liste.
     *
     * @param unLivreur Objet Livreur à ajouter.
     * @throws ListeChaineeException lancée s'il existe un livreur avec le ID.
     */
    void ajouter(Livreur unLivreur) throws ListeChaineeException;

    /**
     * Supprime un livreur à partir de son identifiant.
     *
     * @param idLivreur : identifiant du livreur à supprimer.
     * @return true, sinon false si le livreur n'existe pas.
     */
    boolean supprimer(int idLivreur);

    /**
     * Recherche l'objet Livreur à partir de son identifiant
     *
     * @param idLivreur identifiant du livreur à retrouver.
     * @return l'objet livreur trouvé, ou null s'il n'existe pas.
     */
    Livreur rechercher(int idLivreur);

    /**
     * Retourne le nombre d'éléments se trouvant dans la liste chaînée.
     *
     * @return Le nombre d'éléments.
     */
    int taille();

    /**
     * Crée et retourne un tableau de livreurs pour cette liste chaînée de livreurs.
     *
     * @return Le tableau de livreurs.
     */
    Livreur[] toArray();
}
