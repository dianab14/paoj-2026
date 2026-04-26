package com.pao.project.cabinet.model;

public class Medicament {
    private String denumire;
    private String dozaj;
    private String producator;

    public Medicament(String denumire, String dozaj, String producator) {
        this.denumire = denumire;
        this.dozaj = dozaj;
        this.producator = producator;
    }

    public String getDenumire() { return denumire; }
    public String getDozaj() { return dozaj; }
    public String getProducator() { return producator; }

    public void setDozaj(String dozaj) { this.dozaj = dozaj; }

    @Override
    public String toString() {
        return denumire + " [" + dozaj + "] - " + producator;
    }
}