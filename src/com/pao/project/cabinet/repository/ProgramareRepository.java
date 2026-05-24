package com.pao.project.cabinet.repository;

import com.pao.project.cabinet.model.Medic;
import com.pao.project.cabinet.model.Pacient;
import com.pao.project.cabinet.model.Programare;
import com.pao.project.cabinet.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProgramareRepository implements Repository<Programare, Integer> {

    private final Connection connection;

    public ProgramareRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Programare p) {
        String sql = "INSERT INTO programari (cnp_pacient, cod_parafa_medic, data_ora, motiv, confirmata) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, p.getPacient().getCnp().getValoare());
            stmt.setString(2, p.getMedic().getCodParafa());
            stmt.setString(3, p.getDataOra().toString());
            stmt.setString(4, p.getMotiv());
            stmt.setInt(5, p.isConfirmata() ? 1 : 0);
            stmt.executeUpdate();
            System.out.println("[DB] Programare salvata: #" + p.getId());
        } catch (SQLException e) {
            System.out.println("[DB] Eroare la salvare programare: " + e.getMessage());
        }
    }

    // ── TRANZACTIE JDBC ────────────────────────────────────────
    // Confirma programarea SI salveaza un log in consultatii
    // Daca una esueaza, se face rollback la amandoua
    public void confirmaPrograreSiSalveazaLog(Programare p, String diagnostic) {
        try {
            connection.setAutoCommit(false);

            // Operatia 1: confirma programarea
            String sqlConfirma = "UPDATE programari SET confirmata = 1 " +
                                 "WHERE cnp_pacient = ? AND cod_parafa_medic = ? AND data_ora = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sqlConfirma)) {
                stmt.setString(1, p.getPacient().getCnp().getValoare());
                stmt.setString(2, p.getMedic().getCodParafa());
                stmt.setString(3, p.getDataOra().toString());
                stmt.executeUpdate();
            }

            // Operatia 2: salveaza consultatie in baza de date
            String sqlConsultatie = "INSERT INTO consultatii (cnp_pacient, cod_parafa_medic, data_ora, diagnostic, observatii) " +
                                    "VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sqlConsultatie)) {
                stmt.setString(1, p.getPacient().getCnp().getValoare());
                stmt.setString(2, p.getMedic().getCodParafa());
                stmt.setString(3, p.getDataOra().toString());
                stmt.setString(4, diagnostic);
                stmt.setString(5, "Inregistrat automat la confirmare programare");
                stmt.executeUpdate();
            }

            connection.commit();
            System.out.println("[DB] Tranzactie reusita: programare confirmata + consultatie salvata.");

        } catch (SQLException e) {
            try {
                connection.rollback();
                System.out.println("[DB] Rollback efectuat: " + e.getMessage());
            } catch (SQLException ex) {
                System.out.println("[DB] Eroare la rollback: " + ex.getMessage());
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("[DB] Eroare la resetare autocommit: " + e.getMessage());
            }
        }
    }

    @Override
    public Optional<Programare> findById(Integer id) {
        String sql = "SELECT * FROM programari WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("[DB] Eroare la cautare programare: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Programare> findAll() {
        List<Programare> lista = new ArrayList<>();
        String sql = "SELECT * FROM programari";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("[DB] Eroare la listare programari: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void update(Programare p) {
        String sql = "UPDATE programari SET motiv=?, confirmata=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, p.getMotiv());
            stmt.setInt(2, p.isConfirmata() ? 1 : 0);
            stmt.setInt(3, p.getId());
            stmt.executeUpdate();
            System.out.println("[DB] Programare actualizata: #" + p.getId());
        } catch (SQLException e) {
            System.out.println("[DB] Eroare la actualizare programare: " + e.getMessage());
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM programari WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("[DB] Programare stearsa: #" + id);
        } catch (SQLException e) {
            System.out.println("[DB] Eroare la stergere programare: " + e.getMessage());
        }
    }

    // ── JOIN 3: programari active cu datele pacientului si medicului ──
    public List<String> findProgramariCuDetalii() {
        List<String> rezultat = new ArrayList<>();
        String sql = """
                SELECT p.nume, p.prenume, m.nume as nume_medic, m.prenume as prenume_medic,
                       pr.data_ora, pr.motiv, pr.confirmata
                FROM programari pr
                JOIN pacienti p ON pr.cnp_pacient = p.cnp
                JOIN medici m ON pr.cod_parafa_medic = m.cod_parafa
                ORDER BY pr.data_ora
                """;
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                rezultat.add("Pacient: " + rs.getString("prenume") + " " + rs.getString("nume")
                        + " | Medic: Dr. " + rs.getString("prenume_medic") + " " + rs.getString("nume_medic")
                        + " | Data: " + rs.getString("data_ora")
                        + " | Motiv: " + rs.getString("motiv")
                        + " | Confirmat: " + (rs.getInt("confirmata") == 1 ? "DA" : "NU"));
            }
        } catch (SQLException e) {
            System.out.println("[DB] Eroare JOIN programari detalii: " + e.getMessage());
        }
        return rezultat;
    }

    private Programare mapRow(ResultSet rs) throws SQLException {
        // Cream obiecte minimale pentru Pacient si Medic
        Pacient pacient = new Pacient(
                rs.getString("cnp_pacient"), "", "", "",
                new com.pao.project.cabinet.model.CNP(rs.getString("cnp_pacient")),
                0, "");
        Medic medic = new Medic(
                rs.getString("cod_parafa_medic"), "", "", "",
                rs.getString("cod_parafa_medic"), "", 0);

        Programare p = new Programare(pacient, medic,
                LocalDateTime.parse(rs.getString("data_ora")),
                rs.getString("motiv"));
        p.setConfirmata(rs.getInt("confirmata") == 1);
        return p;
    }
}