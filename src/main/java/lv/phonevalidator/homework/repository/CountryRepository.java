package lv.phonevalidator.homework.repository;

import lv.phonevalidator.homework.entity.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<CountryEntity, Long> {
}
