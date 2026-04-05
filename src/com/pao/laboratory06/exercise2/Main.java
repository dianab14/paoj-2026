package com.pao.laboratory06.exercise2;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        Colaborator[] colaboratori = new Colaborator[n];

        for (int i = 0; i < n; i++) {
            String tip = scanner.next();
            Colaborator c = switch (tip) {
                case "CIM" -> new CIMColaborator();
                case "PFA" -> new PFAColaborator();
                case "SRL" -> new SRLColaborator();
                default -> throw new IllegalArgumentException("Tip necunoscut: " + tip);
            };
            c.citeste(scanner);
            colaboratori[i] = c;
        }

        for (Colaborator c : colaboratori) c.afiseaza();

        Colaborator[] sortati = colaboratori.clone();
        Arrays.sort(sortati);
        System.out.println();
        System.out.print("Colaborator cu venit net maxim: ");
        sortati[0].afiseaza();

        System.out.println("\nColaboratori persoane juridice:");
        for (Colaborator c : sortati)
            if (c instanceof PersoanaJuridica) c.afiseaza();

        System.out.println("\nSume și număr colaboratori pe tip:");
        for (TipColaborator tip : TipColaborator.values()) {
            double suma = 0;
            int count = 0;
            for (Colaborator c : colaboratori) {
                if (c.getTip() == tip) {
                    suma += c.calculeazaVenitNetAnual();
                    count++;
                }
            }
            if (count == 0) {
                System.out.printf("%s: suma = nu lei, număr = null%n", tip);
            } else {
                System.out.printf("%s: suma = %.2f lei, număr = %d%n", tip, suma, count);
            }
        }
    }
}