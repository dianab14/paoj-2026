package com.pao.laboratory10.exercise2;

import com.pao.laboratory10.exercise1.TipTranzactie;
import com.pao.laboratory10.exercise1.Tranzactie;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 1. Citește N tranzacții (cu duplicate)
        int n = Integer.parseInt(sc.nextLine().trim());
        List<Tranzactie> lista = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String[] parts = sc.nextLine().trim().split(" ");
            int id = Integer.parseInt(parts[0]);
            double suma = Double.parseDouble(parts[1]);
            String data = parts[2];
            TipTranzactie tip = TipTranzactie.valueOf(parts[3]);
            lista.add(new Tranzactie(id, suma, data, tip));
        }

        // 2. Procesează comenzi
        while (sc.hasNextLine()) {
            String linie = sc.nextLine().trim();
            if (linie.isEmpty()) continue;
            String[] parts = linie.split(" ");

            switch (parts[0]) {
                case "UNIQUE_IDS" -> {
                    LinkedHashSet<Integer> ids = new LinkedHashSet<>();
                    for (Tranzactie t : lista) ids.add(t.getId());
                    System.out.println("IDs unice (" + ids.size() + "): " + ids);
                }

                case "MONTHLY_REPORT" -> {
                    TreeMap<String, double[]> raport = new TreeMap<>();
                    for (Tranzactie t : lista) {
                        String luna = t.getData().substring(0, 7);
                        raport.putIfAbsent(luna, new double[]{0.0, 0.0});
                        if (t.getTip() == TipTranzactie.CREDIT) raport.get(luna)[0] += t.getSuma();
                        else raport.get(luna)[1] += t.getSuma();
                    }
                    for (Map.Entry<String, double[]> e : raport.entrySet()) {
                        System.out.printf("%s: CREDIT %.2f RON, DEBIT %.2f RON%n",
                                e.getKey(), e.getValue()[0], e.getValue()[1]);
                    }
                }

                case "TOP" -> {
                    int topN = Integer.parseInt(parts[1]);
                    List<Tranzactie> copie = new ArrayList<>(lista);
                    copie.sort(Comparator.comparingDouble(Tranzactie::getSuma).reversed());
                    System.out.println("Top " + topN + ":");
                    copie.subList(0, Math.min(topN, copie.size())).forEach(System.out::println);
                }

                case "SORT_ASC" -> {
                    lista.sort(Comparator.comparingDouble(Tranzactie::getSuma));
                    lista.forEach(System.out::println);
                }

                case "SORT_DESC" -> {
                    lista.sort(Comparator.comparingDouble(Tranzactie::getSuma).reversed());
                    lista.forEach(System.out::println);
                }

                case "REVERSE" -> {
                    Collections.reverse(lista);
                    lista.forEach(System.out::println);
                }

                case "MIN_MAX" -> {
                    Comparator<Tranzactie> cmp = Comparator.comparingDouble(Tranzactie::getSuma);
                    System.out.println("MIN: " + Collections.min(lista, cmp));
                    System.out.println("MAX: " + Collections.max(lista, cmp));
                }

                case "CME_DEMO" -> {
                    try {
                        for (Tranzactie t : lista) lista.remove(t);
                    } catch (ConcurrentModificationException e) {
                        System.out.println("ConcurrentModificationException prins: modificare in iteratie detectata.");
                    }
                }
            }
        }
    }
}