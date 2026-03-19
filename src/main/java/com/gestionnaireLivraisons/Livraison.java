package com.gestionnaireLivraisons;

import java.util.ArrayList;

/**
 * La classe qui modélise une livraison.
 */
public class Livraison implements Comparable<Livraison> {

    // Les données membres statiques
    // TODO : À compléter/modifier
    private static final int MAX_TENTATIVES = 3;
    private static int compteurID = 1;

    // Les attributs d'instance
    // TODO : À compléter/modifier
    private int id;
    private Priorite priorite;
    private int tentative;
    private int lot;
    private Statut statut;

    /**
     * Constructeur d'une livraison.
     *
     * @param priorite La priorité de la nouvelle livraison.
     * @param lot      Le lot auquel cette livraison appartient.
     */

    public Livraison(Priorite priorite, int lot) {
        this.id = prochainID();
        this.priorite = priorite;
        this.tentative = 0;
        this.lot = lot;
        this.statut = Statut.EN_ATTENTE;
    }


    /**
     * Produit un nouvel ID pour la Livraison
     */
    private static int prochainID() {
        int id = compteurID;
        compteurID++;
        return id;
    }

    /**
     * Retourne l'identifiant de cette livraison.
     *
     * @return L'id de cette livraison.
     */
    public int getId() {
        return id;
    }

    /**
     * Retourne la priorité de cette livraison.
     *
     * @return La priorite de cette livraison.
     */
    public Priorite getPriorite() {
        return priorite;
    }

    /**
     * Retourne la tentative pour cette livraison.
     *
     * @return La tentative de cette livraison.
     */
    public int getTentative() {
        return tentative;
    }

    /**
     * Retourne le lot auquel appartient cette livraison.
     *
     * @return Le lot de cette livraison.
     */
    public int getLot() {
        return lot;
    }

    /**
     * Mutateur pour le statut de la livraison.
     *
     */
    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    /**
     * Ajoute UN au numéro de tentative pour cette livraison.
     *
     * @return False si on a atteint le nombre maximal de tentatives pour cette livraison. True sinon.
     */
    public void nouvelleTentative() {
        if (this.tentative < MAX_TENTATIVES) {
            this.tentative++;
        }
    }

    public boolean resteTentatives() {
        return this.tentative < MAX_TENTATIVES;
    }

    /**
     * Construit et retourne une chaîne de caractères équivalente à cette livraison.
     *
     * @return String La chaîne qui représente cette livraison.
     */
    @Override
    public String toString() {

        return "Livraison{" +
                "id=" + id +
                ", priorite=" + priorite +
                ", tentative=" + tentative +
                ", lot=" + lot +
                ", statut=" + statut +
                '}';

    }

    /**
     * Compare cette livraison avec une autre livraison.
     *
     * @param autreLivraison La seconde livraison à comparer avec cette livraison.
     * @return Le résultat de la comparaison au sens de l'interface Comparable<T>.
     */
    @Override
    public int compareTo(Livraison autreLivraison) {

        if (this.lot < autreLivraison.lot) return -1;
        if (this.lot > autreLivraison.lot) return 1;

        if (this.priorite != autreLivraison.priorite) {
            if (this.priorite == Priorite.URGENTE) return -1;
            return 1;
        }

        if (this.tentative > autreLivraison.tentative) return -1;
        if (this.tentative < autreLivraison.tentative) return 1;

        return 0;
    }
    
}
