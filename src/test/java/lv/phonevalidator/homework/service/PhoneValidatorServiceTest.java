package lv.phonevalidator.homework.service;

import lv.phonevalidator.homework.entity.CountryCodeEntity;
import lv.phonevalidator.homework.entity.CountryEntity;
import lv.phonevalidator.homework.exception.PhoneValidatorException;
import lv.phonevalidator.homework.mapper.CountryMapper;
import lv.phonevalidator.homework.model.CountryCodeDTO;
import lv.phonevalidator.homework.model.CountryDTO;
import lv.phonevalidator.homework.repository.CountryCodeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PhoneValidatorServiceTest {

    @Mock
    private CountryMapper countryMapper;
    @Mock
    private CountryCodeRepository countryCodeRepository;

    @InjectMocks
    private PhoneValidatorService phoneValidatorService;

    @ParameterizedTest
    @MethodSource("createTestData")
    void testFindCountriesByPhone(@SuppressWarnings("unused") String title,
                                  String number,
                                  Set<CountryCodeEntity> expectedCountryCodes,
                                  CountryDTO countryDTO,
                                  List<CountryDTO> result) throws PhoneValidatorException {
        // Set up
        when(countryCodeRepository.findAllWhereStartWithCountryCode(anyString())).thenReturn(expectedCountryCodes);
        when(countryMapper.toCountryDTO(any())).thenReturn(countryDTO);

        // Perform
        var actual = phoneValidatorService.findCountriesByPhone(number);

        // Verify
        assertThat(actual)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(result);
    }

    private static Stream<Arguments> createTestData() {
        var isleOfMan = createCountryEntity("Isle of Man", 1L);
        var isleOfManCode1 = createCountryCodeEntity("441624", 1L, isleOfMan);
        var isleOfManCode2 = createCountryCodeEntity("447524", 2L, isleOfMan);
        var isleOfManCode3 = createCountryCodeEntity("447624", 3L, isleOfMan);
        var isleOfManCode4 = createCountryCodeEntity("447924", 4L, isleOfMan);
        isleOfMan.setCodes(Set.of(isleOfManCode1, isleOfManCode2, isleOfManCode3, isleOfManCode4));

        var latvia = createCountryEntity("Latvia", 2L);
        var latviaCode = createCountryCodeEntity("371", 5L, latvia);
        latvia.setCodes(Set.of(latviaCode));


        var latviaResult = createCountryDTO("Latvia",
                Set.of(CountryCodeDTO.builder()
                        .countryCode("371")
                        .build()));

        var isleOfManResult = createCountryDTO("Isle of Man",
                Set.of(CountryCodeDTO.builder()
                                .countryCode("441624")
                                .build(),
                        CountryCodeDTO.builder()
                                .countryCode("447524")
                                .build(),
                        CountryCodeDTO.builder()
                                .countryCode("447624")
                                .build(),
                        CountryCodeDTO.builder()
                                .countryCode("447924")
                                .build()
                )
        );

        return Stream.of(
                Arguments.of("should return Latvia",
                        "+37126270398",
                        Set.of(latviaCode),
                        latviaResult,
                        List.of(latviaResult)
                ),
                Arguments.of("should return US and Canada",
                        "+11165384765",
                        Set.of(isleOfManCode1, isleOfManCode2, isleOfManCode3, isleOfManCode4),
                        isleOfManResult,
                        List.of(isleOfManResult)
                )
        );
    }

    @Test
    void testFindCountriesByPhoneWhenMultipleCountriesFound() throws PhoneValidatorException {
        //Set up
        var number = "11165384765";
        var canada = createCountryEntity("Canada", 1L);
        var canadaCode = createCountryCodeEntity("1", 1L, canada);
        canada.setCodes(Set.of(canadaCode));

        var unitedStates = createCountryEntity("United States", 2L);
        var usCode = createCountryCodeEntity("1", 2L, unitedStates);
        unitedStates.setCodes(Set.of(usCode));

        var canadaResult = createCountryDTO("Canada",
                Set.of(CountryCodeDTO.builder()
                        .countryCode("1")
                        .build()));

        var usResult = createCountryDTO("United States",
                Set.of(CountryCodeDTO.builder()
                        .countryCode("1")
                        .build()));
        when(countryCodeRepository.findAllWhereStartWithCountryCode("11165384765")).thenReturn(Set.of(canadaCode, usCode));
        when(countryMapper.toCountryDTO(unitedStates)).thenReturn(usResult);
        when(countryMapper.toCountryDTO(canada)).thenReturn(canadaResult);

        //perform
        var actual = phoneValidatorService.findCountriesByPhone(number);

        //verify
        assertThat(actual).isNotNull()
                .containsExactlyInAnyOrder(usResult, canadaResult);
    }

    private static CountryCodeEntity createCountryCodeEntity(String countryCode, Long id, CountryEntity country) {
        return new CountryCodeEntity()
                .setId(id)
                .setCountryCode(countryCode)
                .setCountry(country);
    }

    private static CountryEntity createCountryEntity(String name, Long id) {
        return new CountryEntity()
                .setId(id)
                .setName(name);
    }

    private static CountryDTO createCountryDTO(String name, Set<CountryCodeDTO> countryCodes) {
        return CountryDTO.builder()
                .name(name)
                .codes(countryCodes)
                .build();
    }
}