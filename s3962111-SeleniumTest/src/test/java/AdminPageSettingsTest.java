import com.inflectra.spiratest.addons.junitextension.SpiraTestCase;
import com.inflectra.spiratest.addons.junitextension.SpiraTestConfiguration;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpiraTestConfiguration(
//following are REQUIRED
        url = "https://rmit.spiraservice.net/",
        login = "s3962111",
        rssToken = "{E5D54B1D-0483-4DE5-9AF2-60F2340E9440}",
        projectId = 282
)

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdminPageSettingsTest {
    private static ChromeDriver driver;
    private String expectedResult;
    private String actualResult;
    private WebElement element;
    private static WebDriverWait wait;
    private String accountNumber;

    @BeforeAll
    public static void setup()
    {
        System.setProperty("Webdriver.chrome.driver","chromedriver.exe");
        driver = new ChromeDriver();



        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://parabank.parasoft.com/parabank/admin.htm");

    }

    @Test
    @SpiraTestCase(testCaseId =23243)
    @Order(1)
    public void testNavigateToAdminPage() {
        // Wait for the page to be loaded and assert the URL
        wait.until(ExpectedConditions.urlToBe("https://parabank.parasoft.com/parabank/admin.htm"));
        assertEquals("https://parabank.parasoft.com/parabank/admin.htm", driver.getCurrentUrl(),
                "URL should be the Admin page.");
    }


    @Test
    @SpiraTestCase(testCaseId =23244)
    @Order(2)
    public void testChangeDataAccessMode() {
        // Locate all the radio buttons
        List<WebElement> accessModes = driver.findElements(By.name("accessMode"));

        // Find the initially selected radio button
        WebElement initiallySelected = accessModes.stream()
                .filter(WebElement::isSelected)
                .findFirst()
                .orElseThrow(() -> new AssertionError("No access mode was initially selected."));

        // Record the initially selected mode's value
        String initialValue = initiallySelected.getAttribute("value");

        // Select a different mode that is not the initially selected one
        WebElement newSelection = accessModes.stream()
                .filter(mode -> !mode.getAttribute("value").equals(initialValue))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No other access mode available to select."));

        // Click the new selection to change the data access mode
        newSelection.click();

        // Assert that the selected mode has changed
        String newValue = newSelection.getAttribute("value");
        assertNotEquals(initialValue, newValue,
                "The data access mode should be changed from the initial selection.");
    }

    @Test
    @SpiraTestCase(testCaseId =23245)
    @Order(3)
    public void testSetInitialBalance() {
        WebElement initialBalanceInput = driver.findElement(By.id("initialBalance"));
        initialBalanceInput.clear();
        initialBalanceInput.sendKeys("600");

        assertEquals("600", initialBalanceInput.getAttribute("value"),
                "Initial balance should be 600.");
    }

    @Test
    @SpiraTestCase(testCaseId =23247)
    @Order(4)
    public void testSetMinimumBalance() {
        WebElement minimumBalanceInput = driver.findElement(By.id("minimumBalance"));
        minimumBalanceInput.clear();
        minimumBalanceInput.sendKeys("200");

        assertEquals("200", minimumBalanceInput.getAttribute("value"),
                "Minimum balance should be 200.");
    }

    @Test
    @SpiraTestCase(testCaseId =23248)
    @Order(5)
    public void testSetLoanProvider() {
        Select loanProvider = new Select(driver.findElement(By.id("loanProvider")));
        loanProvider.selectByVisibleText("Local");

        assertEquals("local", loanProvider.getFirstSelectedOption().getAttribute("value"),
                "Loan provider should be Local.");
    }

    @Test
    @SpiraTestCase(testCaseId =23249)
    @Order(6)
    public void testSetLoanProcessor() {
        Select loanProcessor = new Select(driver.findElement(By.id("loanProcessor")));
        loanProcessor.selectByVisibleText("Combined");

        assertEquals("combined", loanProcessor.getFirstSelectedOption().getAttribute("value"),
                "Loan processor should be Combined.");
    }

    @Test
    @SpiraTestCase(testCaseId =23250)
    @Order(7)
    public void testSetLoanProcessorThreshold() {
        WebElement thresholdInput = driver.findElement(By.id("loanProcessorThreshold"));
        thresholdInput.clear();
        thresholdInput.sendKeys("10");

        assertEquals("10", thresholdInput.getAttribute("value"),
                "Threshold should be 10%.");
    }


    @Test
    @SpiraTestCase(testCaseId =23251)
    @Order(8)
    public void testSubmitAndSuccessMessage() {
        // Submit the form
        WebElement submitButton = driver.findElement(By.cssSelector("input[type='submit'][value='Submit']"));
        submitButton.click();

        // Wait for the success message
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("p[style='color: #080'] > b")));

        // Assert that the success message is correct
        String expectedMessage = "Settings saved successfully.";
        String actualMessage = successMessage.getText().trim();
        assertEquals(expectedMessage, actualMessage, "Success message should match.");
    }

    @AfterAll
    public static void CloseBrowser()
    {
        driver.close();
    }
}
