package model;

import java.sql.Date;

public class Resident {
    private int idResident;
    private String nom;
    private String prenom;
    private String numeroIdentite;
    private String telephone;
    private Date dateNaissance;
    private int idCompte; // Có thể là 0 nếu không liên kết tài khoản

    public Resident() {
    }

    public Resident(int idResident, String nom, String prenom, String numeroIdentite, String telephone, Date dateNaissance, int idCompte) {
        this.idResident = idResident;
        this.nom = nom;
        this.prenom = prenom;
        this.numeroIdentite = numeroIdentite;
        this.telephone = telephone;
        this.dateNaissance = dateNaissance;
        this.idCompte = idCompte;
    }

    // Getters và Setters
    public int getIdResident() { return idResident; }
    public void setIdResident(int idResident) { this.idResident = idResident; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getNumeroIdentite() { return numeroIdentite; }
    public void setNumeroIdentite(String numeroIdentite) { this.numeroIdentite = numeroIdentite; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public Date getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(Date dateNaissance) { this.dateNaissance = dateNaissance; }

    public int getIdCompte() { return idCompte; }
    public void setIdCompte(int idCompte) { this.idCompte = idCompte; }
}