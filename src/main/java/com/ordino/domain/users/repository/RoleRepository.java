package com.ordino.domain.users.repository;

import com.ordino.domain.users.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findByRoleIn(Collection<String> roleNames);

    @Query("SELECT r1.role, r2.role FROM Role r1 JOIN r1.incompatibleRoles r2 WHERE r1.role IN :roleNames AND r2.role IN :roleNames")
    List<Object[]> findIncompatiblePairs(Collection<String> roleNames);
}
