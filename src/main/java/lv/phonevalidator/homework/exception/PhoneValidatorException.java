package lv.phonevalidator.homework.exception;

public class PhoneValidatorException extends Exception {

    public PhoneValidatorException(String message) {
        super(message);
    }

    public PhoneValidatorException(String message, Throwable cause) {
        super(message, cause);
    }
}
