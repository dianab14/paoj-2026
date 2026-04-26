package com.pao.project.cabinet.model;

import java.util.ArrayList;
import java.util.List;

public class Pacient extends Persoana implements Comparable<Pacient> {
    private final CNP cnp;
    private int varsta;
    private String adresa;
    private List<String> istoricMedical;

    public Pacient(String nume, String prenume, String email, String telefon, 
            CNP cnp, int varsta, String adresa) {
        super(nume, prenume, email, telefon);
        this.cnp = cnp;
        this.varsta = varsta;
        this.adresa = adresa;
        this.istoricMedical = new ArrayList<>();
    }

    @Override
    public String getRol() { return "Pacient"; }

    public CNP getCnp() { return cnp; }
    public int getVarsta() { return varsta; }
    public String getAdresa() { return adresa; }
    public List<String> getIstoricMedical() { return istoricMedical; }

    public void setVarsta(int varsta) { this.varsta = varsta; }
    public void setAdresa(String adresa) { this.adresa = adresa; }

    public void adaugaInIstoricMedical(String intrare) {
        istoricMedical.add(intrare);
    }

    @Override
    public int compareTo(Pacient other) {
        return this.getNume().compareToIgnoreCase(other.getNume());
    }

    @Override
    public String toString() {
        return super.toString() + " | CNP: " + cnp + " | Varsta: " + varsta;
    }
}