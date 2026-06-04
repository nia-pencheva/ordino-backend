package com.ordino.domain.units.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ordino.domain.units.model.entity.Unit;

public interface UnitRepository extends JpaRepository<Unit, Long> {

    boolean existsByUnit(String unit);
    boolean existsByUnitAndIdNot(String unit, Long id);

    boolean existsByAbbreviation(String abbreviation);
    boolean existsByAbbreviationAndIdNot(String abbreviation, Long id);
}
