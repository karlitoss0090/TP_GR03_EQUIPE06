package com.gestionnaireLivraisons;

import com.atoudeft.commun.evenement.Arguments;
import com.atoudeft.commun.evenement.Evenement;
import com.atoudeft.commun.evenement.GestionnaireEvenement;
import com.atoudeft.commun.net.Connexion;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * La classe pour gérer les erreurs d'identification des clients
 */
class AuthenticationException extends Exception {
}

/**
 * La classe qui permet de gérer des livraisons selon des évènements envoyés par un livreur (client).
 */
public class GestionnaireLivraisons implements GestionnaireEvenement {
    // Emplacement du fichier contenant la liste des livreurs enregistrés.
    final private static String fichierLivreurs = "src/main/livreurs.txt";

    // Attributs d'ínstance pour un GestionnaireLivraisons
    final private IListeChaineeLivreurs livreursEnregistres;
    final private Hashtable<Connexion, Livreur> livreursAuthentifies;
    final private FilePrioriteLivraisons livraisonsAEffectuer;
    final private IListeLivraisons livraisonsEchouees;

    final private ArrayList<String> messagesId;

    /**
     * Construit un gestionnaire de livraisons.
     *
     */
    public GestionnaireLivraisons() {
        this.livreursEnregistres = new ListeChaineeLivreurs();
        this.lireFichierLivreurs();

        this.livreursAuthentifies = new Hashtable<>();
        this.livraisonsAEffectuer = LivraisonFactory.populateFileLivraisons();
        this.livraisonsEchouees = new ListeLivraisons();

        this.messagesId = new ArrayList<>();
    }

    /**
     * Lit le fichier des livreurs enregistrés.
     */
    private void lireFichierLivreurs() {
        try {
            List<String> lignes = Files.readAllLines(Path.of(GestionnaireLivraisons.fichierLivreurs), StandardCharsets.UTF_8);

            for (String ligne : lignes) {
                ligne = ligne.trim();
                if (ligne.charAt(0) != '#') {   //  ignorer les commentaires.
                    Arguments args = new Arguments(new Evenement(null, null, ligne));
                    int idLivreur = Integer.parseInt(args.extraireArgumentSuivant());
                    String typeLivreur = args.extraireArgumentSuivant().toUpperCase();
                    String nomLivreur = args.lire();
                    Livreur livreur;

                    // Créer le livreur avec le constructeur approprié
                    switch (typeLivreur) {
                        case "VELO":
                            // TODO : À compléter/modifier
                            livreur = null;
                            break;
                        case "CAMION":
                            // TODO : À compléter/modifier
                            livreur = null;
                            break;
                        case "VOITURE":
                            // TODO : À compléter/modifier
                            livreur = null;
                            break;
                        default:
                            throw new IOException();
                    }
                    this.livreursEnregistres.ajouter(livreur);
                    System.out.println(ligne);
                }
            }
        } catch (IOException | ListeChaineeException e) {
            System.err.println("ERREUR dans la lecture du fichier de livreurs.");
            System.exit(-1);
        }
    }

