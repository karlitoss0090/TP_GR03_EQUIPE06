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
                            livreur = new LivreurVelo(idLivreur, nomLivreur);

                            break;
                        case "CAMION":

                            livreur = new LivreurCamion(idLivreur, nomLivreur);

                            break;
                        case "VOITURE":
                            livreur = new LivreurVoiture(idLivreur, nomLivreur);

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
        Connexion connexion = (Connexion) evenement.getSource();
        Livreur livreur = this.livreursAuthentifies.get(connexion);

        if (livreur == null) {
            return "AUTHENTICATION_ERROR";
        }

        IListeLivraisons livraisonsARemettre = livreur.supprimerToutesLesLivraisons();
        this.livraisonsAEffectuer.ajouterTout(livraisonsARemettre);

        this.livreursAuthentifies.remove(connexion);

        return "OK";
    }

    /**
     * Applique la commande ID envoyée par un client.
     *
     * @param evenement L'évènement reçu.
     * @return La chaîne à renvoyer au client.
     */
    private String traiterID(Evenement evenement) {

        Connexion cnx = (Connexion) evenement.getSource();
        Arguments args = new Arguments(evenement);

        int idLivreur = Integer.parseInt(args.extraireArgumentSuivant());

        Livreur livreur = this.livreursEnregistres.rechercher(idLivreur);

        if (livreur == null) {
            return "Erreur";
        }

        if (this.livreursAuthentifies.containsKey(cnx) ||
                this.livreursAuthentifies.containsValue(livreur)) {
            return "Erreur";
        }

        this.livreursAuthentifies.put(cnx, livreur);

        return "OK";
    }

    /**
     * Applique la commande REGISTER envoyée par le client.
     *
     * @param evenement L'évènement reçu.
     * @return La chaîne à renvoyer au client.
     */
    private String traiterREGISTER(Evenement evenement) {

        Arguments args = new Arguments(evenement);

        int idLivreur;
        String typeLivreur;
        String nomLivreur;

        try {
            idLivreur = Integer.parseInt(args.extraireArgumentSuivant());
            typeLivreur = args.extraireArgumentSuivant().toUpperCase();
            nomLivreur = args.lire().trim();
        } catch (Exception e) {
            return "BAD_ARGUMENT_ERROR";
        }

        if (nomLivreur.isEmpty()) {
            return "BAD_ARGUMENT_ERROR";
        }

        if (!typeLivreur.equals("VELO") &&
                !typeLivreur.equals("CAMION") &&
                !typeLivreur.equals("VOITURE")) {
            return "BAD_ARGUMENT_ERROR";
        }

        if (this.livreursEnregistres.rechercher(idLivreur) != null) {
            return "ID_ALREADY_USED_ERROR";
        }

        Livreur livreur;

        switch (typeLivreur) {
            case "VELO":
                livreur = new LivreurVelo(idLivreur, nomLivreur);
                break;

            case "CAMION":
                livreur = new LivreurCamion(idLivreur, nomLivreur);
                break;

            case "VOITURE":
                livreur = new LivreurVoiture(idLivreur, nomLivreur);
                break;

            default:
                return "BAD_ARGUMENT_ERROR";
        }

        try {
            this.livreursEnregistres.ajouter(livreur);
        } catch (ListeChaineeException e) {
            return "BAD_ARGUMENT_ERROR";
        }

        return "REGISTERED";
    }

    /**
     * Applique la commande GET envoyée par un client.
     *
     * @param evenement L'évènement reçu.
     * @return La chaîne à renvoyer au client.
     */
    private String traiterGET(Evenement evenement) {
        Connexion connexion = (Connexion) evenement.getSource();
        Livreur livreur = this.livreursAuthentifies.get(connexion);

        if (livreur == null) {
            return "AUTHENTICATION_ERROR";
        }

        int nombreDejaEnCours = livreur.nbLivraisonsEnCours();

        int nombreAAtribuer = livreur.capaciteLivraison() - nombreDejaEnCours;

        if (nombreAAtribuer <= 0 || this.livraisonsAEffectuer.estVide()) {
            return "EMPTY";
        }

        String reponse = "DELIVERIES";
        int nombreAttribue = 0;

        for (int i = 0; i < nombreAAtribuer && !this.livraisonsAEffectuer.estVide(); i++) {
            Livraison livraison = this.livraisonsAEffectuer.retirer();

            livreur.ajouterLivraisonEnCours(livraison);
            livraison.setStatut(Statut.EN_COURS);

            reponse += " " + livraison.getId()
                    + " " + livraison.getLot()
                    + " " + livraison.getPriorite()
                    + " " + livraison.getTentative();

            nombreAttribue++;
        }

        reponse = "DELIVERIES " + nombreAttribue + reponse.substring(10);

        return reponse;
    }

    /**
     * Applique la commande DELIVERED envoyée par un client.
     *
     * @param evenement L'évènement reçu.
     * @return La chaîne à renvoyer au client.
     */
    private String traiterDELIVERED(Evenement evenement) {
        Connexion connexion = (Connexion) evenement.getSource();
        Livreur livreur = this.livreursAuthentifies.get(connexion);

        if (livreur == null) {
            return "AUTHENTICATION_ERROR";
        }

        int idLivraison;

        try {
            idLivraison = Integer.parseInt(evenement.getArgument().trim());
        } catch (Exception e) {
            return "BAD_ARGUMENT_ERROR";
        }

        Livraison livraison = livreur.supprimerLivraisonEnCours(idLivraison);

        if (livraison == null) {
            return "BAD_DELIVERY_ERROR";
        }

        livraison.setStatut(Statut.LIVREE);
        livreur.ajouterLivraisonEffectuee(livraison);

        return "OK";
    }

    /**
     * Applique la commande FAILED envoyée par un client.
     *
     * @param evenement L'évènement reçu.
     * @return La chaîne à renvoyer au client.
     */
    private String traiterFAILED(Evenement evenement) {
        Connexion connexion = (Connexion) evenement.getSource();
        Livreur livreur = this.livreursAuthentifies.get(connexion);

        if (livreur == null) {
            return "AUTHENTICATION_ERROR";
        }

        int idLivraison;

        try {
            idLivraison = Integer.parseInt(evenement.getArgument().trim());
        } catch (Exception e) {
            return "BAD_ARGUMENT_ERROR";
        }

        Livraison livraison = livreur.supprimerLivraisonEnCours(idLivraison);

        if (livraison == null) {
            return "BAD_DELIVERY_ERROR";
        }

        if (livraison.resteTentatives()) {
            livraison.nouvelleTentative();
            livraison.setStatut(Statut.EN_ATTENTE);
            this.livraisonsAEffectuer.ajouter(livraison);
        } else {
            livraison.setStatut(Statut.ECHOUEE);
            this.livraisonsEchouees.ajouter(livraison);
        }
        return "OK";
    }

    /**
     * Calcule et retourne le revenu produit par un livreur.
     *
     * @param evenement L'évènement reçu.
     */
    private String traiterINCOME(Evenement evenement) {
        Connexion connexion = (Connexion) evenement.getSource();
        Livreur livreur = this.livreursAuthentifies.get(connexion);

        // vérifier authentification
        if (livreur == null) {
            return "AUTHENTICATION_ERROR";
        }

        double revenu = livreur.calculerRevenu();

        return "INCOME " + revenu;
    }

    /**
     * Retourne toutes les livraisons en cours pour le livreur concerné.
     *
     * @param evenement Événement de type INFO à traiter.
     * @return La chaine constituant la réponse à retourner au client.
     */
    private String traiterINFO(Evenement evenement) {
        Connexion connexion = (Connexion) evenement.getSource();
        Livreur livreur = this.livreursAuthentifies.get(connexion);

        if (livreur == null) {
            return "AUTHENTICATION_ERROR";
        }

        String argument = evenement.getArgument();


        if (argument == null || argument.trim().isEmpty()) {
            Iterator<Livraison> iterateur = livreur.donneIterateurLivraisonsEnCours();

            if (!iterateur.hasNext()) {
                return "NO_DELIVERY_ERROR";
            }

            String reponse = "DELIVERIES_INFO";
            int nombreLivraisons = 0;

            while (iterateur.hasNext()) {
                Livraison livraison = iterateur.next();

                reponse += " " + livraison.getId()
                        + " " + livraison.getLot()
                        + " " + livraison.getPriorite()
                        + " " + livraison.getTentative();

                nombreLivraisons++;
            }

            reponse = "DELIVERIES_INFO " + nombreLivraisons + reponse.substring("DELIVERIES_INFO".length());
            return reponse;
        }


        int idLivraison;

        try {
            idLivraison = Integer.parseInt(argument.trim());
        } catch (NumberFormatException e) {
            return "BAD_ARGUMENT_ERROR";
        }

        Livraison livraison = livreur.rechercherLivraisonEnCours(idLivraison);

        if (livraison == null) {
            return "BAD_DELIVERY_ERROR";
        }

        return "DELIVERIES_INFO 1 "
                + livraison.getId() + " "
                + livraison.getLot() + " "
                + livraison.getPriorite() + " "
                + livraison.getTentative();
    }

    /**
     * Le client envoie un message à un autre ou à d'autres livreurs.
     *
     * @param evenement Événement de type INFO à traiter.
     * @return La chaine constituant la réponse à retourner au client.
     */
