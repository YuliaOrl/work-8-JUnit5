package guru.qa;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class WebTest {

    @ValueSource(strings = {"Ручная кладь", "Багаж", "Выбор места", "Страхование"})
    @ParameterizedTest(name = "На сайте присутствуют страницы с информацией по теме: \"{0}\"")
    void sitePobedaPageTest(String testData) {
        open("https://www.pobeda.aero/");
        $$(".offer_title").find(text(testData)).click();
        $(".title-inner").shouldHave(text(testData));
    }


    static Stream<Arguments> pobedaSiteMenuTest() {
        return Stream.of(
                Arguments.of("English", List.of("Hand baggage", "Baggage", "Seats", "Insurance", "Hotels", "Auto", "Transfer", "Excursions")),
                Arguments.of("Deutsch", List.of("Gepäck", "Sitzauswahl", "Flugversicherung")),
                Arguments.of("Italiano", List.of("Bagaglio", "Scelta del posto", "Assicurazione")),
                Arguments.of("Русский", List.of("Ручная кладь", "Багаж", "Выбор места", "Страхование", "Отели", "Авто", "Трансфер", "Экскурсии"))
        );
    }

    @MethodSource
    @ParameterizedTest(name = "Для локали {0} на сайте отображаются разделы {1}")
    void pobedaSiteMenuTest(String lang, List<String> expectedTitle) {
        open("https://www.pobeda.aero/");
        $("#idLanguageSelector").pressEnter();
        $$(".ui-dropdown_item_option_name").find(text(lang)).click();
        $$(".offer_title").filter(visible).shouldHave(CollectionCondition.texts(expectedTitle));
    }


    static Stream<Arguments> sitePobedaMenuEnumTest() {
        return Stream.of(
                Arguments.of(Lang.English, List.of("Online check-in", "Manage my booking", "Info/Rules")),
                Arguments.of(Lang.Deutsch, List.of("Buchung bearbeiten", "Online check-in", "Infos/Regeln")),
                Arguments.of(Lang.Italiano, List.of("Gestisci prenotazione", "Online check-in", "Info/Regole")),
                Arguments.of(Lang.Русский, List.of("Забронировать билет", "Управление бронированием", "Онлайн-регистрация", "Информация"))
        );
    }

    @MethodSource
    @ParameterizedTest(name = "Для локали {0} отображаются кнопки меню {1}")
    void sitePobedaMenuEnumTest(Lang lang, List<String> expectedButtons) {
        open("https://www.pobeda.aero/");
        $("#idLanguageSelector").pressEnter();
        $$(".ui-dropdown_item_option_name").find(text(lang.name())).click();
        $$(".button_label").filter(visible).shouldHave(CollectionCondition.texts(expectedButtons));
    }


    @CsvSource(value = {
            "вылет, Онлайн-табло",
            "возврат, Возврат и обмен авиабилетов",
            "выбор места, Выбор места",
            "способ оплаты, Способы оплаты"
    })

    @ParameterizedTest(name = "Результаты поиска содержат текст \"{1}\" для запроса \"{0}\"")
    void sitePobedaSearchTest(String testData, String expectedResult) {
        open("https://www.pobeda.aero/");
        $(".web-search_trigger_button").click();
        $("#webSearchId").setValue(testData).pressEnter();
        $$("li.searchresults_list_item").first().shouldHave(text(expectedResult));
    }


    @EnumSource(Lang.class)
    @ParameterizedTest(name = "Для локали {0} проверяется заполнение поля ввода Email для подписки")
    void pobedaSiteEmailTest(Lang lang) {
        open("https://www.pobeda.aero/");
        $("#idLanguageSelector").pressEnter();
        $$(".ui-dropdown_item_option_name").find(text(lang.name())).click();
        $(".newsletter_form_button").click();
        $(".newsletter_feedback_message").shouldBe(visible);
    }
}
