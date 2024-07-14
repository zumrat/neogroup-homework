package lv.phonevalidator.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lv.phonevalidator.homework.model.CountryCodeDTO;
import lv.phonevalidator.homework.model.CountryDTO;
import lv.phonevalidator.homework.service.PhoneValidatorService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PhoneValidatorController.class)
class PhoneValidatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private PhoneValidatorService phoneValidatorService;

    @ParameterizedTest
    @MethodSource("testData")
    void testFindCountryByPhoneNumber(String phoneNumber,
                                      List<CountryDTO> expectedCountries) throws Exception {

        when(phoneValidatorService.findCountriesByPhone(phoneNumber)).thenReturn(expectedCountries);

        var result = mockMvc.perform(get("/api/v1/phoneValidator/countries")
                        .param("number", phoneNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(result).isEqualTo(objectMapper.writeValueAsString(expectedCountries));
    }

    private static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(
                        "12423222931",
                        List.of(createCountry("Bahamas", Set.of(createCountryCode("1242"))))),
                Arguments.of(
                        "11165384765",
                        List.of(
                                createCountry("United States", Set.of(createCountryCode("1"))),
                                createCountry("Canada", Set.of(createCountryCode("1")))
                        )
                ),
                Arguments.of(
                        "71423423412",
                        List.of(createCountry("Russia", Set.of(createCountryCode("7"))))
                ),
                Arguments.of(
                        "77112227231",
                        List.of(createCountry("Kazakhstan",
                                        Set.of(
                                                createCountryCode("76"),
                                                createCountryCode("77"),
                                                createCountryCode("997")
                                        )
                                )
                        )
                ),
                Arguments.of(
                        "99912227231",
                        List.of()
                )
        );
    }

    private static CountryDTO createCountry(String name, Set<CountryCodeDTO> codes) {
        return CountryDTO.builder()
                .name(name)
                .codes(codes)
                .build();
    }

    private static CountryCodeDTO createCountryCode(String code) {
        return CountryCodeDTO.builder()
                .countryCode(code)
                .build();
    }
}
