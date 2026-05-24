package com.pao.project.cabinet.repository;

import com.pao.project.cabinet.model.CNP;
import com.pao.project.cabinet.model.Pacient;
import com.pao.project.cabinet.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PacientRepository implements Repository<Pacient, String> {

    private final Connection connection;

    public PacientRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Pacient p) {
        String sql = "INSERT INTO pacienti (cnp, nume, prenume, email, telefon, varsta, adresa) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, p.getCnp().getValoare());
            stmt.setString(2, p.getNume());
            stmt.setString(3, p.getPrenume());
            stmt.setString(4, p.getEmail());
            stmt.setString(5, p.getTelefon());
            stmt.setInt(6, p.getVarsta());
            stmt.setString(7, p.getAdresa());
            stmt.executeUpdate();
            System.out.println("[DB] Pacient salvat: " + p.getNumeComplet());
        } catch (SQLException e) {
            System.out.println("[DB] Eroare la salvare pacient: " + e.getMessage());
        }
    }

    @Override
    public Optional<Pacient> findById(String cnp) {
        String sql = "SELECT * FROM pacienti WHERE cnp = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cnp);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("[DB] Eroare la cautare pacient: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Pacient> findAll() {
        List<Pacient> lista = new ArrayList<>();
        String sql = "SELECT * FROM pacienti ORDER BY nume";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("[DB] Eroare la listare pacienti: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void update(Pacient p) {
        String sql = "UPDATE pacienti SET nume=?, prenume=?, email=?, telefon=?, varsta=?, adresa=? " +
                     "WHERE cnp=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, p.getNume());
            stmt.setString(2, p.getPrenume());
            stmt.setString(3, p.getEmail());
            stmt.setString(4, p.getTelefon());
            stmt.setInt(5, p.getVarsta());
            stmt.setString(6, p.getAdresa());
            stmt.setString(7, p.getCnp().getValoare());
            stmt.executeUpdate();
            System.out.println("[DB] Pacient actualizat: " + p.getNumeComplet());
        } catch (SQLException e) {
            System.out.println("[DB] Eroare la actualizare pacient: " + e.getMessage());
        }
    }

    @Override
    public void delete(String cnp) {
        String sql = "DELETE FROM pacienti WHERE cnp=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cnp);
            stmt.executeUpdate();
            System.out.println("[DB] Pacient sters cu CNP: " + cnp);
        } catch (SQLException e) {
            System.out.println("[DB] Eroare la stergere pacient: " + e.getMessage());
        }
    }

    // ── JOIN 1: pacienti cu numarul de programari ──────────────
    public List<String> findPacientiCuNrProgramari() {
        List<String> rezultat = new ArrayList<>();
        String sql = """
                SELECT p.nume, p.prenume, COUNT(pr.id) as nr_programari
                FROM pacienti p
                LEFT JOIN programari pr ON p.cnp = pr.cnp_pacient
                GROUP BY p.cnp
                ORDER BY nr_programari DESC
                """;
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                rezultat.add(rs.getString("prenume") + " " + rs.getString("nume")
                        + " — programari: " + rs.getInt("nr_programari"));
            }
        } catch (SQLException e) {
            System.out.println("[DB] Eroare JOIN programari: " + e.getMessage());
        }
        return rezultat;
    }

    // ── Mapeaza un rand din ResultSet la obiect Pacient ────────
    private Pacient mapRow(ResultSet rs) throws SQLException {
        return new Pacient(
                rs.getString("nume"),
                rs.getString("prenume"),
                rs.getString("email"),
                rs.getString("telefon"),
                new CNP(rs.getString("cnp")),
                rs.getInt("varsta"),
                rs.getString("adresa")
        );
    }
}