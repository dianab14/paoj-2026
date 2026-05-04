package com.pao.laboratory11.exercise1;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RiskScorer {

    public static int computeScore(BigDecimal amount, String country, String channel) {
        // amountScore: floor(amount/1000) * 10, fără cap
        int amountScore =
                amount.divide(new BigDecimal("1000"), 0, RoundingMode.DOWN).intValue() * 10;

        int countryScore = Rules.RISKY_COUNTRIES.contains(country) ? 20 : 0;

        int channelScore = switch (channel) {
            case "WEB"    -> 40;
            case "APP", "MOBILE" -> 20;
            case "ATM"    -> 5;
            case "POS"    -> 3;
            default       -> 0;
        };

        int bonus = 5; // bonusFraudPattern aplicat mereu

        return amountScore + countryScore + channelScore + bonus;
    }
}