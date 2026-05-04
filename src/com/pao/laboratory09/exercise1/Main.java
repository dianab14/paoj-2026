package com.pao.laboratory09.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex1.ser";

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        // 1. Citește N tranzacții
        int n = Integer.parseInt(sc.nextLine().trim());
        List<Tranzactie> tranzactii = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String[] parts = sc.nextLine().trim().split(" ");
            int id = Integer.parseInt(parts[0]);
            double suma = Double.parseDouble(parts[1]);
            String data = parts[2];
            String contSursa = parts[3];
            String contDestinatie = parts[4];
            TipTranzactie tip = TipTranzactie.valueOf(parts[5]);

            Tranzactie t = new Tranzactie(id, suma, data, contSursa, contDestinatie, tip);
            // 2. Setează note înainte de serializare
            t.setNote("procesat");
            tranzactii.add(t);
        }

        // 3. Serializare
        new File("output").mkdirs();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            oos.writeObject(tranzactii);
        }

        // 4. Deserializare
        List<Tranzactie> deserializate;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(OUTPUT_FILE))) {
            deserializate = (List<Tranzactie>) ois.readObject();
        }

        // 5. Procesează comenzi până la EOF
        while (sc.hasNextLine()) {
            String linie = sc.nextLine().trim();
            if (linie.isEmpty()) continue;
            
            ///A
            if (linie.equals("LIST")) {
                for (Tranzactie t : deserializate) {
                    System.out.println(format(t));
                }

            ///B
            } else if (linie.startsWith("FILTER ")) {
                String prefix = linie.substring(7).trim();
                List<Tranzactie> filtrate = new ArrayList<>();
                for (Tranzactie t : deserializate) {
                    if (t.getData().startsWith(prefix)) {
                        filtrate.add(t);
                    }
                }
                if (filtrate.isEmpty()) {
                    System.out.println("Niciun rezultat.");
                } else {
                    for (Tranzactie t : filtrate) {
                        System.out.println(format(t));
                    }
                }
                ///C
            } else if (linie.startsWith("NOTE ")) {
                int id = Integer.parseInt(linie.substring(5).trim());
                Tranzactie gasita = null;
                for (Tranzactie t : deserializate) {
                    if (t.getId() == id) {
                        gasita = t;
                        break;
                    }
                }
                if (gasita == null) {
                    System.out.println("NOTE[" + id + "]: not found");
                } else {
                    System.out.println("NOTE[" + id + "]: " + gasita.getNote());
                }
            }
        }
    }

    private static String format(Tranzactie t) {
        return String.format("[%d] %s %s: %.2f RON | %s -> %s",
                t.getId(), t.getData(), t.getTip(),
                t.getSuma(), t.getContSursa(), t.getContDestinatie());
    }
}