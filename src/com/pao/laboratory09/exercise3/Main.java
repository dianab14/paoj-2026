package com.pao.laboratory09.exercise3;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        CoadaTranzactii coada = new CoadaTranzactii();
        ProcessorThread processorThread = new ProcessorThread(coada);

        // 1. Creează cei 3 producători
        ATMThread atm1 = new ATMThread(1, coada);
        ATMThread atm2 = new ATMThread(2, coada);
        ATMThread atm3 = new ATMThread(3, coada);

        // 2. Pornește producătorii
        atm1.start();
        atm2.start();
        atm3.start();

        // 3. Pornește consumatorul
        Thread processorFir = new Thread(processorThread);
        processorFir.start();

        // 4. Așteaptă terminarea tuturor ATM-urilor
        atm1.join();
        atm2.join();
        atm3.join();

        // 5. Oprește consumatorul și trezește-l din eventual wait()
        processorThread.activ = false;
        synchronized (coada) {
            coada.notifyAll();
        }

        // 6. Așteaptă terminarea consumatorului
        processorFir.join();

        // 7. Afișează totalul
        System.out.println("Toate tranzactiile procesate. Total: " + processorThread.getTotalProcesate());
    }
}