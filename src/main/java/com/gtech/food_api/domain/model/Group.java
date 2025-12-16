package com.gtech.food_api.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;
import java.util.HashSet;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Group {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(name = "group_permission",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();

    public boolean addPermission(Permission permission) {
        return getPermissions().add(permission);
    }

    public boolean removePermission(Permission permission) {
        return getPermissions().remove(permission);
    }
}
