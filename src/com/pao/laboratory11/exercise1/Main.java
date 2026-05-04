package com.pao.laboratory11.exercise1;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    static String formatShort(Transaction tx) {
        return String.format("[%d] %s score=%d", tx.getId(), tx.verdict(), tx.getScore());
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = Integer.parseInt(sc.nextLine().trim());
        List<Transaction> all = new ArrayList<>(n);
        Map<Integer, Transaction> byId = new HashMap<>();

        for (int i = 0; i < n; i++) {
            String[] p = sc.nextLine().trim().split(" ");
            int id         = Integer.parseInt(p[0]);
            BigDecimal amt = new BigDecimal(p[1]);
            LocalDate date = LocalDate.parse(p[2]);
            String country = p[3];
            String channel = p[4];

            Transaction tx = new Transaction(id, amt, date, country, channel);
            all.add(tx);
            byId.put(id, tx);
        }

        List<Transaction> flagged = all.stream()
                .filter(Transaction::isFlagged)
                .sorted(TransactionComparator.INSTANCE)
                .collect(Collectors.toList());

        List<Transaction> sorted = all.stream()
                .sorted(TransactionComparator.INSTANCE)
                .collect(Collectors.toList());

        int q = Integer.parseInt(sc.nextLine().trim());
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < q; i++) {
            String linie = sc.nextLine().trim();

            if (linie.startsWith("CHECK ")) {
                int id = Integer.parseInt(linie.substring(6).trim());
                Transaction tx = byId.get(id);
                if (tx == null)
                    sb.append("CHECK ").append(id).append(" => NOT_FOUND\n");
                else
                    sb.append("CHECK ").append(id)
                      .append(" => ").append(tx.verdict())
                      .append(" score=").append(tx.getScore()).append("\n");

            } else if (linie.equals("LIST_FLAGGED")) {
                if (flagged.isEmpty()) {
                    sb.append("NONE\n");
                } else {
                    for (Transaction tx : flagged)
                        sb.append(formatShort(tx)).append("\n");
                }

            } else if (linie.startsWith("TOP_RISK ")) {
                int k = Integer.parseInt(linie.substring(9).trim());
                int limit = Math.min(k, sorted.size());
                for (int j = 0; j < limit; j++)
                    sb.append(formatShort(sorted.get(j))).append("\n");

            } else {
                sb.append("ERR UNKNOWN_COMMAND\n");
            }
        }

        System.out.print(sb);
    }
}