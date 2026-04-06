package com.pao.laboratory06.exercise3;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        ///Array inginer + sortare
        Inginer[] ingineri = {
            new Inginer("Zamfir", "Ion", "0721000001", 8000, 15000),
            new Inginer("Andrei", "Maria", "0721000002", 12000, 30000),
            new Inginer("Popescu", "Dan", null, 9500, 20000)
        };

        System.out.println("=== Sortare naturală (după nume) ===");
        Arrays.sort(ingineri);
        for (Inginer i : ingineri) System.out.println(i);

        System.out.println("\n=== Sortare după salariu descrescător ===");
        Arrays.sort(ingineri, new ComparatorInginerSalariu());
        for (Inginer i : ingineri) System.out.println(i);

        // Acces prin referinta PlataOnline
        System.out.println("\n=== Acces prin interfață PlataOnline ===");
        PlataOnline cont = new Inginer("Ionescu", "Ana", "0722000003", 7000, 5000);
        cont.autentificare("ana", "parola123");
        System.out.println("Sold: " + cont.consultareSold());
        cont.efectuarePlata(1000);

        //PersoanaJuridica prin PlataOnlineSMS + stocare SMS
        System.out.println("\n=== PersoanaJuridica cu SMS ===");
        PersoanaJuridica firma = new PersoanaJuridica("TechSRL", "SRL", "0733000004", 50000);
        PlataOnlineSMS contSMS = firma;

        contSMS.autentificare("techsrl", "secret");
        contSMS.efectuarePlata(5000);
        contSMS.trimiteSMS("Plată confirmată: 5000 lei");
        contSMS.trimiteSMS("Sold actualizat: 45000 lei");
        System.out.println("SMS-uri trimise: " + firma.getSmsTrimise());

        // Fără telefon
        System.out.println("\n=== PersoanaJuridica fără telefon ===");
        PersoanaJuridica firmaFaraTel = new PersoanaJuridica("NoPhoneSRL", "SRL", null, 10000);
        firmaFaraTel.trimiteSMS("Test SMS");

        // Mesaj invalid
        System.out.println("\n=== SMS cu mesaj null ===");
        boolean rezultat = firma.trimiteSMS(null);
        System.out.println("Rezultat trimitere SMS null: " + rezultat);

        // Constante financiare ---
        System.out.println("\n=== Constante financiare ===");
        System.out.println("TVA: " + ConstanteFinanciare.TVA.getValoare());
        System.out.println("Salariu minim: " + ConstanteFinanciare.SALARIU_MINIM.getValoare());
        System.out.println("Cota impozit: " + ConstanteFinanciare.COTA_IMPOZIT.getValoare());

        // Tratare cazuri eroare
        System.out.println("\n=== Edge cases ===");

        // Autentificare cu user null
        try {
            cont.autentificare(null, "parola");
        } catch (IllegalArgumentException e) {
            System.out.println("Eroare autentificare: " + e.getMessage());
        }

        // Inginerii nu implementeaza PlataOnlineSMS, deci cast-ul arunca ClassCastException
        try {
            PlataOnline inginer = new Inginer("Test", "Test", "0700", 5000, 1000);
            PlataOnlineSMS smsInginer = (PlataOnlineSMS) inginer; // ClassCastException
            smsInginer.trimiteSMS("test");
        } catch (ClassCastException e) {
            System.out.println("Eroare: Inginerul nu suportă SMS — " + e.getMessage());
        }
    }
}