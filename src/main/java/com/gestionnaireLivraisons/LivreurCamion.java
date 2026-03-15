package com.gestionnaireLivraisons;

/**
 * La classe de livreur en camion
 */

public class LivreurCamion extends Livreur {

    public LivreurCamion(int id, String nom) {
        super(id, nom);
    }

    @Override
    public int capaciteLivraison() {
        return 8;
    }

    @Override
    public double calculerRevenu() {
        return nbLivraisonsEffectuees() * 10.0;
    }

    @Override
    public String toString() {
        return "Livreur camion : id=" + getId() + ", nom=" + getNom();
    }
}
