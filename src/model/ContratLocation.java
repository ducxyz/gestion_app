package model;

import java.sql.Date;

public class ContratLocation {
    private int idContrat;
    private Date dateDebut;
    private Date dateFin;
    private double loyerMensuel;
    private String statut;
    private int idAppartement;
    private int idResident;

    // Các trường phụ để hiển thị tên (không lưu trực tiếp trong bảng Contrat)
    private String numeroAppartement;
    private String nomResident;

    public ContratLocation() {}

    // Constructor đầy đủ
    public ContratLocation(int idContrat, Date dateDebut, Date dateFin, double loyerMensuel, String statut, int idAppartement, int idResident, String numeroAppartement, String nomResident) {
        this.idContrat = idContrat;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.loyerMensuel = loyerMensuel;
        this.statut = statut;
        this.idAppartement = idAppartement;
        this.idResident = idResident;
        this.numeroAppartement = numeroAppartement;
        this.nomResident = nomResident;
    }

    // Getters
    public int getIdContrat() { return idContrat; }
    public Date getDateDebut() { return dateDebut; }
    public Date getDateFin() { return dateFin; }
    public double getLoyerMensuel() { return loyerMensuel; }
    public String getStatut() { return statut; }
    public int getIdAppartement() { return idAppartement; }
    public int getIdResident() { return idResident; }
    public String getNumeroAppartement() { return numeroAppartement; }
    public String getNomResident() { return nomResident; }

    // Setters
    public void setIdContrat(int idContrat) { this.idContrat = idContrat; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }
    public void setLoyerMensuel(double loyerMensuel) { this.loyerMensuel = loyerMensuel; }
    public void setStatut(String statut) { this.statut = statut; }
    public void setIdAppartement(int idAppartement) { this.idAppartement = idAppartement; }
    public void setIdResident(int idResident) { this.idResident = idResident; }
    public void setNumeroAppartement(String numeroAppartement) { this.numeroAppartement = numeroAppartement; }
    public void setNomResident(String nomResident) { this.nomResident = nomResident; }
}
