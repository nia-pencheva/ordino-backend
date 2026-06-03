package com.ordino.domain.menus.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "menus_sections")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"menu", "menuSectionRecipes"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MenuSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "INT UNSIGNED")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private Integer position;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Instant updatedAt;

    @OneToMany(mappedBy = "menuSection")
    private List<MenuSectionRecipe> menuSectionRecipes;
}
