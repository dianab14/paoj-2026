package com.pao.laboratory11.exercise2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    static class Transaction {
        int id;
        BigDecimal amount;
        LocalDate date;
        String country;
        String channel;
        String account;

        Transaction(int id, BigDecimal amount, LocalDate date, String country, String channel, String account) {
            this.id = id;
            this.amount = amount;
            this.date = date;
            this.country = country;
            this.channel = channel;
            this.account = account;
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int N = Integer.parseInt(br.readLine());
        List<Transaction> txs = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            String[] p = br.readLine().split(" ");

            txs.add(new Transaction(
                    Integer.parseInt(p[0]),
                    new BigDecimal(p[1]),
                    LocalDate.parse(p[2]),
                    p[3],
                    p[4],
                    p[5]
            ));
        }

        int Q = Integer.parseInt(br.readLine());

        for (int i = 0; i < Q; i++) {
            String[] cmd = br.readLine().split(" ");

            switch (cmd[0]) {
                case "REPORT_MONTH" -> reportMonth(txs, cmd[1]);
                case "REPORT_ACCOUNT" -> reportAccount(txs, cmd[1]);
                case "TOP_CHANNELS" -> topChannels(txs, Integer.parseInt(cmd[1]));
            }
        }
    }

    static void reportMonth(List<Transaction> txs, String ymStr) {
        YearMonth ym = YearMonth.parse(ymStr);

        List<Transaction> filtered = txs.stream()
                .filter(t -> YearMonth.from(t.date).equals(ym))
                .toList();

        BigDecimal total = filtered.stream()
                .map(t -> t.amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.printf("MONTH %s total=%.2f count=%d\n",
                ym, total, filtered.size());
    }

    static void reportAccount(List<Transaction> txs, String account) {
        List<Transaction> filtered = txs.stream()
                .filter(t -> t.account.equals(account))
                .toList();

        BigDecimal total = filtered.stream()
                .map(t -> t.amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.printf("ACCOUNT %s total=%.2f count=%d\n",
                account, total, filtered.size());
    }

    static void topChannels(List<Transaction> txs, int k) {

        Map<String, Long> counts = txs.stream()
                .collect(Collectors.groupingBy(t -> t.channel, Collectors.counting()));

        List<Map.Entry<String, Long>> sorted = counts.entrySet().stream()
                .sorted((a, b) -> {
                    int cmp = Long.compare(b.getValue(), a.getValue());
                    if (cmp != 0) return cmp;
                    return a.getKey().compareTo(b.getKey());
                })
                .limit(k)
                .toList();

        for (var e : sorted) {
            System.out.println(e.getKey() + " " + e.getValue());
        }
    }
}
