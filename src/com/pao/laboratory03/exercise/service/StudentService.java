package com.pao.laboratory03.exercise.service;

import com.pao.laboratory03.exercise.model.Student;
import com.pao.laboratory03.exercise.model.Subject;
import com.pao.laboratory03.exercise.exception.StudentNotFoundException;

import java.util.*;

public class StudentService {
    private static StudentService instance;
    private final List<Student> students = new ArrayList<>();

    private StudentService() {}

    public static StudentService getInstance() {
        if (instance == null) {
            instance = new StudentService();
        }
        return instance;
    }

    public void addStudent(String name, int age) {
        for (Student s : students) {
            if (s.getName().equalsIgnoreCase(name)) {
                throw new RuntimeException("Studentul cu numele " + name + " exista deja!");
            }
        }
        students.add(new Student(name, age));
    }

    public Student findByName(String name) {
        for (Student s : students) {
            if (s.getName().equalsIgnoreCase(name)) {
                return s;
            }
        }
        throw new StudentNotFoundException("Studentul " + name + " nu a fost gasit.");
    }

    public void addGrade(String studentName, Subject subject, double grade) {
        Student s = findByName(studentName);
        s.addGrade(subject, grade);
    }

    public void printAllStudents() {
        if (students.isEmpty()) {
            System.out.println("Nu exista studenti.");
            return;
        }
        int count = 1;
        for (Student s : students) {
            System.out.println(count++ + ". " + s);
            for (Map.Entry<Subject, Double> entry : s.getGrades().entrySet()) {
                System.out.println("   " + entry.getKey().name() + " = " + entry.getValue());
            }
        }
    }

    public void printTopStudents() {
        System.out.println("=== Top studenti ===");
        List<Student> sortedStudents = new ArrayList<>(students);

        Collections.sort(sortedStudents, new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                return Double.compare(s2.getAverage(), s1.getAverage());
            }
        });

        int count = 1;
        for (Student s : sortedStudents) {
            System.out.printf("%d. %s — media: %.2f%n", count++, s.getName(), s.getAverage());
        }
    }

    public Map<Subject, Double> getAveragePerSubject() {
        Map<Subject, Double> sums = new HashMap<>();
        Map<Subject, Integer> counts = new HashMap<>();

        for (Student s : students) {
            for (Map.Entry<Subject, Double> entry : s.getGrades().entrySet()) {
                Subject subj = entry.getKey();
                Double grade = entry.getValue();

                double currentSum = sums.getOrDefault(subj, 0.0);
                sums.put(subj, currentSum + grade);

                int currentCount = counts.getOrDefault(subj, 0);
                counts.put(subj, currentCount + 1);
            }
        }

        Map<Subject, Double> averages = new HashMap<>();
        for (Subject subj : sums.keySet()) {
            double average = sums.get(subj) / counts.get(subj);
            averages.put(subj, average);
        }
        
        return averages;
    }
}