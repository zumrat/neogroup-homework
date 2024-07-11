package lv.phonevalidator.homework.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Set;

@JsonDeserialize(builder = CountryDTO.Builder.class)
public record CountryDTO(
        String name,
        Set<CountryCodeDTO> codes
) {

    private CountryDTO(Builder builder) {
        this(
                builder.name,
                builder.codes
        );
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static final class Builder {
        private String name;
        private Set<CountryCodeDTO> codes;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder codes(Set<CountryCodeDTO> codes) {
            this.codes = codes;
            return this;
        }

        public CountryDTO build() {
            return new CountryDTO(this);
        }
    }
}
