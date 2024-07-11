package lv.phonevalidator.homework.service;

import jakarta.transaction.Transactional;

import lv.phonevalidator.homework.entity.CountryCodeEntity;
import lv.phonevalidator.homework.entity.CountryEntity;
import lv.phonevalidator.homework.exception.PhoneValidatorException;
import lv.phonevalidator.homework.repository.CountryRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WikipediaReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(WikipediaReader.class);
    private static final String WIKI_PAGE = "https://en.wikipedia.org/wiki/List_of_country_calling_codes#Alphabetical_order";
    private static final String COUNTRY_CODES_TABLE = "table.wikitable.sortable.sticky-header-multi";

    private final CountryRepository countryRepository;

    public WikipediaReader(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Transactional(rollbackOn = PhoneValidatorException.class)
    public void populateDatabase() throws PhoneValidatorException {
        try {
            countryRepository.deleteAll();
            Document doc = Jsoup.connect(WIKI_PAGE).get();
            var table = doc.select(COUNTRY_CODES_TABLE).getFirst();
            LOGGER.info("Start reading content of country codes table {} and parsing it to populate database", COUNTRY_CODES_TABLE);
            var countries = parseCountryCodesTable(table);

            countryRepository.saveAll(countries);
        } catch (IOException ex) {
            throw new PhoneValidatorException("Error occurred while reading wiki page", ex);
        }
    }

    private static List<CountryEntity> parseCountryCodesTable(Element table) {
        return table.childNode(1).childNodes().stream()
                .filter(it -> it.nodeName().equals("tr"))
                .skip(2)
                .map(WikipediaReader::parseTableRow)
                .toList();
    }

    private static CountryEntity parseTableRow(Node tableRow) {
        var countryEntity = new CountryEntity();
        return countryEntity
                .setName(findCountry(tableRow))
                .setCodes(findCountryCodes(tableRow).stream()
                        .map(it -> new CountryCodeEntity()
                                .setCountryCode(it)
                                .setCountry(countryEntity)
                        )
                        .collect(Collectors.toSet())
                );
    }

    private static String findCountry(Node tableRow) {
        return Optional.of(tableRow)
                .map(row -> row.childNode(1))
                .map(Node::lastChild)
                .map(TextNode.class::cast)
                .map(TextNode::text)
                .map(String::trim)
                .orElseThrow(() -> new RuntimeException("Country name not found in the table row"));
    }

    private static List<String> findCountryCodes(Node tableRow) {
        return tableRow.childNode(3).childNodes()
                .stream()
                .filter(it -> "a".equals(it.nodeName()))
                .map(it -> it.childNode(0))
                .map(countryCode -> ((TextNode) countryCode).text())
                .map(countryCode -> countryCode.replaceAll("\\D+", ":"))
                .map(countryCode -> Arrays.stream(countryCode.split(":"))
                        .toList())
                .map(splitCountryCode -> {
                    if (splitCountryCode.size() == 1) {
                        return splitCountryCode;
                    }
                    String baseCode = splitCountryCode.getFirst();
                    return splitCountryCode.stream().skip(1)
                            .map(remainder -> baseCode + remainder)
                            .toList();
                })
                .flatMap(Collection::stream)
                .toList();
    }
}
