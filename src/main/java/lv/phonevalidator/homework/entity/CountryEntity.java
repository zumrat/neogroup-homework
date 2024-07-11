package lv.phonevalidator.homework.entity;

import jakarta.persistence.*;

import java.util.Set;


@Entity
@Table(name = "countries")
public class CountryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @OneToMany(mappedBy = "countryEntity", cascade = CascadeType.ALL)
    private Set<CountryCodeEntity> codes;

    public Long getId() {
        return id;
    }

    public CountryEntity setId(Long countryId) {
        this.id = countryId;
        return this;
    }

    public String getName() {
        return name;
    }

    public CountryEntity setName(String name) {
        this.name = name;
        return this;
    }

    public Set<CountryCodeEntity> getCodes() {
        return codes;
    }

    public CountryEntity setCodes(Set<CountryCodeEntity> codes) {
        this.codes = codes;
        return this;
    }
}
