package lv.phonevalidator.homework.controller;

import lv.phonevalidator.homework.model.CountryDTO;
import lv.phonevalidator.homework.service.PhoneValidatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/phoneValidator")
public class PhoneValidatorController {

    private final PhoneValidatorService phoneValidatorService;

    public PhoneValidatorController(PhoneValidatorService phoneValidatorService) {
        this.phoneValidatorService = phoneValidatorService;
    }

    @GetMapping(path = "/countries")
    public ResponseEntity<List<CountryDTO>> findCountryByPhoneNumber(@RequestParam String number) {
        var result = phoneValidatorService.findCountriesByPhone(number);
        return ResponseEntity.ok(result);
    }
}
