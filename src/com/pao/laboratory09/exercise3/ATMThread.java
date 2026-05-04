package com.pao.laboratory09.exercise3;

import java.util.Random;

public class ATMThread extends Thread {
    private final int atmId;
    private final CoadaTranzactii coada;
    private static int contor = 1; // id global pentru tranzacții
    private static final Object lock = new Object();

    public ATMThread(int atmId, CoadaTranzactii coada) {
        this.atmId = atmId;
        this.coada = coada;
    }

    @Override
    public void run() {
        Random rand = new Random();
        for (int i = 0; i < 4; i++) {
            int id;
            synchronized (lock) {
                id = contor++;
            }
            double suma = Math.round(rand.nextDouble() * 1000 * 100.0) / 100.0;
            Tranzactie t = new Tranzactie(id, suma, "2024-01-15");

            try {
                System.out.printf("[ATM-%d] trimite: Tranzactie #%d %.2f RON%n",
                        atmId, t.getId(), t.getSuma());
                coada.adauga(t);
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}