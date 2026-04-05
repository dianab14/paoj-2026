package com.pao.laboratory06.exercise3;

import java.util.ArrayList;
import java.util.List;

public class PersoanaJuridica extends Persoana implements PlataOnlineSMS {
    private double sold;
    private List<String> smsTrimise = new ArrayList<>();

    public PersoanaJuridica(String nume, String prenume, String telefon, double sold) {
        super(nume, prenume, telefon);
        this.sold = sold;
    }

    @Override
    public void autentificare(String user, String parola) {
        if (user == null || user.isBlank() || parola == null || parola.isBlank())
            throw new IllegalArgumentException("User și parola nu pot fi null/goale!");
        System.out.println("PersoanaJuridica " + nume + " autentificată.");
    }

    @Override
    public double consultareSold() { return sold; }

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
    public boolean trimiteSMS(String mesaj) {
        if (mesaj == null || mesaj.isBlank()) return false;
        if (telefon == null || telefon.isBlank()) {
            System.out.println("SMS eșuat: nu există număr de telefon valid.");
            return false;
        }
        smsTrimise.add(mesaj);
        System.out.println("SMS trimis către " + telefon + ": " + mesaj);
        return true;
    }

    public List<String> getSmsTrimise() { return smsTrimise; }

    @Override
    public String toString() {
        return String.format("PersoanaJuridica{nume='%s', telefon='%s', sold=%.2f}", nume, telefon, sold);
    }
}