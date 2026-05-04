package com.pao.laboratory10.exercise1;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LinkedList<Tranzactie> coada = new LinkedList<>();

        while (sc.hasNextLine()) {
            String linie = sc.nextLine().trim();
            if (linie.isEmpty()) continue;
            String[] parts = linie.split(" ");
            String cmd = parts[0];

            switch (cmd) {
                case "ENQUEUE" -> {
                    Tranzactie t = parseTranzactie(parts);
                    coada.addLast(t);
                }
                case "PUSH" -> {
                    Tranzactie t = parseTranzactie(parts);
                    coada.addFirst(t);
                }
                case "DEQUEUE" -> {
                    if (coada.isEmpty()) System.out.println("Coada goala.");
                    else System.out.println("Procesat: " + coada.removeFirst());
                }
                case "POP" -> {
                    if (coada.isEmpty()) System.out.println("Coada goala.");
                    else System.out.println("Extras: " + coada.removeFirst());
                }
                case "PRINT" -> {
                    for (Tranzactie t : coada) System.out.println(t);
                }
                case "SIZE" -> {
                    System.out.println("Dimensiune coada: " + coada.size());
                }
                case "REMOVE_DEBIT" -> {
                    int n = 0;
                    Iterator<Tranzactie> itr = coada.iterator();
                    while (itr.hasNext()) {
                        if (itr.next().getTip() == TipTranzactie.DEBIT) {
                            itr.remove();
                            n++;
                        }
                    }
                    System.out.println("Eliminat " + n + " tranzactii DEBIT.");
                }
                case "REMOVE_BELOW" -> {
                    double threshold = Double.parseDouble(parts[1]);
                    int n = 0;
                    Iterator<Tranzactie> itr = coada.iterator();
                    while (itr.hasNext()) {
                        if (itr.next().getSuma() < threshold) {
                            itr.remove();
                            n++;
                        }
                    }
                    System.out.printf("Eliminat %d tranzactii sub %.2f RON.%n", n, threshold);
                }
            }
        }
    }

    private static Tranzactie parseTranzactie(String[] parts) {
        int id = Integer.parseInt(parts[1]);
        double suma = Double.parseDouble(parts[2]);
        String data = parts[3];
        TipTranzactie tip = TipTranzactie.valueOf(parts[4]);
        return new Tranzactie(id, suma, data, tip);
    }
}