package com.pao.project.cabinet.model;

import java.time.LocalDate;
import java.util.List;

public class Reteta {
    private static int contor = 1;

    private final int id;
    private Medic medic;
    private Pacient pacient;
    private List<Medicament> medicamente;
    private LocalDate dataEmitere;
    private String observatii;

    public Reteta(Medic medic, Pacient pacient, List<Medicament> medicamente,
                  LocalDate dataEmitere, String observatii) {
        this.id = contor++;
        this.medic = medic;
        this.pacient = pacient;
        this.medicamente = medicamente;
        this.dataEmitere = dataEmitere;
        this.observatii = observatii;
    }

    public int getId() { return id; }
    public Medic getMedic() { return medic; }
    public Pacient getPacient() { return pacient; }
    public List<Medicament> getMedicamente() { return medicamente; }
    public LocalDate getDataEmitere() { return dataEmitere; }
    public String getObservatii() { return observatii; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Reteta #").append(id)
          .append(" | Medic: ").append(medic.getNumeComplet())
          .append(" | Pacient: ").append(pacient.getNumeComplet())
          .append(" | Data: ").append(dataEmitere)
          .append("\n  Medicamente:");
        for (Medicament m : medicamente) {
            sb.append("\n    - ").append(m);
        }
        if (observatii != null && !observatii.isEmpty()) {
            sb.append("\n  Observatii: ").append(observatii);
        }
        return sb.toString();
    }
}