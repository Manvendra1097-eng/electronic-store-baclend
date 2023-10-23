package com.m2code.dtos;

public enum PAYMENT_STATUS {
    NOT_PAID, PAID;

    public static PAYMENT_STATUS getStatus(String status) {
        switch (status.toUpperCase()) {
            case "NOT_PAID":
                return PAYMENT_STATUS.NOT_PAID;
            case "PAID":
                return PAYMENT_STATUS.PAID;
            default:
                return PAYMENT_STATUS.NOT_PAID;
        }
    }
}
