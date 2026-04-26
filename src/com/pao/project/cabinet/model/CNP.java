package com.pao.project.cabinet.model;

public final class CNP {
    private final String valoare;

    public CNP(String valoare) {
        if (valoare == null || !valoare.matches("\\d{13}")) {
            throw new IllegalArgumentException("CNP invalid: trebuie sa aiba exact 13 cifre.");
        }
        this.valoare = valoare;
    }

    public String getValoare() { return valoare; }

    @Override
    public String toString() { return valoare; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CNP)) return false;
        return valoare.equals(((CNP) o).valoare);
    }

    @Override
    public int hashCode() { return valoare.hashCode(); }
}