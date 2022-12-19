package ru.kharina.study.springsecurityconference.model;

public enum Permission {
    PERMISSION_MIN("permission:min"),
    PERMISSION_VISITOR("permission:visitor"),
    PERMISSION_SPEAKER("permission:speaker"),
    PERMISSION_ADMIN("permission:admin");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
