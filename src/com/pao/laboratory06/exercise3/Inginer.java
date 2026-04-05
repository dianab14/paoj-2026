package com.pao.laboratory06.exercise3;

public class Inginer extends Angajat implements PlataOnline, Comparable<Inginer> {
    private double sold;
    private boolean autentificat = false;

    public Inginer(String nume, String prenume, String telefon, double salariu, double sold) {
        super(nume, prenume, telefon, salariu);
        this.sold = sold;
    }

    @Override
    public void autentificare(String user, String parola) {
        if (user == null || user.isBlank() || parola == null || parola.isBlank())
            throw new IllegalArgumentException("User și parola nu pot fi null/goale!");
        autentificat = true;
        System.out.println("Inginer " + nume + " autentificat cu succes.");
    }

    @Override
    public double consultareSold() {
        return sold;
    }

    @Override
    public boolean efectuarePlata(double suma) {
        if (suma <= 0) return false;
        if (sold >= suma) {
            sold -= suma;
            System.out.println("Plată de " + suma + " lei efectuată. Sold rămas: " + sold);
            return true;
        }
        System.out.println("Fonduri insuficiente.");
        return false;
    }

    @Override
    public int compareTo(Inginer other) {
        return this.nume.compareTo(other.nume);
    }

    @Override
    public String toString() {
        return String.format("Inginer{nume='%s', salariu=%.2f, sold=%.2f}", nume, salariu, sold);
    }
}