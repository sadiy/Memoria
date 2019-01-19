package utils;

public enum Donnees {
    IDENTIFIANT("invité"),
    SCORE("0"),
    NIVEAU_ATTEINT("0"),
    MOTDEPASSE("");

    private String valeur;

    private Donnees(String valeur) {
        this.valeur = valeur ;
    }

    public String valeurDefaut() {
        return  this.valeur ;
    }
}
