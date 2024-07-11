package lv.phonevalidator.homework.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "country_codes")
public class CountryCodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country_code", nullable = false, length = 10)
    private String countryCode;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private CountryEntity countryEntity;

    public Long getId() {
        return id;
    }

    public CountryCodeEntity setId(Long countryCodeId) {
        this.id = countryCodeId;
        return this;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public CountryCodeEntity setCountryCode(String codeValue) {
        this.countryCode = codeValue;
        return this;
    }

    public CountryEntity getCountry() {
        return countryEntity;
    }

    public CountryCodeEntity setCountry(CountryEntity countryEntity) {
        this.countryEntity = countryEntity;
        return this;
    }
}
