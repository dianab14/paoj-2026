package com.pao.laboratory07.exercise3;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        if (!sc.hasNextLine())
            return;
        int n = Integer.parseInt(sc.nextLine().trim());
        
        List<Comanda> comenzi = new ArrayList<>();

        for (int i=0; i<n; i++) {
            String linie = sc.nextLine();
            String[] cuvinte = linie.split(" ");
            String tipComanda = cuvinte[0];

            if (tipComanda.equals("STANDARD")) {
                String nume = cuvinte[1];
                double pret = Double.parseDouble(cuvinte[2]);
                String client = cuvinte[3];
                comenzi.add(new ComandaStandard(nume, pret, client));
            }
            else if (tipComanda.equals("DISCOUNTED")) {
                String nume = cuvinte[1];
                double pret = Double.parseDouble(cuvinte[2]);
                int discount = Integer.parseInt(cuvinte[3]);
                String client = cuvinte[4];
                comenzi.add(new ComandaRedusa(nume, pret, discount, client));
            }
            else if (tipComanda.equals("GIFT")) {
                String nume = cuvinte[1];
                String client = cuvinte[2];
                comenzi.add(new ComandaGratuita(nume, client));
            }
        }

        for (Comanda c : comenzi) {
            System.out.println(c.descriere());
        }

        while (sc.hasNextLine()) {
            String linieComanda = sc.nextLine().trim();
            if (linieComanda.isEmpty())
                continue;
            
            String[] tokens = linieComanda.split(" ");
            String actiune = tokens[0];

            if (actiune.equals("STATS")) {
                executaStats(comenzi);
            }
            else if (actiune.equals("FILTER")) {
                double prag = Double.parseDouble(tokens[1]);
                executaFilter(comenzi, prag);
            }
            else if (actiune.equals("SORT")) {
                executaSort(comenzi);
            }
            else if (actiune.equals("SPECIAL")) {
                executaSpecial(comenzi);
            }
            else if (actiune.equals("QUIT")) {
                break;
            }
        }
        sc.close();
    }

    private static void executaStats(List<Comanda> comenzi) {
        System.out.println("\n--- STATS ---");
        
        Map<String, Double> medii = comenzi.stream().collect(Collectors.groupingBy(new Function<Comanda, String>() {
                public String apply(Comanda c) {
                    if (c instanceof ComandaStandard) return "STANDARD";
                    if (c instanceof ComandaRedusa) return "DISCOUNTED";
                    return "GIFT";
                }
            },
            Collectors.averagingDouble(new ToDoubleFunction<Comanda>() {
                public double applyAsDouble(Comanda c) {
                    return c.pretFinal();
                }
            })
        ));

        String[] tipuri = {"STANDARD", "DISCOUNTED", "GIFT"};
        for (String t : tipuri) {
            double valoareMedie = medii.getOrDefault(t, 0.0);
            System.out.printf("%s: medie = %.2f lei\n", t, valoareMedie);
        }
    }

    private static void executaFilter(List<Comanda> comenzi, double prag) {
        System.out.printf("\n--- FILTER (>= %.2f) ---\n", prag);
        
        List<Comanda> filtrate = comenzi.stream().filter(new Predicate<Comanda>() {
            public boolean test(Comanda c) {
                return c.pretFinal() >= prag;
            }
        }).collect(Collectors.toList());

        for (Comanda c : filtrate) {
            String tip = c instanceof ComandaStandard ? "STANDARD" : (c instanceof ComandaRedusa ? "DISCOUNTED" : "GIFT");
            System.out.printf("%s: %s, pret: %.2f lei - client: %s\n", tip, c.nume, c.pretFinal(), c.client);
        }
    }

    private static void executaSort(List<Comanda> comenzi) {
        System.out.println("\n--- SORT (by client, then by pret) ---");
        
        List<Comanda> sortate = comenzi.stream().sorted(new Comparator<Comanda>() {
            public int compare(Comanda c1, Comanda c2) {
                int comparatieClient = c1.client.compareTo(c2.client);
                if (comparatieClient != 0) {
                    return comparatieClient;
                }
                return Double.compare(c1.pretFinal(), c2.pretFinal());
            }
        }).collect(Collectors.toList());

        for (Comanda c : sortate) {
            String tip = c instanceof ComandaStandard ? "STANDARD" : (c instanceof ComandaRedusa ? "DISCOUNTED" : "GIFT");
            String pretInfo = (c instanceof ComandaGratuita) ? "gratuit" : String.format("pret: %.2f lei", c.pretFinal());
            System.out.printf("%s: %s, %s - client: %s\n", tip, c.nume, pretInfo, c.client);
        }
    }

    private static void executaSpecial(List<Comanda> comenzi) {
        System.out.println("\n--- SPECIAL (discount > 15%) ---");
        
        List<Comanda> speciale = comenzi.stream().filter(new Predicate<Comanda>() {
            public boolean test(Comanda c) {
                if (c instanceof ComandaRedusa) {
                    ComandaRedusa cr = (ComandaRedusa) c;
                    return cr.getDiscount() > 15;
                }
                return false;
            }
        }).collect(Collectors.toList());

        for (Comanda c : speciale) {
            System.out.println(c.descriere());
        }
    }
}