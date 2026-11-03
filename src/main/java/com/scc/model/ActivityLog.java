package com.scc.model;

public record ActivityLog(
        int id,
        int userId,
        String actionType,
        String description,
        String timestamp
) {
    @Override
    public String toString() {
        return String.format("[%d] User ID: %d | Type: %s | %s | Time: %s",
                id, userId, actionType, description, timestamp);
    }
}