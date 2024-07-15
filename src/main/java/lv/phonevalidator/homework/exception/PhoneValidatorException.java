package lv.phonevalidator.homework.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class PhoneValidatorException extends ResponseStatusException {

    public PhoneValidatorException(String message) {
        super(HttpStatusCode.valueOf(400), message);
    }

    public PhoneValidatorException(String message, Throwable cause) {
        super(HttpStatusCode.valueOf(400), message, cause);
    }
}
