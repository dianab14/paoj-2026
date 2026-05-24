package com.pao.project.cabinet.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantLock;

public class AuditService {
    private static AuditService instance;
    private static final String AUDIT_FILE = "audit.csv";
    private final ReentrantLock lock = new ReentrantLock();

    private AuditService() {
        // Scriem headerul doar daca fisierul e nou
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(AUDIT_FILE, true))) {
            // append=true, nu suprascrie la fiecare rulare
        } catch (IOException e) {
            System.out.println("[AUDIT] Eroare la initializare: " + e.getMessage());
        }
    }

    public static AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    // Metoda thread-safe de logare
    public void log(String numeActiune) {
        lock.lock();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(AUDIT_FILE, true))) {
            writer.write(numeActiune + "," + LocalDateTime.now());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("[AUDIT] Eroare la scriere: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }
}