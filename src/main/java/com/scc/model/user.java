package com.scc.model;

public record user(int id, String name, String email, String password, String role, String department, String status) {
    @Override
    public String toString() {
        return String.format("[%d] %s (%s) - Status: %s", id, name, email, status);
    }
}
