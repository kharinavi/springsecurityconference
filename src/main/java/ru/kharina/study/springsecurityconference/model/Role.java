package ru.kharina.study.springsecurityconference.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    VISITOR(Set.of(Permission.PERMISSION_MIN,Permission.PERMISSION_VISITOR)),
    SPEAKER(Set.of(Permission.PERMISSION_MIN,Permission.PERMISSION_SPEAKER)),
    ADMIN(Set.of(Permission.PERMISSION_MIN, Permission.PERMISSION_VISITOR,
            Permission.PERMISSION_SPEAKER, Permission.PERMISSION_ADMIN));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
