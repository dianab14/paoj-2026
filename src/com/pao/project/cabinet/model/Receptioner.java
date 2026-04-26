package com.pao.project.cabinet.model;

public class Receptioner extends Persoana {
    private String program; // ex: "08:00-16:00"

    public Receptioner(String nume, String prenume, String email, String telefon, String program) {
        super(nume, prenume, email, telefon);
        this.program = program;
    }

    @Override
    public String getRol() { return "Receptioner"; }

    public String getProgram() { return program; }
    public void setProgram(String program) { this.program = program; }

    @Override
    public String toString() {
        return super.toString() + " | Program: " + program;
    }
}