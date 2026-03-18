# Laboratory 02 — Moștenire, Clase abstracte, Interfețe, Colecții

## Demo-uri (citește și rulează)

| Pachet | Ce demonstrează |
|--------|-----------------|
| `abstractclasses/` | Clasă abstractă, moștenire, polimorfism, upcasting |
| `equalshashcode/` | `==` vs `.equals()`, `hashCode`, comportament `HashSet` |
| `immutable/` | Clasă imutabilă: `final class`, `final` fields, fără setteri |
| `strings/` | `String` vs `StringBuilder` vs `StringBuffer` + benchmark |
| `collections/` | `ArrayList`, `HashSet`, `TreeSet` — operații și parcurgere |

---

## Exerciții

| # | Exercițiu | Dificultate | Fișiere de completat |
|---|-----------|-------------|----------------------|
| 1 | Forme geometrice | ⭐ ~20 min | `exercise1/Circle.java`, `Rectangle.java` |
| 2 | equals/hashCode Student | ⭐ ~15 min | `exercise2/Student.java` |
| 3 | Angajați + ArrayList | ⭐⭐ ~25 min | `exercise3/model/Programator.java`, `Manager.java`, `service/AngajatService.java` |
| 4 | Zoo (bonus) | ⭐⭐ ~30 min | `exercise4/model/Dog,Cat,Parrot.java`, `service/ZooService.java` |

---

### Exercițiul 1 — Forme geometrice ⭐

Implementează `Circle` și `Rectangle` care extind `Shape`.

**Fișiere:** `exercise1/Circle.java`, `exercise1/Rectangle.java`
**Model:** `exercise1/Shape.java` (dat)
**Test:** `exercise1/Main.java` (nu modifica)

**Output așteptat:**
```
=== Forme geometrice ===
Circle [area=78.54, perimeter=31.42]
Rectangle [area=24.00, perimeter=20.00]
Circle [area=12.57, perimeter=12.57]
Rectangle [area=25.00, perimeter=20.00]

=== Polimorfism — aceeași metodă, comportament diferit ===
Circle     → aria = 78.54
Rectangle  → aria = 24.00
Circle     → aria = 12.57
Rectangle  → aria = 25.00

=== Suma ariilor tuturor formelor ===
Total arii: 140.10
```

---

### Exercițiul 2 — equals/hashCode Student ⭐

Adaugă `equals(Object o)` și `hashCode()` în `Student` — doi studenți sunt egali dacă au **același id**.

**Fișier:** `exercise2/Student.java`
**Model:** `equalshashcode/Book.java` (dat)
**Test:** `exercise2/Main.java` (nu modifica)

**Output așteptat:**
```
=== VERIFICARE ===
Test 1 (equals):   PASSED ✓
Test 2 (hashCode):  PASSED ✓
Test 3 (HashSet):   PASSED ✓
```

---

### Exercițiul 3 — Angajați + ArrayList ⭐⭐

Implementează `Programator` și `Manager` (extind `Angajat`) + completează `AngajatService`.

**Fișiere:** `exercise3/model/Programator.java`, `Manager.java`, `service/AngajatService.java`
**Model:** `exercise3/model/Angajat.java` (dat)
**Test:** `exercise3/Main.java` (nu modifica)

**Formule:**
- `Programator.salariuTotal()` = `getSalariuBaza() * 1.5`
- `Manager.salariuTotal()` = `getSalariuBaza() * 2 + nrSubordonati * 100`

**Output așteptat:**
```
=== Adăugare angajați ===
Angajat adăugat: Ana
Angajat adăugat: Mihai
Angajat adăugat: Ion
Angajat adăugat: Elena

=== Lista angajaților ===
1. Programator{name='Ana', salariuBaza=5000.0, salariuTotal=7500.00}
2. Programator{name='Mihai', salariuBaza=4500.0, salariuTotal=6750.00}
3. Manager{name='Ion', salariuBaza=6000.0, salariuTotal=13000.00}
4. Manager{name='Elena', salariuBaza=7000.0, salariuTotal=14500.00}

=== Total salarii ===
Suma salariilor: 41750.00 RON

=== VERIFICARE ===
Test 1 (Programator): PASSED ✓
Test 2 (Manager):     PASSED ✓
Test 3 (Total):       PASSED ✓
```

---

### Exercițiul 4 — Grădina Zoologică ⭐⭐ (bonus)

Implementează `Dog`, `Cat`, `Parrot` (extind `Animal`) + completează `ZooService` (Singleton cu 4 metode).

**Fișiere:** `exercise4/model/Dog.java`, `Cat.java`, `Parrot.java`, `service/ZooService.java`
**Model:** `exercise4/model/Animal.java`, `Describable.java` (date)
**Test:** `exercise4/Main.java` — meniu interactiv (nu modifica)

**Exemplu interacțiune:**
```
Alege opțiunea: 2
Nume: Rex
Vârsta: 5
Adăugat: Dog{name='Rex', age=5}

Alege opțiunea: 1
  1. Rex (varsta: 5 ani) face: Ham!
```

---

## Cheat Sheet

| Concept | Reține |
|---------|--------|
| `abstract class` | Nu se instanțiază; metode abstracte + concrete |
| `interface` | Contract pur; o clasă poate implementa mai multe |
| `extends` + `super` | Moștenire de la 1 clasă; `super(args)` = constructor părinte |
| `@Override` | Subclasa redefinește comportamentul |
| Polimorfism | Variabilă tip-părinte → comportament tip-real |
| `equals`/`hashCode` | Suprascrie ambele; obligatoriu pt `HashSet`/`HashMap` |
| Clasă imutabilă | `final class`, `final` fields, fără setteri |
| `StringBuilder` | Folosește-l pt concatenări repetate |
| `ArrayList` | Listă dinamică cu index — înlocuiește array-uri |
| `HashSet` | Fără duplicate — necesită `equals`/`hashCode` |
| `TreeSet` | Fără duplicate + sortat automat |

---

## Ce urmează la Laboratory 03?
- `Map` (`HashMap`, `TreeMap`)
- `Comparable` cu `Collections.sort`
- Enum-uri
- Introducere excepții
