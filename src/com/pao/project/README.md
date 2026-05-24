# Cabinet Medical — Definirea Sistemului (Etapa I)

## 1.1 — Cele 10 acțiuni / interogări posibile în sistem

1. **Înregistrează un pacient nou** — adaugă un pacient în sistem pe baza datelor personale și a CNP-ului unic
2. **Caută un pacient după CNP** — returnează datele complete ale pacientului sau aruncă `PacientNegasitException`
3. **Caută pacienți după nume** — returnează lista tuturor pacienților al căror nume / prenume conține șirul dat
4. **Listează toți pacienții sortați alfabetic** — afișează pacienții ordonați după nume folosind `TreeSet<Pacient>`
5. **Șterge un pacient din sistem** — elimină pacientul identificat prin CNP
6. **Listează medicii după specialitate** — afișează toți medicii dintr-o specialitate dată, folosind `Map<String, List<Medic>>`
7. **Programează un pacient la un medic** — creează o programare validând conflictele de orar; aruncă `ProgramareConflictException` dacă medicul are deja o programare la acea oră
8. **Confirmă o programare** — marchează programarea ca „confirmată" pe baza ID-ului
9. **Înregistrează o consultație** — salvează diagnosticul și observațiile, actualizează automat istoricul medical al pacientului; opțional emite o rețetă
10. **Afișează istoricul medical al unui pacient** — listează toate consultațiile trecute ale unui pacient identificat prin CNP

---

## 1.2 — Cele 8 tipuri de obiecte din domeniu

| Clasă         | Rol                                                                             |
|---------------|---------------------------------------------------------------------------------|
| `Persoana`    | Clasă abstractă de bază; metodă abstractă `getRol()`                            |
| `Pacient`     | Extinde `Persoana`; are CNP, vârstă, adresă, istoric medical                    |
| `Medic`       | Extinde `Persoana`; are cod parafă, specialitate, ani experiență                |
| `Specialist`  | Extinde `Medic`; are titlu academic și spital afiliat (al 2-lea nivel ierarhie) |
| `Receptioner` | Extinde `Persoana`; are program de lucru                                        |
| `Programare`  | Asociere Pacient–Medic la o dată/oră, cu motiv și stare confirmare              |
| `Consultatie` | Rezultatul consultului medical (diagnostic, observații, rețetă opțională)       |
| `Reteta`      | Emisa în urma unei consultații, conține lista de `Medicament`                   |
| `Medicament`  | Denumire, dozaj, producător                                                     |
| `CNP`         | Clasă **imutabilă** — identificator unic validat (exact 13 cifre)               |

---

## Structura proiectului

```
src/com/pao/project/cabinet/
├── Main.java
├── model/
│   ├── Persoana.java        ← clasă abstractă (getRol())
│   ├── Pacient.java         ← extends Persoana, implements Comparable<Pacient>
│   ├── Medic.java           ← extends Persoana
│   ├── Specialist.java      ← extends Medic  (nivel 2 de moștenire)
│   ├── Receptioner.java     ← extends Persoana
│   ├── Programare.java
│   ├── Consultatie.java
│   ├── Reteta.java
│   ├── Medicament.java
│   └── CNP.java             ← clasă imutabilă (final, fără setteri)
├── service/
│   ├── Pacientservice.java  ← Singleton; gestionează pacienti
│   └── Medicservice.java    ← Singleton; gestionează medici, programări, consultații
└── exception/
    ├── Pacientnegasitexception.java
    └── Programareconflictexception.java
```
javac -cp "lib\sqlite-jdbc-3.53.1.0.jar;lib\java-diff-utils-4.15.jar" -d out (Get-ChildItem -Recurse -Filter "*.java" -Path src\com\pao\project | Select-Object -ExpandProperty FullName)