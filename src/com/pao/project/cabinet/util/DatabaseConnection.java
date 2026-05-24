package com.pao.project.cabinet.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            // Citeste db.properties
            Properties props = new Properties();
            InputStream is = getClass().getClassLoader()
                    .getResourceAsStream("db.properties");
            if (is == null) {
                throw new RuntimeException("Nu gasesc db.properties in resources/");
            }
            props.load(is);

            String url = props.getProperty("db.url");
            connection = DriverManager.getConnection(url);
            System.out.println("[DB] Conexiune SQLite stabilita.");

            // Creeaza tabelele daca nu exista
            initSchema();

        } catch (SQLException | IOException e) {
            throw new RuntimeException("Eroare la conectarea la baza de date: " + e.getMessage());
        }
    }

    private void initSchema() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS pacienti (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    cnp TEXT NOT NULL UNIQUE,
                    nume TEXT NOT NULL,
                    prenume TEXT NOT NULL,
                    email TEXT NOT NULL,
                    telefon TEXT NOT NULL,
                    varsta INTEGER NOT NULL,
                    adresa TEXT NOT NULL
                );
                CREATE TABLE IF NOT EXISTS medici (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    cod_parafa TEXT NOT NULL UNIQUE,
                    nume TEXT NOT NULL,
                    prenume TEXT NOT NULL,
                    email TEXT NOT NULL,
                    telefon TEXT NOT NULL,
                    specialitate TEXT NOT NULL,
                    ani_experienta INTEGER NOT NULL
                );
                CREATE TABLE IF NOT EXISTS programari (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    cnp_pacient TEXT NOT NULL,
                    cod_parafa_medic TEXT NOT NULL,
                    data_ora TEXT NOT NULL,
                    motiv TEXT NOT NULL,
                    confirmata INTEGER NOT NULL DEFAULT 0,
                    FOREIGN KEY (cnp_pacient) REFERENCES pacienti(cnp),
                    FOREIGN KEY (cod_parafa_medic) REFERENCES medici(cod_parafa)
                );
                CREATE TABLE IF NOT EXISTS consultatii (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    cnp_pacient TEXT NOT NULL,
                    cod_parafa_medic TEXT NOT NULL,
                    data_ora TEXT NOT NULL,
                    diagnostic TEXT NOT NULL,
                    observatii TEXT,
                    FOREIGN KEY (cnp_pacient) REFERENCES pacienti(cnp),
                    FOREIGN KEY (cod_parafa_medic) REFERENCES medici(cod_parafa)
                );
                """;

        // Executa fiecare CREATE TABLE separat
        for (String statement : sql.split(";")) {
            String trimmed = statement.trim();
            if (!trimmed.isEmpty()) {
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute(trimmed);
                }
            }
        }
        System.out.println("[DB] Schema initializata.");
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[DB] Conexiune inchisa.");
            }
        } catch (SQLException e) {
            System.out.println("[DB] Eroare la inchiderea conexiunii: " + e.getMessage());
        }
    }
}