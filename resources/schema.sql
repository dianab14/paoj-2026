DROP TABLE IF EXISTS consultatii;
DROP TABLE IF EXISTS programari;
DROP TABLE IF EXISTS medici;
DROP TABLE IF EXISTS pacienti;

CREATE TABLE pacienti (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    cnp TEXT NOT NULL UNIQUE,
    nume TEXT NOT NULL,
    prenume TEXT NOT NULL,
    email TEXT NOT NULL,
    telefon TEXT NOT NULL,
    varsta INTEGER NOT NULL,
    adresa TEXT NOT NULL
);

CREATE TABLE medici (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    cod_parafa TEXT NOT NULL UNIQUE,
    nume TEXT NOT NULL,
    prenume TEXT NOT NULL,
    email TEXT NOT NULL,
    telefon TEXT NOT NULL,
    specialitate TEXT NOT NULL,
    ani_experienta INTEGER NOT NULL
);

CREATE TABLE programari (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    cnp_pacient TEXT NOT NULL,
    cod_parafa_medic TEXT NOT NULL,
    data_ora TEXT NOT NULL,
    motiv TEXT NOT NULL,
    confirmata INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY (cnp_pacient) REFERENCES pacienti(cnp),
    FOREIGN KEY (cod_parafa_medic) REFERENCES medici(cod_parafa)
);

CREATE TABLE consultatii (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    cnp_pacient TEXT NOT NULL,
    cod_parafa_medic TEXT NOT NULL,
    data_ora TEXT NOT NULL,
    diagnostic TEXT NOT NULL,
    observatii TEXT,
    FOREIGN KEY (cnp_pacient) REFERENCES pacienti(cnp),
    FOREIGN KEY (cod_parafa_medic) REFERENCES medici(cod_parafa)
);