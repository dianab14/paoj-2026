package com.pao.project.cabinet.repository;

import com.pao.project.cabinet.model.Consultatie;
import com.pao.project.cabinet.model.Medic;
import com.pao.project.cabinet.model.Pacient;
import com.pao.project.cabinet.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConsultatieRepository implements Repository<Consultatie, Integer> {

    private final Connection connection;

    public ConsultatieRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Consultatie c) {
        String sql = "INSERT INTO consultatii (cnp_pacient, cod_parafa_medic, data_ora, diagnostic, observatii) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, c.getPacient().getCnp().getValoare());
            stmt.setString(2, c.getMedic().getCodParafa());
            stmt.setString(3, c.getDataOra().toString());
            stmt.setString(4, c.getDiagnostic());
            stmt.setString(5, c.getObservatii());
            stmt.executeUpdate();
            System.out.println("[DB] Consultatie salvata: #" + c.getId());
        } catch (SQLException e) {
            System.out.println("[DB] Eroare la salvare consultatie: " + e.getMessage());
        }
    }

    @Override
    public Optional<Consultatie> findById(Integer id) {
        String sql = "SELECT * FROM consultatii WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("[DB] Eroare la cautare consultatie: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Consultatie> findAll() {
        List<Consultatie> lista = new ArrayList<>();
        String sql = "SELECT * FROM consultatii ORDER BY data_ora DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("[DB] Eroare la listare consultatii: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void update(Consultatie c) {
        String sql = "UPDATE consultatii SET diagnostic=?, observatii=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, c.getDiagnostic());
            stmt.setString(2, c.getObservatii());
            stmt.setInt(3, c.getId());
            stmt.executeUpdate();
            System.out.println("[DB] Consultatie actualizata: #" + c.getId());
        } catch (SQLException e) {
            System.out.println("[DB] Eroare la actualizare consultatie: " + e.getMessage());
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM consultatii WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("[DB] Consultatie stearsa: #" + id);
        } catch (SQLException e) {
            System.out.println("[DB] Eroare la stergere consultatie: " + e.getMessage());
        }
    }

    private Consultatie mapRow(ResultSet rs) throws SQLException {
        Pacient pacient = new Pacient(
                rs.getString("cnp_pacient"), "", "", "",
                new com.pao.project.cabinet.model.CNP(rs.getString("cnp_pacient")),
                0, "");
        Medic medic = new Medic(
                rs.getString("cod_parafa_medic"), "", "", "",
                rs.getString("cod_parafa_medic"), "", 0);

        return new Consultatie(
                pacient, medic,
                LocalDateTime.parse(rs.getString("data_ora")),
                rs.getString("diagnostic"),
                rs.getString("observatii"));
    }
}