package model;

import java.time.LocalDateTime;

public class Account {
    private int accountId;
    private String email;
    private String passwordHash;
    private String roleId;
    private boolean status;
    private LocalDateTime createdAt;

    public Account() {
        this.roleId = "User";
        this.status = true;
        this.createdAt = LocalDateTime.now();
    }

    public Account(int accountId, String email, String passwordHash, String roleId, boolean status, LocalDateTime createdAt) {
        this.accountId = accountId;
        this.email = email;
        this.passwordHash = passwordHash;
        this.roleId = roleId;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getRoleId() { return roleId; }
    public void setRoleId(String roleId) { this.roleId = roleId; }

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