    /**
     * Écrit le fichier des livreurs enregistrés.
     */
    private void ecrireFichierLivreurs() {
        try {
            StringBuilder contenu = new StringBuilder();

            for (Livreur Livreur : this.livreursEnregistres.toArray()) {
                contenu.insert(0, Livreur + "\n");
            }
            contenu.insert(0, "#  structure <id livreur> <type livreur> <nom livreur>\n");

            Files.writeString(Path.of(GestionnaireLivraisons.fichierLivreurs), contenu.toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("ERREUR dans l'écriture du fichier de livreurs.");
            System.exit(-1);
        }
    }

    /**
     * Lance la sauvegarde des livreurs enregistrés avant de quitter le serveur.
     *
     */
    public void quitter() {
        this.ecrireFichierLivreurs();
    }

    /**
     * Affiche l'ensemble des livraisons à effectuer.
     *
     */
    public void afficherLivraisonsAEffectuer() {
        System.out.println("Liste des livraisons à effectuer :");
        System.out.println("(id, lot, priorité, tentative)");
        this.livraisonsAEffectuer.afficher();
    }

    /**
     * Affiche des statistiques concernant les livraisons.
     *
     */
    public void afficherStatistiques() {
        // TODO : À compléter/modifier
    }


    /**
     * Applique la commande EXIT envoyée par un client.
     *
     * @param evenement L'évènement reçu.
     * @return La chaîne à renvoyer au client.
     */
    private String traiterEXIT(Evenement evenement) {
        // TODO : À compléter/modifier
        return "";
    }

    /**
     * Applique la commande ID envoyée par un client.
     *
     * @param evenement L'évènement reçu.
     * @return La chaîne à renvoyer au client.
     */
    private String traiterID(Evenement evenement) {
        // TODO : À compléter/modifier
        return "";
    }

    /**
     * Applique la commande REGISTER envoyée par le client.
     *
     * @param evenement L'évènement reçu.
     * @return La chaîne à renvoyer au client.
     */
    private String traiterREGISTER(Evenement evenement) {
        // TODO : À compléter/modifier
        return "";
    }

    /**
     * Applique la commande GET envoyée par un client.
     *
     * @param evenement L'évènement reçu.
     * @return La chaîne à renvoyer au client.
     */
    private String traiterGET(Evenement evenement) {
        // TODO : À compléter/modifier
        return "";
    }

    /**
     * Applique la commande DELIVERED envoyée par un client.
     *
     * @param evenement L'évènement reçu.
     * @return La chaîne à renvoyer au client.
     */
    private String traiterDELIVERED(Evenement evenement) {
        // TODO : À compléter/modifier
        return "";
    }

    /**
     * Applique la commande FAILED envoyée par un client.
     *
     * @param evenement L'évènement reçu.
     * @return La chaîne à renvoyer au client.
     */
    private String traiterFAILED(Evenement evenement) {
        // TODO : À compléter/modifier
        return "";
    }

    /**
     * Calcule et retourne le revenu produit par un livreur.
     *
     * @param evenement L'évènement reçu.
     */
    private String traiterINCOME(Evenement evenement) {
        // TODO : À compléter/modifier
        return "";
    }

    /**
     * Retourne toutes les livraisons en cours pour le livreur concerné.
     *
     * @param evenement Événement de type INFO à traiter.
     * @return La chaine constituant la réponse à retourner au client.
     */
    private String traiterINFO(Evenement evenement) {
        // TODO : À compléter/modifier
        return "";
    }

    /**
     * Le client envoie un message à un autre ou à d'autres livreurs.
     *
     * @param evenement Événement de type INFO à traiter.
     * @return La chaine constituant la réponse à retourner au client.
     */
    private String traiterSEND(Evenement evenement) {
        // TODO : À compléter/modifier
        return "";
    }

    /**
     * Renvoie un message d'erreur au client pour cause d'évènement inconnu.
     *
     * @return La chaîne à renvoyer au client.
     */
    private String traiterCOMMAND_ERROR() {
        String reponse;

        reponse = "COMMAND_ERROR";

        return reponse;
    }


    /**
     * Gère un évènement reçu en paramètre.
     *
     * @param evenement L'événement a géré.
     */
    @Override
    public void traiter(Evenement evenement) {
        Object source = evenement.getSource();

        if (source instanceof Connexion) {
            Connexion cnx = (Connexion) source;
            System.out.println("GEST. LIV. a reçu : " + evenement.getType() + " " + evenement.getArgument());

            String reponse = "";
            switch (evenement.getType().toUpperCase()) {
                case "EXIT": // Le client se déconnecte.
                    reponse = this.traiterEXIT(evenement);
                    break;
                case "REGISTER": // Le client s'enregistre comme nouveau livreur.
                    reponse = this.traiterREGISTER(evenement);
                    break;
                case "ID": // Le client s'identifie.
                    reponse = this.traiterID(evenement);
                    break;
                case "GET": //  Le client a demandé des livraisons à effectuer.
                    reponse = this.traiterGET(evenement);
                    break;
                case "DELIVERED": // Le client informe qu'une livraison a été effectuée.
                    reponse = this.traiterDELIVERED(evenement);
                    break;
                case "FAILED": // Le client informe qu'une livraison n'a pas pu être effectuée.
                    reponse = this.traiterFAILED(evenement);
                    break;
                case "INCOME":
                    reponse = this.traiterINCOME(evenement);
                    break;
                case "INFO":
                    reponse = this.traiterINFO(evenement);
                    break;
                case "SEND":
                    reponse = this.traiterSEND(evenement);
                    break;
                default: // La commande envoyée par le client n'a pas été reconnue.
                    this.traiterCOMMAND_ERROR();
            }

            cnx.envoyer(reponse);
        }
    }
}
