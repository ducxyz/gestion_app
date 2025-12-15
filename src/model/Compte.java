package model;

public class Compte {
    private int idCompte;
    private String nomUtilisateur;
    private String motDePasseHash;
    private String role; // "ADMIN" hoặc "USER"
    private int statut;

    public Compte() {}

    public Compte(int idCompte, String nomUtilisateur, String motDePasseHash, String role, int statut) {
        this.idCompte = idCompte;
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasseHash = motDePasseHash;
        this.role = role;
        this.statut = statut;
    }

    // Getters
    public int getIdCompte() { return idCompte; }
    public String getNomUtilisateur() { return nomUtilisateur; }
    public String getMotDePasseHash() { return motDePasseHash; }
    public String getRole() { return role; }
    public int getStatut() { return statut; }

    // Setters
    public void setIdCompte(int idCompte) { this.idCompte = idCompte; }
    public void setNomUtilisateur(String nomUtilisateur) { this.nomUtilisateur = nomUtilisateur; }
    public void setMotDePasseHash(String motDePasseHash) { this.motDePasseHash = motDePasseHash; }
    public void setRole(String role) { this.role = role; }
    public void setStatut(int statut) { this.statut = statut; }
}