private String traiterSEND(Evenement evenement) {
    Connexion connexionExpediteur = (Connexion) evenement.getSource();
    Livreur expediteur = this.livreursAuthentifies.get(connexionExpediteur);

    if (expediteur == null) {
        return "AUTHENTICATION_ERROR";
    }

    Arguments args = new Arguments(evenement);

    String idMessageStr;
    String destinataireStr;
    String message;

    try {
        idMessageStr = args.extraireArgumentSuivant();
        destinataireStr = args.extraireArgumentSuivant();
        message = args.lire().trim();
    } catch (Exception e) {
        return "BAD_ARGUMENT_ERROR";
    }

    if (idMessageStr == null || idMessageStr.trim().isEmpty()
            || destinataireStr == null || destinataireStr.trim().isEmpty()
            || message.isEmpty()) {
        return "BAD_ARGUMENT_ERROR";
    }

    // doublon : déjà reçu, on n'envoie rien de nouveau, mais on ACK quand même
    if (this.messagesId.contains(idMessageStr)) {
        return "ACK " + idMessageStr;
    }

    this.messagesId.add(idMessageStr);

    // diffusion
    if (destinataireStr.equals("*")) {
        for (Map.Entry<Connexion, Livreur> entree : this.livreursAuthentifies.entrySet()) {
            Connexion cnxDest = entree.getKey();
            Livreur livreurDest = entree.getValue();

            if (livreurDest.getId() != expediteur.getId()) {
                cnxDest.envoyer("MSG " + expediteur.getId() + " " + message);
            }
        }

        return "ACK " + idMessageStr;
    }

    // destinataire précis
    int idDestinataire;
    try {
        idDestinataire = Integer.parseInt(destinataireStr);
    } catch (NumberFormatException e) {
        return "BAD_ARGUMENT_ERROR";
    }

    Connexion connexionDestinataire = this.retrouverConnexionLivreurAuthentifie(idDestinataire);

    if (connexionDestinataire == null) {
        return "AUTHENTICATION_ERROR";
    }

    connexionDestinataire.envoyer("MSG " + expediteur.getId() + " " + message);

    return "ACK " + idMessageStr;
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
    private Connexion retrouverConnexionLivreurAuthentifie(int idLivreur) {
    for (Map.Entry<Connexion, Livreur> entree : this.livreursAuthentifies.entrySet()) {
        if (entree.getValue().getId() == idLivreur) {
            return entree.getKey();
        }
    }
    return null;
    }
    
}
