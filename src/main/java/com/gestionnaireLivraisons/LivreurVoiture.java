package com.gestionnaireLivraisons;

/**
 * La classe de livreur en voiture
 */

public class LivreurVoiture extends Livreur {

    public LivreurVoiture(int id, String nom) {
        super(id, nom);
    }

    @Override
    public int capaciteLivraison() {
        return 5;
    }

    @Override
    public double calculerRevenu() {
        return nbLivraisonsEffectuees() * 7.5;
    }

    @Override
    public String toString() {
        return "Livreur voiture : id=" + getId() + ", nom=" + getNom();
    }
}
