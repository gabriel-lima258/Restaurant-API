package com.gtech.food_api.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class User {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @CreationTimestamp
    @Column(nullable = false, updatable = false, columnDefinition = "datetime")
    private OffsetDateTime createdAt;

    @ManyToMany
    @JoinTable(name = "group_user",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "group_id"))
    private Set<Group> groups = new HashSet<>();

    @OneToMany(mappedBy = "client")
    private List<Order> orders = new ArrayList<>();

    public boolean correctPassword(String currentPassword, String newPassword) {
        if (this.password.equals(currentPassword)) {
            this.password = newPassword;
            return true;
        }
        return false;
    }

    public boolean addGroup(Group group) {
        return getGroups().add(group);
    }

    public boolean removeGroup(Group group) {
        return getGroups().remove(group);
    }

    public boolean isNewUser() {
        return getId() == null;
    }
}
