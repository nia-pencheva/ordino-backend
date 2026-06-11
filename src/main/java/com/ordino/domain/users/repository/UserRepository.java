package com.ordino.domain.users.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ordino.domain.users.model.entity.Role;
import com.ordino.domain.users.model.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.username = :username AND u.deletedAt IS NULL")
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.deletedAt IS NULL")
    List<User> findAllWithRoles();

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.id = :id AND u.deletedAt IS NULL")
    Optional<User> findByIdWithRoles(Long id);

    @Query("SELECT DISTINCT r FROM User u JOIN u.roles r WHERE u.deletedAt IS NULL")
    List<Role> findAllDistinctRoles();

    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByUsername(String username);
    boolean existsByUsernameAndIdNot(String username, Long id);

    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumberAndIdNot(String phoneNumber, Long id);

    @Query(
        value = """
            SELECT DISTINCT u FROM User u
            JOIN FETCH u.roles
            WHERE u.deletedAt IS NULL
            AND (:roleId IS NULL OR u.id IN (
                SELECT u2.id FROM User u2 JOIN u2.roles r WHERE r.id = :roleId
            ))
            ORDER BY u.id
        """,
        countQuery = """
            SELECT COUNT(DISTINCT u) FROM User u
            WHERE u.deletedAt IS NULL
            AND (:roleId IS NULL OR u.id IN (
                SELECT u2.id FROM User u2 JOIN u2.roles r WHERE r.id = :roleId
            ))
        """
    )
    Page<User> findAllWithRolesPaginated(@Param("roleId") Long roleId, Pageable pageable);

    @Query(
        value = """
            SELECT DISTINCT u FROM User u
            JOIN FETCH u.roles
            WHERE u.deletedAt IS NULL
            AND (:roleId IS NULL OR u.id IN (
                SELECT u2.id FROM User u2 JOIN u2.roles r WHERE r.id = :roleId
            ))
            AND (
                LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(u.phoneNumber) LIKE LOWER(CONCAT('%', :search, '%'))
            )
            ORDER BY
                CASE WHEN LOWER(u.fullName) = LOWER(:search) THEN 1
                     WHEN LOWER(u.fullName) LIKE LOWER(CONCAT(:search, '%')) THEN 2
                     WHEN LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%')) THEN 3
                     WHEN LOWER(u.username) = LOWER(:search) THEN 4
                     WHEN LOWER(u.username) LIKE LOWER(CONCAT(:search, '%')) THEN 5
                     WHEN LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) THEN 6
                     WHEN LOWER(u.email) = LOWER(:search) THEN 7
                     WHEN LOWER(u.email) LIKE LOWER(CONCAT(:search, '%')) THEN 8
                     WHEN LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) THEN 9
                     WHEN LOWER(u.phoneNumber) = LOWER(:search) THEN 10
                     WHEN LOWER(u.phoneNumber) LIKE LOWER(CONCAT(:search, '%')) THEN 11
                     ELSE 12
                END, u.id
        """,
        countQuery = """
            SELECT COUNT(DISTINCT u) FROM User u
            WHERE u.deletedAt IS NULL
            AND (:roleId IS NULL OR u.id IN (
                SELECT u2.id FROM User u2 JOIN u2.roles r WHERE r.id = :roleId
            ))
            AND (
                LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(u.phoneNumber) LIKE LOWER(CONCAT('%', :search, '%'))
            )
        """
    )
    Page<User> searchUsers(@Param("search") String search, @Param("roleId") Long roleId, Pageable pageable);
}
