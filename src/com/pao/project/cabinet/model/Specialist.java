package com.pao.project.cabinet.model;

public class Specialist extends Medic {
    private String titluAcademic;
    private String spitalAfiliat;

    public Specialist(String nume, String prenume, String email, String telefon,
                      String codParafa, String specialitate, int aniExperienta,
                      String titluAcademic, String spitalAfiliat) {
        super(nume, prenume, email, telefon, codParafa, specialitate, aniExperienta);
        this.titluAcademic = titluAcademic;
        this.spitalAfiliat = spitalAfiliat;
    }

    @Override
    public String getRol() { return "Medic Specialist"; }

    public String getTitluAcademic() { return titluAcademic; }
    public String getSpitalAfiliat() { return spitalAfiliat; }

    public void setTitluAcademic(String titluAcademic) { this.titluAcademic = titluAcademic; }
    public void setSpitalAfiliat(String spitalAfiliat) { this.spitalAfiliat = spitalAfiliat; }

    @Override
    public String toString() {
        return super.toString() + " | Titlu: " + titluAcademic + " | Spital: " + spitalAfiliat;
    }
}