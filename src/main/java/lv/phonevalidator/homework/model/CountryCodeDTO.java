package lv.phonevalidator.homework.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = CountryCodeDTO.Builder.class)
public record CountryCodeDTO(
        String countryCode
) {

    private CountryCodeDTO(Builder builder) {
        this(builder.countryCode);
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static final class Builder {

        private String countryCode;

        public Builder countryCode(String countryCode) {
            this.countryCode = countryCode;
            return this;
        }

        public CountryCodeDTO build(){
            return new CountryCodeDTO(this);
        }
    }
}
