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
public class OpenSavingsAccountTest {
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

        // Navigate to the login page

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://parabank.parasoft.com/parabank/index.htm");

        // Log in using registered credentials
        driver.findElement(By.name("username")).sendKeys("testuser12"); // Registered username
        driver.findElement(By.name("password")).sendKeys("password12"); // Registered password
        driver.findElement(By.cssSelector("input[value='Log In']")).click(); // Submit the login form

    }

    @Test
    @SpiraTestCase(testCaseId = 23207)
    @Order(1)
    public void testNavigateToOpenSavingsAccountPage() {
        // Navigate to the Open Savings Account page
        driver.findElement(By.linkText("Open New Account")).click();

        // Assert that we are on the correct page
        String expectedTitle = "ParaBank | Open Account";
        String actualTitle = driver.getTitle();
        Assertions.assertEquals(expectedTitle, actualTitle, "The title should be 'Parabank | Open Account'.");
    }

    @Test
    @SpiraTestCase(testCaseId = 23208)
    @Order(2)
    public void testSelectAccountTypeSavings() {
        // Wait for the account type dropdown to be visible
        WebElement accountTypeDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("type")));

        // Select the "SAVINGS" account type
        Select accountType = new Select(accountTypeDropdown);
        accountType.selectByVisibleText("SAVINGS");

        // Assert that "SAVINGS" was selected
        assertEquals("1", accountType.getFirstSelectedOption().getAttribute("value"),
                "Account type should be 'SAVINGS'.");
    }

    @Test
    @SpiraTestCase(testCaseId = 23209)
    @Order(3)
    public void testSelectExistingAccount() {
        // Wait for the 'fromAccountId' dropdown to be visible
        WebElement accountDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fromAccountId")));

        // Create a Select instance to interact with the dropdown
        Select select = new Select(accountDropdown);

        // Step 2: Wait until the dropdown has at least one option available
        wait.until(driver -> select.getOptions().size() > 0);

        // Select the first (and only) option by **visible text or value**
        select.selectByIndex(0);


        // Assert that the first account was selected
        String selectedAccount = select.getFirstSelectedOption().getAttribute("value");
        assertNotNull(selectedAccount, "An existing account should be selected.");
    }

    @Test
    @SpiraTestCase(testCaseId = 23210)
    @Order(4)
    public void testSubmitOpenAccountForm() {
        // Click the "Open New Account" button
        WebElement openAccountButton = driver.findElement(By.cssSelector("input.button[value='Open New Account']"));
        openAccountButton.click();

        // Wait for the success message to be visible
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#openAccountResult p:first-of-type")));

        // Extract the actual message text
        String actualMessage = successMessage.getText();

        // Define the expected success message
        String expectedMessage = "Congratulations, your account is now open.";

        // Assert that the actual message matches the expected message
        assertEquals(expectedMessage, actualMessage, "The success message should match the expected text.");
    }



    @AfterAll
    public static void CloseBrowser()
    {
        driver.close();
    }
    }


