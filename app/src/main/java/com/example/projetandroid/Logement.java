package com.example.projetandroid;



public class Logement {

    private String id;
    private String nomLogement;
    private String adresse;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNomLogement(String nomLogement) {
        this.nomLogement = nomLogement;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setSuperficie(String superficie) {
        this.superficie = superficie;
    }

    public void setNbPieces(String nbPieces) {
        this.nbPieces = nbPieces;
    }

    public void setParking(Boolean parking) {
        this.parking = parking;
    }

    private String superficie;
    private String nbPieces;
    private Boolean parking;

    public Logement() {} // Obligatoire Firestore

    public String getNomLogement() { return nomLogement; }
    public String getAdresse() { return adresse; }
    public String getSuperficie() { return superficie; }
    public String getNbPieces() { return nbPieces; }
    public Boolean getParking() { return parking; }
}
