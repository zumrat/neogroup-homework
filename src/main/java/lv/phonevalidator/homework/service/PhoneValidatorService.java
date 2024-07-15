package lv.phonevalidator.homework.service;

import lv.phonevalidator.homework.entity.CountryCodeEntity;
import lv.phonevalidator.homework.entity.CountryEntity;
import lv.phonevalidator.homework.exception.PhoneValidatorException;
import lv.phonevalidator.homework.mapper.CountryMapper;
import lv.phonevalidator.homework.model.CountryDTO;
import lv.phonevalidator.homework.repository.CountryCodeRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

@Service
public class PhoneValidatorService {

    private final CountryMapper countryMapper;
    private final CountryCodeRepository countryCodeRepository;

    public PhoneValidatorService(CountryMapper countryMapper, CountryCodeRepository countryCodeRepository) {
        this.countryMapper = countryMapper;
        this.countryCodeRepository = countryCodeRepository;
    }

    public List<CountryDTO> findCountriesByPhone(String number) throws PhoneValidatorException {
        var onlyDigits = number.replaceAll("\\D", "");
        if (StringUtils.isBlank(onlyDigits)) {
            throw new PhoneValidatorException("Invalid phone number provided");
        }

        var countryCodes = countryCodeRepository.findAllWhereStartWithCountryCode(onlyDigits);

        var minCodeLength = countryCodes.stream()
                .map(CountryCodeEntity::getCountryCode)
                .map(String::length)
                .max(Comparable::compareTo)
                .map(it -> Math.min(it, onlyDigits.length()))
                .orElse(0);

        return countryCodes.stream()
                .filter(it -> it.getCountryCode().length() >= minCodeLength)
                .map(CountryCodeEntity::getCountry)
                .filter(distinctByKey(CountryEntity::getId))
                .map(countryMapper::toCountryDTO)
                .toList();
    }

    private <T, K> Predicate<T> distinctByKey(Function<T, K> keyFn) {
        Set<K> seen = new HashSet<>();
        return el -> seen.add(keyFn.apply(el));
    }
}
