package com.pao.project.cabinet.model;

public abstract class Persoana {
    private String nume;
    private String prenume;
    private String email;
    private String telefon;
    
    public Persoana (String nume, String prenume, String email, String telefon) {
        this.nume = nume;
        this.prenume = prenume;
        this.email = email;
        this.telefon = telefon;
    }

    public String getNume() {
        return nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setTelefon(String telefon){
        this.telefon = telefon;
    }

    public String getNumeComplet() {
        return nume + " " + prenume;
    }

    public abstract String getRol();

    @Override
    public String toString() {
        return "[" + getRol() + "] " + getNumeComplet() + " | email: " + email + " | tel: " + telefon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Persoana)) return false;
        Persoana p = (Persoana) o;
        return nume.equals(p.nume) && prenume.equals(p.prenume) && email.equals(p.email);
    }
 
    @Override
    public int hashCode() {
        return 31 * nume.hashCode() + prenume.hashCode() + email.hashCode();
    }

}
