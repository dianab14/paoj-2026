package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class CIMColaborator extends PersoanaFizica {
    private boolean bonus;

    @Override
    public void citeste(Scanner in) {
        nume = in.next();
        prenume = in.next();
        venitBrutLunar = in.nextDouble();
        String rest = in.nextLine().trim();
        bonus = rest.equals("DA");
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double net = venitBrutLunar * 12 * 0.55;
        if (bonus) net *= 1.10;
        return net;
    }

    @Override
    public boolean areBonus() { return bonus; }

    @Override
    public String tipContract() { return "CIM"; }

    @Override
    public TipColaborator getTip() { return TipColaborator.CIM; }
}