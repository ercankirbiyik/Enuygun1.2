package step;

import base.BaseTest;
import methods.Methods;
import com.thoughtworks.gauge.Step;
import model.ElementInfos;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseSteps extends BaseTest {


    public BaseSteps() throws IOException {
        String currentWorkingDir = System.getProperty("user.dir");
        initMap(getFileList(currentWorkingDir + "/src"));
        //initMap(getFileList());
    }

    Methods methods = new Methods();


    @Step({"Go to <url> address",
            "<url> adresine git"})
    public void goToUrl(String url) {
        driver.get(url);
        logger.info(url + " adresine gidiliyor.");
    }

    @Step({"Wait <value> milliseconds",
            "<long> milisaniye bekle"})
    public void waitByMilliSeconds(long milliseconds) {
        try {
            logger.info(milliseconds + " milisaniye bekleniyor.");
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Step({"Wait <value> seconds",
            "<value> saniye bekle"})
    public void waitBySeconds(int value) {
        try {
            logger.info(value + " saniye bekleniyor.");
            Thread.sleep(value * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Step({"Click to element <key>",
            "<key> elementine tıkla"})
    public void clicktoElement(String key) {
        if (!key.isEmpty()) {
            //hoverElement(findElement(key));
            waitByMilliSeconds(methods.DEFAULT_MILLISECOND_WAIT_AMOUNT);
            methods.clickElement(methods.findElement(key));
            logger.info(key + " elementine tıklandı.");
        }
    }


    @Step({"<key> elementini bul, temizle ve <text> değerini yaz",
            "Find element by <key> clear and send keys <text>"})
    public void sendKeys(String key, String text) {
        if (!key.equals("")) {
            methods.findElement(key).clear();
            methods.findElement(key).sendKeys(text);
            logger.info(key + " elementine '" + text + "' texti yazıldı.");
        }
    }


    @Step({"<action> eylemini <key> elementine gönder",
            "Send <action> action to <key> element"})
    public void sendAction(String action, String key) {
        WebElement element = methods.findElement(key);
        if (element != null) {
            switch (action.toLowerCase()) {
                case "enter":
                    element.sendKeys(Keys.ENTER);
                    break;
                case "tab":
                    element.sendKeys(Keys.TAB);
                    break;
                case "delete":
                    element.sendKeys(Keys.DELETE);
                    break;
                case "clear":
                    element.clear();
                    break;
                case "cancel":
                    element.sendKeys(Keys.CANCEL);
                    break;
                case "esc":
                case "escape":
                    element.sendKeys(Keys.ESCAPE);
                    break;
                default:
                    Assertions.fail("Geçersiz eylem: " + action);
            }
            logger.info(key + " elementine " + action + " eylemi gönderildi.");
        } else {
            Assertions.fail(key + " elementi bulunamadı.");
        }
    }


    @Step("<key> checkbox tıklandıysa tıkla, tıklanmadıysa hiçbir şey yapma")
    public void clickCheckboxIfClicked(String key) {
        WebElement checkbox = methods.findElement(key);
        if (checkbox.isSelected()) {
            checkbox.click();
            logger.info(key + " checkbox tıklandı.");
        } else {
            logger.info(key + " checkbox zaten tıklanmamış, herhangi bir işlem yapılmadı.");
        }
    }


    @Step("<key> checkbox tıklanmadıysa tıkla, tıklandıysa tıklama")
    public void clickCheckboxIfNotClicked(String key) {
        WebElement checkbox = methods.findElement(key);
        if (!checkbox.isSelected()) {
            checkbox.click();
            logger.info(key + " checkbox tıklandı.");
        } else {
            logger.info(key + " checkbox zaten tıklanmış, herhangi bir işlem yapılmadı.");
        }
    }


    @Step({"Check if element <key> contains text <expectedText>",
            "<key> elementi <expectedText> değerini içeriyor mu kontrol et"})
    public void checkElementContainsText(String key, String expectedText) {
        Boolean containsText = methods.findElement(key).getText().contains(expectedText);
        logger.info("Beklenen text: " + expectedText);
        logger.info("Gerçek text: " + containsText);
        assertTrue(containsText, "Expected text is not contained!!  " +
                "  Beklenen text ile gerçek text aynı değil!!");
        logger.info(key + " elementi " + "'"+ expectedText + "'" + " degerini iceriyor.");
    }



    @Step({"Check if element <key> exists",
            "Element <key> var mı kontrol et"})
    public WebElement getElementWithKeyIfExists(String key) {
        WebElement webElement;
        int loopCount = 0;
        while (loopCount < methods.DEFAULT_MAX_ITERATION_COUNT) {
            try {
                webElement = methods.findElement(key);
                logger.info(key + " elementi bulundu.");
                return webElement;
            } catch (WebDriverException e) {
            }
            loopCount++;
            waitByMilliSeconds(methods.DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        assertFalse(Boolean.parseBoolean("Element: '" + key + "' bulanamadı"));
        return null;
    }


}