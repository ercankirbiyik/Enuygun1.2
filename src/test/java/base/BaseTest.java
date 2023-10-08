package base;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.BeforeScenario;
import model.ElementInfos;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

public class BaseTest {


    protected static ChromeOptions chromeOptions = new ChromeOptions();
    protected static WebDriver driver = new ChromeDriver();

    FirefoxOptions firefoxOptions = new FirefoxOptions();
    protected static Actions actions;

    public Logger logger = LoggerFactory.getLogger(getClass());

    String selectPlatform = "mac";

    String browserName = "chrome";

    String testUrl = "https://www.enuygun.com/";

    public static final String DEFAULT_DIRECTORY_PATH = "elementValues";

    ConcurrentMap<String, Object> elementMapList = new ConcurrentHashMap<>();

    @BeforeScenario
    public void setup() {
        logger.info("************* Test Started *************");
        if (StringUtils.isEmpty(System.getenv("key"))) {
            logger.info("Local cihazda " + selectPlatform + " ortaminda " + browserName + " browserinda test ayaga kalkacak");
            if ("mac".equalsIgnoreCase(selectPlatform)) {
                if ("chrome".equalsIgnoreCase(browserName)) {
                    //driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
                    chromeOptions.addArguments("--start-fullscreen");
                    //driver.get(testUrl);
                    logger.info(testUrl + " url adresi açılıyor!");
                } else if ("firefox".equalsIgnoreCase(browserName)) {
                    //driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
                    firefoxOptions.addArguments("--start-fullscreen");
                    //driver.get(testUrl);
                    logger.info(testUrl + " url adresi açılıyor!");
                } else if ("edge".equalsIgnoreCase(browserName)) {
                    //driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
                    //driver.get(testUrl);
                    logger.info(testUrl + " url adresi açılıyor!");
                }
                actions = new Actions(driver);
            }
        }
    }

    @AfterScenario
    public void tearDown() {
        driver.quit();
    }


    public void initMap(List<File> fileList) {
        elementMapList = new ConcurrentHashMap<>();
        Type elementType = new TypeToken<List<ElementInfos>>() {
        }.getType();
        Gson gson = new Gson();
        List<ElementInfos> elementInfoList = null;
        for (File file : fileList) {
            try {
                FileReader filez = new FileReader(file);
                elementInfoList = gson
                        .fromJson(new FileReader(file), elementType);
                elementInfoList.parallelStream()
                        .forEach(elementInfo -> elementMapList.put(elementInfo.getKey(), elementInfo));
            } catch (FileNotFoundException e) {

            }
        }
    }

    public static List<File> getFileList(String directoryName) throws IOException {
        List<File> dirList = new ArrayList<>();
        try (Stream<Path> walkStream = Files.walk(Paths.get(directoryName))) {
            walkStream.filter(p -> p.toFile().isFile()).forEach(f -> {
                if (f.toString().endsWith(".json")) {
                    // System.out.println(f.toFile().getName() + " json dosyası bulundu.");
                    dirList.add(f.toFile());
                }
            });
        }
        return dirList;
    }

    public ElementInfos findElementInfoByKey(String key) {
        return (ElementInfos) elementMapList.get(key);
    }

    public void saveValue(String key, String value) {
        elementMapList.put(key, value);
    }

    public String getValue(String key) {
        return elementMapList.get(key).toString();
    }

}