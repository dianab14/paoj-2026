package com.pao.project.cabinet.service;

import com.pao.project.cabinet.exception.Pacientnegasitexception;
import com.pao.project.cabinet.model.Pacient;

import java.util.*;

public class Pacientservice {
    private static Pacientservice instance;

    // Map pentru indexare rapida dupa CNP
    private final Map<String, Pacient> pacientiDupaCnp;
    
    // TreeSet pentru listare sortata alfabetic
    private final TreeSet<Pacient> pacientiSortati;

    private Pacientservice() {
        pacientiDupaCnp = new HashMap<>();
        pacientiSortati = new TreeSet<>();
    }

    public static Pacientservice getInstance() {
        if (instance == null) {
            instance = new Pacientservice();
        }
        return instance;
    }

    // Interogarea 1: Inregistreaza un pacient nou
    public void adaugaPacient(Pacient pacient) {
        if (pacientiDupaCnp.containsKey(pacient.getCnp().getValoare())) {
            System.out.println("[WARN] Pacientul cu CNP " + pacient.getCnp() + " este deja inregistrat.");
            return;
        }
        pacientiDupaCnp.put(pacient.getCnp().getValoare(), pacient);
        pacientiSortati.add(pacient);
        System.out.println("[OK] Pacient inregistrat: " + pacient.getNumeComplet());
    }

    // Interogarea 2: Cauta pacient dupa CNP
    public Pacient cautaDupaCnp(String cnp) throws Pacientnegasitexception {
        Pacient p = pacientiDupaCnp.get(cnp);
        if (p == null) {
            throw new Pacientnegasitexception("Nu exista pacient cu CNP: " + cnp);
        }
        return p;
    }

    // Interogarea 3: Cauta pacienti dupa nume (partial)
    public List<Pacient> cautaDupaNume(String nume) {
        List<Pacient> rezultat = new ArrayList<>();
        for (Pacient p : pacientiDupaCnp.values()) {
            if (p.getNume().toLowerCase().contains(nume.toLowerCase())
                    || p.getPrenume().toLowerCase().contains(nume.toLowerCase())) {
                rezultat.add(p);
            }
        }
        return rezultat;
    }

    // Interogarea 4: Listeaza toti pacientii sortati alfabetic
    public void listeazaTotiPacientii() {
        if (pacientiSortati.isEmpty()) {
            System.out.println("Nu exista pacienti inregistrati.");
            return;
        }
        System.out.println("4. Lista Pacienti (sortati alfabetic)");
        for (Pacient p : pacientiSortati) {
            System.out.println("  " + p);5
        }
    }

    // Interogarea 5: Sterge pacient dupa CNP
    public void stergePacient(String cnp) throws Pacientnegasitexception {
        Pacient p = cautaDupaCnp(cnp);
        pacientiDupaCnp.remove(cnp);
        pacientiSortati.remove(p);
        System.out.println("[OK] Pacient sters: " + p.getNumeComplet());
    }

    // Interogarea 10: Afiseaza istoricul medical al unui pacient
    public void afiseazaIstoricMedical(String cnp) throws Pacientnegasitexception {
        Pacient p = cautaDupaCnp(cnp);
        System.out.println("Istoric Medical: " + p.getNumeComplet() + "");
        if (p.getIstoricMedical().isEmpty()) {
            System.out.println("  (fara intrari)");
        } else {
            for (String intrare : p.getIstoricMedical()) {
                System.out.println("  - " + intrare);
            }
        }
    }

    public int getNrPacienti() {
        return pacientiDupaCnp.size();
    }
}