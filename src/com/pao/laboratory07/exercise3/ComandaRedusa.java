package com.pao.laboratory07.exercise3;

public final class ComandaRedusa extends Comanda {
    private final double pret;
    private final int discount;

    public ComandaRedusa(String nume, double pret, int discount, String client) {
        super(nume, client);
        this.pret = pret;
        this.discount = discount;
    }

    public int getDiscount() { return discount; }

    @Override
    public double pretFinal() { return pret * (1 - discount / 100.0); }

    @Override
    public String descriere() {
        return String.format("DISCOUNTED: %s, pret: %.2f lei (-%d%%) [%s] - client: %s", nume, pretFinal(), discount, stare, client);
    }
}