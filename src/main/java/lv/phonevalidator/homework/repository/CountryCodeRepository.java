package lv.phonevalidator.homework.repository;

import lv.phonevalidator.homework.entity.CountryCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface CountryCodeRepository extends JpaRepository<CountryCodeEntity, Long> {

    @Query("""
            select c from CountryCodeEntity c
            where :code like concat(c.countryCode, '%')
            or c.countryCode like :code%
            """)
    Collection<CountryCodeEntity> findAllWhereStartWithCountryCode(String code);
}
