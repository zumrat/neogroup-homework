package lv.phonevalidator.homework.service;


import lv.phonevalidator.homework.exception.PhoneValidatorException;
import lv.phonevalidator.homework.repository.CountryRepository;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WikipediaReaderTest {

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private WikipediaReader wikipediaReader;

    private Document document;

    @BeforeEach
    void setUp() {
        String html = """
                <table class="wikitable sortable sticky-header-multi">
                    <tbody>
                        <tr><th>Country</th><th>Codes</th></tr>
                        <tr><td>CountryName</td><td><a href="/wiki/Telephone_numbers_in_CountryName">+123</a></td></tr>
                    </tbody>
                </table>
                """;
        document = Jsoup.parse(html, "https://en.wikipedia.org");
    }

    @Test
    void testPopulateDatabase() throws Exception {
        try (var jsoupMockedStatic = mockStatic(Jsoup.class)) {
            Connection connection = mock(Connection.class);
            jsoupMockedStatic.when(() -> Jsoup.connect(anyString()))
                    .thenReturn(connection);
            when(connection.get())
                    .thenReturn(document);

            wikipediaReader.populateDatabase();

            verify(countryRepository, times(1)).deleteAll();
            verify(countryRepository, times(1)).saveAll(any());
        }
    }

    @Test
    void testPopulateDatabaseThrowsException() throws IOException {
        try (var jsoupMockedStatic = mockStatic(Jsoup.class)) {
            Connection mockConnection = mock(Connection.class);
            jsoupMockedStatic.when(() -> Jsoup.connect(anyString())).thenReturn(mockConnection);
            when(mockConnection.get()).thenThrow(new IOException());

            assertThrows(PhoneValidatorException.class, () -> wikipediaReader.populateDatabase());

            verify(countryRepository, times(1)).deleteAll();
        }
    }
}