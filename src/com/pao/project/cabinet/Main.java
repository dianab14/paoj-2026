package com.pao.project.cabinet;
 
import com.pao.project.cabinet.exception.Pacientnegasitexception;
import com.pao.project.cabinet.exception.Programareconflictexception;
import com.pao.project.cabinet.model.*;
import com.pao.project.cabinet.service.Medicservice;
import com.pao.project.cabinet.service.Pacientservice;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {

   static Scanner scanner = new Scanner(System.in);
   static Pacientservice pacientService = Pacientservice.getInstance();
   static Medicservice medicService = Medicservice.getInstance();
     public static void main(String[] args) {

      Medic medicGeneralist = new Medic("Ionescu", "Andrei", "andrei.ionescu@cabinet.ro",
                "0721000001", "PAR001", "Medicina Generala", 10);
      Specialist cardiolog = new Specialist("Popescu", "Maria", "maria.popescu@cabinet.ro",
                "0721000002", "PAR002", "Cardiologie", 15, "Conferentiar", "Spitalul Fundeni");
      medicService.adaugaMedic(medicGeneralist);
      medicService.adaugaMedic(cardiolog);

      Receptioner receptioner = new Receptioner("Dinu", "Elena", 
        "elena@cabinet.ro", "0721000003", "08:00-16:00");
      medicService.adaugaReceptioner(receptioner);

      boolean running = true;
        while (running) {
            System.out.println("1.  Inregistreaza un pacient nou");
            System.out.println("2.  Cauta pacient dupa CNP");
            System.out.println("3.  Cauta pacienti dupa nume");
            System.out.println("4.  Listeaza toti pacientii sortati alfabetic");
            System.out.println("5.  Sterge un pacient");
            System.out.println("6.  Listeaza medici dupa specialitate");
            System.out.println("7.  Programeaza un pacient la un medic");
            System.out.println("8.  Confirma o programare");
            System.out.println("9.  Inregistreaza o consultatie");
            System.out.println("10. Afiseaza istoricul medical al unui pacient");
            System.out.println("0.  Iesire");
            System.out.print("Alege o optiune: ");

            int optiune = citesteInt();

            switch (optiune) {
                case 1 -> actiunea1();
                case 2 -> actiunea2();
                case 3 -> actiunea3();
                case 4 -> actiunea4();
                case 5 -> actiunea5();
                case 6 -> actiunea6();
                case 7 -> actiunea7();
                case 8 -> actiunea8();
                case 9 -> actiunea9();
                case 10 -> actiunea10();
                case 0 -> running = false;
                default -> System.out.println("[EROARE] Optiune invalida. Alege un numar intre 0 si 10.");
            }
        }
        scanner.close();
   }

    static int citesteInt() {
        while (true) {
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                return val;
            } catch (NumberFormatException e) {
                System.out.print("[EROARE] Introdu un numar valid: ");
            }
        }
    }

    ///Interogarea 1
    static void actiunea1() {
        System.out.println("\n1. Inregistrare pacient nou");
        System.out.print("Nume: ");
        String nume = scanner.nextLine().trim();
        System.out.print("Prenume: ");
        String prenume = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Telefon: ");
        String telefon = scanner.nextLine().trim();
        System.out.print("CNP (13 cifre): ");
        String cnpStr = scanner.nextLine().trim();
        System.out.print("Varsta: ");
        int varsta = citesteInt();
        System.out.print("Adresa: ");
        String adresa = scanner.nextLine().trim();

        try {
            CNP cnp = new CNP(cnpStr);
            Pacient p = new Pacient(nume, prenume, email, telefon, cnp, varsta, adresa);
            pacientService.adaugaPacient(p);
        } catch (IllegalArgumentException e) {
            System.out.println("[EROARE] " + e.getMessage());
        }
    }

    ///Interogarea2
   static void actiunea2(){
      System.out.println("\n2. Cautare pacient dupa CNP");
      System.out.print("Introdu CNP: ");
      String cnp = scanner.nextLine().trim();
      try {
         Pacient p = pacientService.cautaDupaCnp(cnp);
         System.out.println("Pacient gasit: " + p);
      } catch (Pacientnegasitexception e) {
         System.out.println("[EROARE] " + e.getMessage());
      }

   }

   ///Interogarea 3
   static void actiunea3(){
      System.out.println("\n3. Cautare pacienti dupa nume");
      System.out.print("Introdu numele (sau o parte din el): ");
      String nume = scanner.nextLine().trim();
      List<Pacient> rezultat = pacientService.cautaDupaNume(nume);
      if (rezultat.isEmpty()) {
         System.out.println("Niciun pacient gasit.");
      } else {
         rezultat.forEach(p -> System.out.println("  " + p));
      }   
   }

   ///Interogarea 4
   static void actiunea4(){
      System.out.println();
      pacientService.listeazaTotiPacientii();
   }

   ///Interogarea 5
   static void actiunea5(){
      System.out.println("\n5. Stergere pacient");
      System.out.print("Introdu CNP-ul pacientului de sters: ");
      String cnp = scanner.nextLine().trim();
      try {
         pacientService.stergePacient(cnp);
      } catch (Pacientnegasitexception e) {
         System.out.println("[EROARE] " + e.getMessage());
      }
   }
   
   ///Interogarea 6
   static void actiunea6(){
      System.out.println("\n6. Medici dupa specialitate");
      System.out.print("Introdu specialitatea: ");
      String spec = scanner.nextLine().trim();
      medicService.listeazaMediciDupaSpecialitate(spec);
   }

   ///Interogarea 7
   static void actiunea7(){
      System.out.println("\n7. Programare pacient");

      System.out.print("Numele receptionerului: ");
      String numeReceptioner = scanner.nextLine().trim();
      Receptioner receptioner = medicService.cautaReceptioner(numeReceptioner);
      if (receptioner == null) {
         System.out.println("[EROARE] Receptionerul nu a fost gasit. Programare anulata.");
         return;
      }
      System.out.println("[OK] Programare initiata de: " + receptioner.getNumeComplet());
      System.out.print("CNP pacient: ");
      String cnp = scanner.nextLine().trim();
      Pacient pacient;
      try {
         pacient = pacientService.cautaDupaCnp(cnp);
      } catch (Pacientnegasitexception e) {
         System.out.println("[EROARE] " + e.getMessage());
         return;
      }

      System.out.print("Parafa medic (ex: PAR001): ");
      String parafa = scanner.nextLine().trim();
      System.out.print("Motiv: ");
      String motiv = scanner.nextLine().trim();
      System.out.print("An: ");
      int an = citesteInt();
      System.out.print("Luna (1-12): ");
      int luna = citesteInt();
      System.out.print("Zi: ");
      int zi = citesteInt();
      System.out.print("Ora (0-23): ");
      int ora = citesteInt();
      System.out.print("Minut (0-59): ");
      int minut = citesteInt();

      try {
         LocalDateTime dataOra = LocalDateTime.of(an, luna, zi, ora, minut);
         medicService.programeazaPacient(pacient, parafa, dataOra, motiv);
      } catch (Programareconflictexception e) {
         System.out.println("[EROARE] " + e.getMessage());
      } catch (IllegalArgumentException e) {
         System.out.println("[EROARE] " + e.getMessage());
      }
   }

   ///Interogarea 8
   static void actiunea8(){
      System.out.println("\n8. Confirmare programare");
      System.out.print("ID programare: ");
      int id = citesteInt();
      medicService.confirmaPrograme(id);   
   }

   ///Interogarea 9
   static void actiunea9(){
      System.out.println("\n9. Inregistrare consultatie");
      System.out.print("CNP pacient: ");
      String cnp = scanner.nextLine().trim();
      Pacient pacient;
      try {
         pacient = pacientService.cautaDupaCnp(cnp);
      } catch (Pacientnegasitexception e) {
         System.out.println("[EROARE] " + e.getMessage());
            return;
      }

      System.out.print("Parafa medic (ex: PAR001): ");
      String parafa = scanner.nextLine().trim();
      System.out.print("Diagnostic: ");
      String diagnostic = scanner.nextLine().trim();
      System.out.print("Observatii: ");
      String observatii = scanner.nextLine().trim();

      Consultatie c = medicService.inregistreazaConsultatie(pacient, parafa, LocalDateTime.now(), 
         diagnostic, observatii);

      System.out.print("Vrei sa adaugi o reteta? (da/nu): ");
      String raspuns = scanner.nextLine().trim();
      if (raspuns.equalsIgnoreCase("da")) {
         System.out.print("Cate medicamente? ");
         int nr = citesteInt();
         java.util.List<Medicament> medicamente = new java.util.ArrayList<>();
         for (int i = 0; i < nr; i++) {
            System.out.println("Medicament " + (i + 1) + ":");
            System.out.print("  Denumire: ");
            String den = scanner.nextLine().trim();
            System.out.print("  Dozaj: ");
            String doz = scanner.nextLine().trim();
            System.out.print("  Producator: ");
            String prod = scanner.nextLine().trim();
            medicamente.add(new Medicament(den, doz, prod));
         }
         System.out.print("Observatii reteta: ");
         String obsReteta = scanner.nextLine().trim();
         Reteta reteta = new Reteta(medicService.getMediciDupaParafa().get(parafa), pacient, medicamente, LocalDate.now(), obsReteta);
         c.setReteta(reteta);
         System.out.println("Reteta adaugata:\n" + reteta);
        }
    }
   
   ///Interogarea 10
   static void actiunea10(){
      System.out.println("\n10. Istoric medical");
      System.out.print("CNP pacient: ");
      String cnp = scanner.nextLine().trim();
      try {
         pacientService.afiseazaIstoricMedical(cnp);
      } catch (Pacientnegasitexception e) {
         System.out.println("[EROARE] " + e.getMessage());
      }
   }
}
