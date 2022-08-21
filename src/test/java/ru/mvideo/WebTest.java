package ru.mvideo;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

@DisplayName("web tests")
public class WebTest {

    @BeforeAll
    static void configure() {
        Configuration.browserSize = "1920x1080";
        baseUrl = "https://www.mvideo.ru/";
    }

    @ValueSource(strings = {"Смартфон Apple iPhone 12", "Смартфон Apple iPhone 13"})
    @ParameterizedTest(name = "Результаты поиска не пустые для запроса {0}")
    void mvideoMainSearchIphoneTest(String testData) {
        open(baseUrl);
        $(".input__field").setValue(testData).pressEnter();
        $$(".ng-star-inserted").shouldBe(CollectionCondition.sizeGreaterThan(0));

    }

    @CsvFileSource(resources = "testData.csv")
    @ParameterizedTest(name = "Результаты поиска содержат текст {1} для запроса {0}")
    void mvideoMainSearchSamsungTest(String testData, String expectedResult) {
        open(baseUrl);
        $(".input__field").setValue(testData).pressEnter();
        $(".ng-star-inserted").shouldHave(text(expectedResult));
    }

    static Stream<Arguments> mvideoMainSearchLaptopTest() {
        return Stream.of(
                Arguments.of("Ноутбук msi", "MSI SWORD 15 A11UE-212XRU"),
                Arguments.of("Ноутбук asus", "ASUS R465JA-EB1262T"),
                Arguments.of("Ноутбук dell", "Dell G15 5515")
        );
    }

    @MethodSource
    @ParameterizedTest(name = "Для поиска по запросу {0} отображается {1}")
    void mvideoMainSearchLaptopTest(String manufacturer, String laptop) {
        open(baseUrl);
        $(".input__field").setValue(manufacturer).pressEnter();
        $(".plp__card-grid").shouldHave(text(laptop));
    }
}
