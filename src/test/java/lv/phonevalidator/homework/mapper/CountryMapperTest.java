package lv.phonevalidator.homework.mapper;

import lv.phonevalidator.homework.entity.CountryCodeEntity;
import lv.phonevalidator.homework.entity.CountryEntity;
import lv.phonevalidator.homework.model.CountryCodeDTO;
import lv.phonevalidator.homework.model.CountryDTO;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CountryMapperTest {
    private final CountryMapper countryMapper = new CountryMapper();
    private static final String LATVIA_COUNTRY_CODE = "+371";
    private static final String COUNTRY_NAME = "Latvia";

    @Test
    void testToCountryDTO() {
        //set up
        var source = createCountryEntity();
        var expected = createCountryDTO();

        //perform
        var actual = countryMapper.toCountryDTO(source);

        //verify
        assertThat(actual).isNotNull()
                .isEqualTo(expected);
    }

    private CountryDTO createCountryDTO() {
        return CountryDTO.builder()
                .name(COUNTRY_NAME)
                .codes(Set.of(createCountryCodeDTO()))
                .build();
    }

    private CountryCodeDTO createCountryCodeDTO() {
        return CountryCodeDTO.builder()
                .countryCode(LATVIA_COUNTRY_CODE)
                .build();
    }

    private CountryEntity createCountryEntity() {
        var entity = new CountryEntity();
        entity.setName(COUNTRY_NAME)
                .setCodes(Set.of(createCountryCodeEntity(entity)));
        return entity;
    }

    private CountryCodeEntity createCountryCodeEntity(CountryEntity countryEntity) {
        return new CountryCodeEntity()
                .setCountryCode(LATVIA_COUNTRY_CODE)
                .setCountry(countryEntity);
    }
}