package model; // <--- DÒNG NÀY RẤT QUAN TRỌNG (Dòng số 1)

public class Appartement { // <--- Phải có chữ "public"
    private int idAppartement;
    private String numero;
    private double surface;
    private String statut;
    private String description;

    // Constructor rỗng (Bắt buộc phải có)
    public Appartement() {}

    // Constructor đầy đủ
    public Appartement(int idAppartement, String numero, double surface, String statut, String description) {
        this.idAppartement = idAppartement;
        this.numero = numero;
        this.surface = surface;
        this.statut = statut;
        this.description = description;
    }

    // --- Getters ---
    public int getIdAppartement() { return idAppartement; }
    public String getNumero() { return numero; }
    public double getSurface() { return surface; }
    public String getStatut() { return statut; }
    public String getDescription() { return description; }

    // --- Setters ---
    public void setIdAppartement(int idAppartement) { this.idAppartement = idAppartement; }
    public void setNumero(String numero) { this.numero = numero; }
    public void setSurface(double surface) { this.surface = surface; }
    public void setStatut(String statut) { this.statut = statut; }
    public void setDescription(String description) { this.description = description; }
}