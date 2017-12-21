package com.sylq.Model;

public enum Rank {
    User  (1),
    Admin (2);

    private final int code;

    Rank(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
