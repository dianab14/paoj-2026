package com.pao.project.cabinet.service;

import com.pao.project.cabinet.exception.Programareconflictexception;
import com.pao.project.cabinet.model.Pacient;
import com.pao.project.cabinet.model.Medic;
import com.pao.project.cabinet.model.Programare;
import com.pao.project.cabinet.model.Receptioner;
import com.pao.project.cabinet.model.Consultatie;
import com.pao.project.cabinet.model.Receptioner;

import java.time.LocalDateTime;
import java.util.*;

public class Medicservice {
    private static Medicservice instance;

    // Map: parafa -> Medic
    private final Map<String, Medic> mediciDupaParafa;
    // Map: specialitate -> Lista de medici
    private final Map<String, List<Medic>> mediciDupaSpecialitate;
    // Lista de programari
    private final List<Programare> programari;
    // Lista de consultatii
    private final List<Consultatie> consultatii;

    private final List<Receptioner> receptioneri = new ArrayList<>();

    public void adaugaReceptioner(Receptioner r) {
        receptioneri.add(r);
    }

    public Receptioner cautaReceptioner(String nume) {
        return receptioneri.stream()
            .filter(r -> r.getNumeComplet().equalsIgnoreCase(nume))
            .findFirst()
            .orElse(null);
    }

    private Medicservice() {
        mediciDupaParafa = new HashMap<>();
        mediciDupaSpecialitate = new HashMap<>();
        programari = new ArrayList<>();
        consultatii = new ArrayList<>();
    }

    public static Medicservice getInstance() {
        if (instance == null) {
            instance = new Medicservice();
        }
        return instance;
    }

    public void adaugaMedic(Medic medic) {
        if (mediciDupaParafa.containsKey(medic.getCodParafa())) {
            System.out.println("[WARN] Medicul cu parafa " + medic.getCodParafa() + " exista deja.");
            return;
        }
        mediciDupaParafa.put(medic.getCodParafa(), medic);
        mediciDupaSpecialitate
                .computeIfAbsent(medic.getSpecialitate(), k -> new ArrayList<>())
                .add(medic);
        System.out.println("[OK] Medic adaugat: " + medic.getNumeComplet() + " (" + medic.getRol() + ")");
    }

    //Interogarea 6: Listeaza medici dupa specialitate
    public void listeazaMediciDupaSpecialitate(String specialitate) {
        List<Medic> lista = mediciDupaSpecialitate.getOrDefault(specialitate, Collections.emptyList());
        System.out.println("Medici cu specialitatea '" + specialitate + "'");
        if (lista.isEmpty()) {
            System.out.println("  (niciun medic gasit)");
        } else {
            lista.forEach(m -> System.out.println("  " + m));
        }
    }

    //Interogarea 7: Programeaza un pacient la un medic
    public Programare programeazaPacient(Pacient pacient, String parafaMedic, LocalDateTime dataOra, String motiv)
            throws Programareconflictexception {
        Medic medic = mediciDupaParafa.get(parafaMedic);
        if (medic == null) {
            throw new IllegalArgumentException("Medicul cu parafa " + parafaMedic + " nu exista.");
        }

        // Verifica conflict de programare (acelasi medic, aceeasi ora)
        for (Programare p : programari) {
            if (p.getMedic().getCodParafa().equals(parafaMedic)
                    && p.getDataOra().equals(dataOra)) {
                throw new Programareconflictexception(
                        "Medicul " + medic.getNumeComplet() + " are deja o programare la " + dataOra);
            }
        }

        Programare prog = new Programare(pacient, medic, dataOra, motiv);
        programari.add(prog);
        System.out.println("[OK] Programare creata: " + prog);
        return prog;
    }

    //Interogarea 8: Confirma o programare
    public void confirmaPrograme(int idProgramare) {
        for (Programare p : programari) {
            if (p.getId() == idProgramare) {
                p.setConfirmata(true);
                System.out.println("[OK] Programare #" + idProgramare + " confirmata.");
                return;
            }
        }
        System.out.println("[WARN] Programarea #" + idProgramare + " nu a fost gasita.");
    }

    //Interogarea 9: Inregistreaza o consultatie si actualizeaza istoricul pacientului
    public Consultatie inregistreazaConsultatie(Pacient pacient, String parafaMedic, LocalDateTime dataOra, String diagnostic, String observatii) {
        Medic medic = mediciDupaParafa.get(parafaMedic);
        if (medic == null) {
            throw new IllegalArgumentException("Medicul cu parafa " + parafaMedic + " nu exista.");
        }

        Consultatie c = new Consultatie(pacient, medic, dataOra, diagnostic, observatii);
        consultatii.add(c);
        pacient.adaugaInIstoricMedical("Consultatie " + dataOra.toLocalDate()
                + ": " + diagnostic + " (Dr. " + medic.getNumeComplet() + ")");
        System.out.println("[OK] Consultatie inregistrata: " + c);
        return c;
    }


    public void listeazaProgramariMedic(String parafaMedic) {
        System.out.println("Programari medic parafa " + parafaMedic);
        programari.stream()
                .filter(p -> p.getMedic().getCodParafa().equals(parafaMedic))
                .forEach(p -> System.out.println("  " + p));
    }


    public void listeazaToateConsultatiile() {
        System.out.println("Toate consultatiile ");
        if (consultatii.isEmpty()) {
            System.out.println("  (nicio consultatie)");
        } else {
            consultatii.forEach(c -> System.out.println("  " + c));
        }
    }

    public List<Programare> getProgramari() { return programari; }
    public List<Consultatie> getConsultatii() { return consultatii; }
    public Map<String, Medic> getMediciDupaParafa() { return mediciDupaParafa; }
}