package com.pao.project.cabinet.model;

public class Medic extends Persoana {
    private final String codParafa;
    private String specialitate;
    private int aniExperienta;

    public Medic(String nume, String prenume, String email, String telefon,
                 String codParafa, String specialitate, int aniExperienta) {
        super(nume, prenume, email, telefon);
        this.codParafa = codParafa;
        this.specialitate = specialitate;
        this.aniExperienta = aniExperienta;
    }

    @Override
    public String getRol() { return "Medic"; }

    public String getCodParafa() { return codParafa; }
    public String getSpecialitate() { return specialitate; }
    public int getAniExperienta() { return aniExperienta; }

    public void setSpecialitate(String specialitate) { this.specialitate = specialitate; }
    public void setAniExperienta(int aniExperienta) { this.aniExperienta = aniExperienta; }

    @Override
    public String toString() {
        return super.toString() + " | Parafa: " + codParafa
                + " | Specialitate: " + specialitate
                + " | Experienta: " + aniExperienta + " ani";
    }
}