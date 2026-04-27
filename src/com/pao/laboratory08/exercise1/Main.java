
//public class Main {
    // Calea către fișierul cu date — relativă la rădăcina proiectului
   ///private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    //public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește studenții din FILE_PATH cu BufferedReader
        // 2. Citește comanda din stdin: PRINT, SHALLOW <nume> sau DEEP <nume>
        // 3. Execută comanda:
        //    - PRINT → afișează toți studenții
        //    - SHALLOW <nume> → shallow clone + modifică orașul clonei la "MODIFICAT" + afișează
        //    - DEEP <nume> → deep clone + modifică orașul clonei la "MODIFICAT" + afișează

       // System.out.println("TODO: implementează exercițiul 1");
   // }
//}

package com.pao.laboratory08.exercise1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static List<Student> citesteStudenti() throws IOException {
        List<Student> studenti = new ArrayList<>();
        String cale = "src/com/pao/laboratory08/tests/studenti.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(cale))) {
            String linie;
            while ((linie = br.readLine()) != null) {
                if (linie.trim().isEmpty()) continue;
                String[] parts = linie.split(",");
                String nume = parts[0].trim();
                int varsta = Integer.parseInt(parts[1].trim());
                String oras = parts[2].trim();
                String strada = parts[3].trim();
                studenti.add(new Student(nume, varsta, new Adresa(oras, strada)));
            }
        }
        return studenti;
    }

    public static void main(String[] args) throws IOException, CloneNotSupportedException {
        Scanner scanner = new Scanner(System.in);
        String comanda = scanner.nextLine().trim();

        List<Student> studenti = citesteStudenti();

        if (comanda.equals("PRINT")) {
            for (Student s : studenti) {
                System.out.println(s);
            }

        } else if (comanda.startsWith("SHALLOW ")) {
            String[] parts = comanda.split(" ", 2);
            String nume = parts[1].trim();

            Student original = null;
            for (Student s : studenti) {
                if (s.getNume().equals(nume)) {
                    original = s;
                    break;
                }
            }

            Student clona = original.shallowClone();
            clona.getAdresa().setOras("MODIFICAT");

            System.out.println("Original: " + original);
            System.out.println("Clona: " + clona);

        } else if (comanda.startsWith("DEEP ")) {
            String[] parts = comanda.split(" ", 2);
            String nume = parts[1].trim();

            Student original = null;
            for (Student s : studenti) {
                if (s.getNume().equals(nume)) {
                    original = s;
                    break;
                }
            }

            Student clona = original.deepClone();
            clona.getAdresa().setOras("MODIFICAT");

            System.out.println("Original: " + original);
            System.out.println("Clona: " + clona);
        }
    }
}