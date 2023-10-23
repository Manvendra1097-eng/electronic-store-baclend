package com.m2code.dtos;

public enum ORDER_STATUS {
    DELIVERED, DISPATCHED, PENDING;

    public static ORDER_STATUS getStatus(String status) {
        switch (status.toUpperCase()) {
            case "DELIVERED":
                return ORDER_STATUS.DELIVERED;
            case "DISPATCHED":
                return ORDER_STATUS.DISPATCHED;
            case "PENDING":
                return ORDER_STATUS.PENDING;
            default:
                return ORDER_STATUS.PENDING;
        }
    }
}
