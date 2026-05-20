package com.ordino.domain.users;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"users", "incompatibleRoles", "incompatibleWith"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "INT UNSIGNED")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String role;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    @ManyToMany
    @JoinTable(
        name = "roles_incompatibilities",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "incompatible_role_id")
    )
    private List<Role> incompatibleRoles;

    @ManyToMany(mappedBy = "incompatibleRoles")
    private List<Role> incompatibleWith;
}
