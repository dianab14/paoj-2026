package com.pao.project.cabinet;
 
import com.pao.project.cabinet.exception.Pacientnegasitexception;
import com.pao.project.cabinet.exception.Programareconflictexception;
import com.pao.project.cabinet.model.*;
import com.pao.project.cabinet.repository.ConsultatieRepository;
import com.pao.project.cabinet.repository.MedicRepository;
import com.pao.project.cabinet.repository.PacientRepository;
import com.pao.project.cabinet.repository.ProgramareRepository;
import com.pao.project.cabinet.service.AuditService;
import com.pao.project.cabinet.service.Medicservice;
import com.pao.project.cabinet.service.Pacientservice;
import com.pao.project.cabinet.util.DatabaseConnection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {

   static Scanner scanner = new Scanner(System.in);
   static Pacientservice pacientService = Pacientservice.getInstance();
   static Medicservice medicService = Medicservice.getInstance();

   static PacientRepository pacientRepo;
   static MedicRepository medicRepo;
   static ProgramareRepository programareRepo;
   static ConsultatieRepository consultatieRepo;

   public static void main(String[] args) {

      // Initializeaza conexiunea la baza de date
      DatabaseConnection.getInstance();
      pacientRepo = new PacientRepository();
      medicRepo = new MedicRepository();
      programareRepo = new ProgramareRepository();
      consultatieRepo = new ConsultatieRepository();

      System.out.println("[TEST] DB initializat, caut cabinet.db...");
      System.out.println("[TEST] User dir: " + System.getProperty("user.dir"));

      // Adauga medicii in memorie si in baza de date
      Medic medicGeneralist = new Medic("Ionescu", "Andrei", "andrei.ionescu@cabinet.ro",
                "0721000001", "PAR001", "Medicina Generala", 10);
      Specialist cardiolog = new Specialist("Popescu", "Maria", "maria.popescu@cabinet.ro",
                "0721000002", "PAR002", "Cardiologie", 15, "Conferentiar", "Spitalul Fundeni");
      medicService.adaugaMedic(medicGeneralist);
      medicService.adaugaMedic(cardiolog);
      medicRepo.save(medicGeneralist);
      medicRepo.save(cardiolog);

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
                case 0 -> {
                    System.out.println("La revedere!");
                    DatabaseConnection.getInstance().closeConnection();
                    running = false;
                }
                default -> System.out.println("[EROARE] Optiune invalida.");
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

    // ── ACTIUNEA 1 ──────────────────────────────────────────
    static void actiunea1() {
        AuditService.getInstance().log("inregistreaza_pacient");

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
            pacientService.adaugaPacient(p);  // adauga in memorie
            pacientRepo.save(p);              // adauga in baza de date
        } catch (IllegalArgumentException e) {
            System.out.println("[EROARE] " + e.getMessage());
        }
    }

    // ── ACTIUNEA 2 ──────────────────────────────────────────
    static void actiunea2() {
        AuditService.getInstance().log("cauta_pacient_cnp");

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

    // ── ACTIUNEA 3 ──────────────────────────────────────────
    static void actiunea3() {
        AuditService.getInstance().log("cauta_pacienti_nume");

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

    // ── ACTIUNEA 4 ──────────────────────────────────────────
    static void actiunea4() {
        AuditService.getInstance().log("listeaza_pacienti");

        System.out.println("\n4. Lista pacienti din baza de date:");
        List<Pacient> toti = pacientRepo.findAll();
        if (toti.isEmpty()) {
            System.out.println("  (niciun pacient in baza de date)");
        } else {
            toti.forEach(p -> System.out.println("  " + p));
        }
    }

    // ── ACTIUNEA 5 ──────────────────────────────────────────
    static void actiunea5() {
        AuditService.getInstance().log("sterge_pacient");

        System.out.println("\n5. Stergere pacient");
        System.out.print("Introdu CNP-ul pacientului de sters: ");
        String cnp = scanner.nextLine().trim();
        try {
            pacientService.stergePacient(cnp);  // sterge din memorie
            pacientRepo.delete(cnp);            // sterge din baza de date
        } catch (Pacientnegasitexception e) {
            System.out.println("[EROARE] " + e.getMessage());
        }
    }

    // ── ACTIUNEA 6 ──────────────────────────────────────────
    static void actiunea6() {
        AuditService.getInstance().log("listeaza_medici_specialitate");

        System.out.println("\n6. Medici dupa specialitate");
        System.out.print("Introdu specialitatea: ");
        String spec = scanner.nextLine().trim();
        medicService.listeazaMediciDupaSpecialitate(spec);

        // Afiseaza si din baza de date cu JOIN
        System.out.println("\n--- Statistici medici (din DB) ---");
        medicRepo.findMediciCuNrConsultatii()
                .forEach(s -> System.out.println("  " + s));
    }

    // ── ACTIUNEA 7 ──────────────────────────────────────────
    static void actiunea7() {
        AuditService.getInstance().log("programeaza_pacient");

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
            Programare prog = medicService.programeazaPacient(pacient, parafa, dataOra, motiv);
            programareRepo.save(prog);  // salveaza in baza de date
        } catch (Programareconflictexception e) {
            System.out.println("[EROARE] " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("[EROARE] " + e.getMessage());
        }
    }

    // ── ACTIUNEA 8 ──────────────────────────────────────────
    static void actiunea8() {
        AuditService.getInstance().log("confirma_programare");

        System.out.println("\n8. Confirmare programare");
        System.out.print("ID programare: ");
        int id = citesteInt();
        medicService.confirmaPrograme(id);

        // Afiseaza toate programarile cu detalii din DB (JOIN)
        System.out.println("\n--- Toate programarile (din DB) ---");
        programareRepo.findProgramariCuDetalii()
                .forEach(s -> System.out.println("  " + s));
    }

    // ── ACTIUNEA 9 ──────────────────────────────────────────
    static void actiunea9() {
        AuditService.getInstance().log("inregistreaza_consultatie");

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

        Consultatie c = medicService.inregistreazaConsultatie(
                pacient, parafa, LocalDateTime.now(), diagnostic, observatii);
        consultatieRepo.save(c);  // salveaza in baza de date

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
            Reteta reteta = new Reteta(
                    medicService.getMediciDupaParafa().get(parafa),
                    pacient, medicamente, LocalDate.now(), obsReteta);
            c.setReteta(reteta);
            System.out.println("Reteta adaugata:\n" + reteta);
        }
    }

    // ── ACTIUNEA 10 ──────────────────────────────────────────
    static void actiunea10() {
        AuditService.getInstance().log("afiseaza_istoric_medical");

        System.out.println("\n10. Istoric medical");
        System.out.print("CNP pacient: ");
        String cnp = scanner.nextLine().trim();
        try {
            pacientService.afiseazaIstoricMedical(cnp);
        } catch (Pacientnegasitexception e) {
            System.out.println("[EROARE] " + e.getMessage());
        }

        // Afiseaza si statistica pacienti cu programari (JOIN)
        System.out.println("\n--- Statistici pacienti (din DB) ---");
        pacientRepo.findPacientiCuNrProgramari()
                .forEach(s -> System.out.println("  " + s));
    }
}