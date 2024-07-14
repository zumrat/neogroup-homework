package lv.phonevalidator.homework.mapper;

import lv.phonevalidator.homework.entity.CountryCodeEntity;
import lv.phonevalidator.homework.entity.CountryEntity;
import lv.phonevalidator.homework.model.CountryCodeDTO;
import lv.phonevalidator.homework.model.CountryDTO;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CountryMapper {

    public CountryDTO toCountryDTO(CountryEntity countryEntity) {
        if (countryEntity == null) {
            return null;
        }
        var codes = countryEntity.getCodes().stream()
                .map(this::toCountryCodeDTO)
                .collect(Collectors.toSet());

        return CountryDTO.builder()
                .name(countryEntity.getName())
                .codes(codes)
                .build();
    }

    private CountryCodeDTO toCountryCodeDTO(CountryCodeEntity countryCodeEntity) {
        if (countryCodeEntity == null) {
            return null;
        }

        return CountryCodeDTO.builder()
                .countryCode(countryCodeEntity.getCountryCode())
                .build();
    }
}
