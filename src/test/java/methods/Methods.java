package methods;

import base.BaseTest;
import model.ElementInfos;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.fail;

public class Methods extends BaseTest {


    public Methods() throws IOException {
        String currentWorkingDir = System.getProperty("user.dir");
        initMap(getFileList(currentWorkingDir + "/src"));
        //initMap(getFileList());
    }
    public static int DEFAULT_MAX_ITERATION_COUNT = 5;
    public static int DEFAULT_MILLISECOND_WAIT_AMOUNT = 50;


    public void clickElement(WebElement element) {
        element.click();
    }


    public By getElementInfoToBy(ElementInfos elementInfo) {
        By by = null;
        if (elementInfo.getType().equals("css")) {
            by = By.cssSelector(elementInfo.getValue());
        } else if (elementInfo.getType().equals(("name"))) {
            by = By.name(elementInfo.getValue());
        } else if (elementInfo.getType().equals("id")) {
            by = By.id(elementInfo.getValue());
        } else if (elementInfo.getType().equals("xpath")) {
            by = By.xpath(elementInfo.getValue());
        } else if (elementInfo.getType().equals("linkText")) {
            by = By.linkText(elementInfo.getValue());
        } else if (elementInfo.getType().equals(("partialLinkText"))) {
            by = By.partialLinkText(elementInfo.getValue());
        }
        return by;
    }

    public WebElement findElement(String key) {
        By infoParam = getElementInfoToBy(findElementInfoByKey(key));
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement webElement = webDriverWait
                .until(ExpectedConditions.presenceOfElementLocated(infoParam));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                webElement);
        return webElement;
    }

    protected WebElement findElementByDataTestId(String dataTestId, Boolean failWhenNotFound) {
        logger.info("Searching element with data-testid " + "=" + "'" + dataTestId + "'");
        try {
            List<WebElement> list = driver.findElements(By.cssSelector("[data-testid = '" + dataTestId + "' i]")); // making
            // caseinsensitive

            if (failWhenNotFound) {
                if (list.size() > 1)
                    fail("Multiple elements found with data-testId:" + dataTestId);
                if (list.size() == 0)
                    fail("Element not found data-testId:" + dataTestId);
            }
            if (list.size() == 1) {
                logger.info("Element with data-testid " + "=" + "'" + dataTestId + "' found");
                return list.get(0);
            }
            return null;
        } catch (NoSuchElementException e) {
            logger.info("Element have data-testid " + dataTestId + " not found");
            if (failWhenNotFound)
                fail("Element not found data-testId:" + dataTestId);
        }
        return null;
    }


    List<WebElement> findElements(String key) {
        return driver.findElements(getElementInfoToBy(findElementInfoByKey(key)));
    }

}
