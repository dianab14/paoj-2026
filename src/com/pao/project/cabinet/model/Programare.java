package com.pao.project.cabinet.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Programare {
    private static int contor = 1;

    private final int id;
    private Pacient pacient;
    private Medic medic;
    private LocalDateTime dataOra;
    private String motiv;
    private boolean confirmata;

    public Programare(Pacient pacient, Medic medic, LocalDateTime dataOra, String motiv) {
        this.id = contor++;
        this.pacient = pacient;
        this.medic = medic;
        this.dataOra = dataOra;
        this.motiv = motiv;
        this.confirmata = false;
    }

    public int getId() { return id; }
    public Pacient getPacient() { return pacient; }
    public Medic getMedic() { return medic; }
    public LocalDateTime getDataOra() { return dataOra; }
    public String getMotiv() { return motiv; }
    public boolean isConfirmata() { return confirmata; }

    public void setDataOra(LocalDateTime dataOra) { this.dataOra = dataOra; }
    public void setConfirmata(boolean confirmata) { this.confirmata = confirmata; }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "Programare #" + id
                + " | Pacient: " + pacient.getNumeComplet()
                + " | Medic: " + medic.getNumeComplet()
                + " | Data: " + dataOra.format(fmt)
                + " | Motiv: " + motiv
                + " | Confirmata: " + (confirmata ? "DA" : "NU");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Programare)) return false;
        return id == ((Programare) o).id;
    }

    @Override
    public int hashCode() { return Integer.hashCode(id); }
}