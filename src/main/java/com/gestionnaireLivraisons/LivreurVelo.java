package com.gestionnaireLivraisons;

/**
 * La classe de livreur à vélo
 */

public class LivreurVelo extends Livreur {

    public LivreurVelo(int id, String nom) {
        super(id, nom);
    }

    @Override
    public int capaciteLivraison() {
        return 2;
    }

    @Override
    public double calculerRevenu() {
        return nbLivraisonsEffectuees() * 5.0;
    }

    @Override
    public String toString() {
        return "Livreur vélo : id=" + getId() + ", nom=" + getNom();
    }
}
