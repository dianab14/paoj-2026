package com.pao.project.cabinet.repository;

import com.pao.project.cabinet.model.Medic;
import com.pao.project.cabinet.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MedicRepository implements Repository<Medic, String> {

    private final Connection connection;

    public MedicRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Medic m) {
        String sql = "INSERT INTO medici (cod_parafa, nume, prenume, email, telefon, specialitate, ani_experienta) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, m.getCodParafa());
            stmt.setString(2, m.getNume());
            stmt.setString(3, m.getPrenume());
            stmt.setString(4, m.getEmail());
            stmt.setString(5, m.getTelefon());
            stmt.setString(6, m.getSpecialitate());
            stmt.setInt(7, m.getAniExperienta());
            stmt.executeUpdate();
            System.out.println("[DB] Medic salvat: " + m.getNumeComplet());
        } catch (SQLException e) {
            System.out.println("[DB] Eroare la salvare medic: " + e.getMessage());
        }
    }

    @Override
    public Optional<Medic> findById(String codParafa) {
        String sql = "SELECT * FROM medici WHERE cod_parafa = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codParafa);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("[DB] Eroare la cautare medic: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Medic> findAll() {
        List<Medic> lista = new ArrayList<>();
        String sql = "SELECT * FROM medici ORDER BY nume";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("[DB] Eroare la listare medici: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void update(Medic m) {
        String sql = "UPDATE medici SET nume=?, prenume=?, email=?, telefon=?, specialitate=?, ani_experienta=? " +
                     "WHERE cod_parafa=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, m.getNume());
            stmt.setString(2, m.getPrenume());
            stmt.setString(3, m.getEmail());
            stmt.setString(4, m.getTelefon());
            stmt.setString(5, m.getSpecialitate());
            stmt.setInt(6, m.getAniExperienta());
            stmt.setString(7, m.getCodParafa());
            stmt.executeUpdate();
            System.out.println("[DB] Medic actualizat: " + m.getNumeComplet());
        } catch (SQLException e) {
            System.out.println("[DB] Eroare la actualizare medic: " + e.getMessage());
        }
    }

    @Override
    public void delete(String codParafa) {
        String sql = "DELETE FROM medici WHERE cod_parafa=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codParafa);
            stmt.executeUpdate();
            System.out.println("[DB] Medic sters cu parafa: " + codParafa);
        } catch (SQLException e) {
            System.out.println("[DB] Eroare la stergere medic: " + e.getMessage());
        }
    }

    // ── JOIN 2: medici cu numarul de consultatii ───────────────
    public List<String> findMediciCuNrConsultatii() {
        List<String> rezultat = new ArrayList<>();
        String sql = """
                SELECT m.nume, m.prenume, m.specialitate, COUNT(c.id) as nr_consultatii
                FROM medici m
                LEFT JOIN consultatii c ON m.cod_parafa = c.cod_parafa_medic
                GROUP BY m.cod_parafa
                ORDER BY nr_consultatii DESC
                """;
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                rezultat.add("Dr. " + rs.getString("prenume") + " " + rs.getString("nume")
                        + " (" + rs.getString("specialitate") + ")"
                        + " — consultatii: " + rs.getInt("nr_consultatii"));
            }
        } catch (SQLException e) {
            System.out.println("[DB] Eroare JOIN consultatii: " + e.getMessage());
        }
        return rezultat;
    }

    private Medic mapRow(ResultSet rs) throws SQLException {
        return new Medic(
                rs.getString("nume"),
                rs.getString("prenume"),
                rs.getString("email"),
                rs.getString("telefon"),
                rs.getString("cod_parafa"),
                rs.getString("specialitate"),
                rs.getInt("ani_experienta")
        );
    }
}