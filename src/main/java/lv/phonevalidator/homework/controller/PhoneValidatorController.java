package lv.phonevalidator.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lv.phonevalidator.homework.exception.PhoneValidatorException;
import lv.phonevalidator.homework.model.CountryDTO;
import lv.phonevalidator.homework.service.PhoneValidatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("api/v1/phoneValidator")
public class PhoneValidatorController {

    private final PhoneValidatorService phoneValidatorService;

    public PhoneValidatorController(PhoneValidatorService phoneValidatorService) {
        this.phoneValidatorService = phoneValidatorService;
    }

    @GetMapping(path = "/countries")
    @Operation(summary = "Get countries by phone number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found country by phone number",
                    content = { @Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CountryDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid phone number provided",
                    content = @Content) })
    public ResponseEntity<List<CountryDTO>> findCountryByPhoneNumber(@RequestParam String number) throws PhoneValidatorException {
        var result = phoneValidatorService.findCountriesByPhone(number);
        return ResponseEntity.ok(result);
    }
}
