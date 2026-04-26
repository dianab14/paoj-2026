package com.pao.project.cabinet.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Consultatie {
    private static int contor = 1;

    private final int id;
    private Pacient pacient;
    private Medic medic;
    private LocalDateTime dataOra;
    private String diagnostic;
    private String observatii;
    private Reteta reteta; // poate fi null

    public Consultatie(Pacient pacient, Medic medic, LocalDateTime dataOra,
                       String diagnostic, String observatii) {
        this.id = contor++;
        this.pacient = pacient;
        this.medic = medic;
        this.dataOra = dataOra;
        this.diagnostic = diagnostic;
        this.observatii = observatii;
    }

    public int getId() { return id; }
    public Pacient getPacient() { return pacient; }
    public Medic getMedic() { return medic; }
    public LocalDateTime getDataOra() { return dataOra; }
    public String getDiagnostic() { return diagnostic; }
    public String getObservatii() { return observatii; }
    public Reteta getReteta() { return reteta; }

    public void setReteta(Reteta reteta) { this.reteta = reteta; }
    public void setDiagnostic(String diagnostic) { this.diagnostic = diagnostic; }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "Consultatie #" + id
                + " | Pacient: " + pacient.getNumeComplet()
                + " | Medic: " + medic.getNumeComplet()
                + " | Data: " + dataOra.format(fmt)
                + " | Diagnostic: " + diagnostic
                + (reteta != null ? " | Are reteta: DA" : "");
    }
}