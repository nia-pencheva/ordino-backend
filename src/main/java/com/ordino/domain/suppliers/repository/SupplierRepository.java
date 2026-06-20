package com.ordino.domain.suppliers.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ordino.domain.suppliers.model.entity.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);

    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumberAndIdNot(String phoneNumber, Long id);


    @Query("SELECT s FROM Supplier s WHERE (:active IS NULL OR s.active = :active)")
    Page<Supplier> findAllFiltered(@Param("active") Boolean active, Pageable pageable);

    @Query("SELECT s FROM Supplier s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')) AND (:active IS NULL OR s.active = :active)")
    Page<Supplier> searchByName(@Param("search") String search, @Param("active") Boolean active, Pageable pageable);

    @Query("SELECT s FROM Supplier s WHERE LOWER(s.address) LIKE LOWER(CONCAT('%', :search, '%')) AND (:active IS NULL OR s.active = :active)")
    Page<Supplier> searchByAddress(@Param("search") String search, @Param("active") Boolean active, Pageable pageable);

    @Query("SELECT s FROM Supplier s WHERE LOWER(s.email) LIKE LOWER(CONCAT('%', :search, '%')) AND (:active IS NULL OR s.active = :active)")
    Page<Supplier> searchByEmail(@Param("search") String search, @Param("active") Boolean active, Pageable pageable);

    @Query("SELECT s FROM Supplier s WHERE LOWER(s.phoneNumber) LIKE LOWER(CONCAT('%', :search, '%')) AND (:active IS NULL OR s.active = :active)")
    Page<Supplier> searchByPhoneNumber(@Param("search") String search, @Param("active") Boolean active, Pageable pageable);
}
