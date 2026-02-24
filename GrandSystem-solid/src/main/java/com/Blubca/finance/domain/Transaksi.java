package com.Blubca.finance.domain;

/**
 * ENTITY / DOMAIN LAYER
 * Record = cara modern Java untuk bikin "data class" imutable.
 * Otomatis punya: constructor, getter (id(), customerName(), dll), toString(), equals().
 * Layer ini TIDAK BOLEH mengandung logika bisnis atau database.
 */
public record Transaksi(int id, String customerName, double amount, String status) {}
