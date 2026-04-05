package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public abstract class Colaborator implements IOperatiiCitireScriere, Comparable<Colaborator> {
    protected String nume;
    protected String prenume;
    protected double venitBrutLunar;

    public abstract double calculeazaVenitNetAnual();
    public abstract TipColaborator getTip();

    @Override
    public int compareTo(Colaborator other) {
        return Double.compare(other.calculeazaVenitNetAnual(), this.calculeazaVenitNetAnual()); // descrescător
    }

    @Override
    public void afiseaza() {
        System.out.printf("%s: %s %s, venit net anual: %.2f lei%n",
                tipContract(), nume, prenume, calculeazaVenitNetAnual());
    }
}