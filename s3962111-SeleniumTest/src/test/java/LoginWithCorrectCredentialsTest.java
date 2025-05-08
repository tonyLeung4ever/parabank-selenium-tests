import com.inflectra.spiratest.addons.junitextension.SpiraTestCase;
import com.inflectra.spiratest.addons.junitextension.SpiraTestConfiguration;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
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
public class LoginWithCorrectCredentialsTest {

    private static ChromeDriver driver;
    private String expectedResult;
    private String actualResult;
    private WebElement element;
    private WebDriverWait wait;

    @BeforeAll
    public static void setup()
    {
        System.setProperty("Webdriver.chrome.driver","chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Navigate to the registration page and create a user
        registerNewUser();
    }

    public static void registerNewUser() {
        driver.get("https://parabank.parasoft.com/parabank/register.htm"); // Open registration page

        // Fill in the registration form
        driver.findElement(By.name("customer.firstName")).sendKeys("John");
        driver.findElement(By.name("customer.lastName")).sendKeys("Doe");
        driver.findElement(By.name("customer.address.street")).sendKeys("123 Main St");
        driver.findElement(By.name("customer.address.city")).sendKeys("Anytown");
        driver.findElement(By.name("customer.address.state")).sendKeys("CA");
        driver.findElement(By.name("customer.address.zipCode")).sendKeys("12345");
        driver.findElement(By.name("customer.phoneNumber")).sendKeys("1234567890");
        driver.findElement(By.name("customer.ssn")).sendKeys("123-45-6789");

        // Use these credentials for login tests
        String username = "testuser13";
        String password = "password13";

        driver.findElement(By.name("customer.username")).sendKeys(username);
        driver.findElement(By.name("customer.password")).sendKeys(password);
        driver.findElement(By.name("repeatedPassword")).sendKeys(password);

        // Submit the form
        driver.findElement(By.cssSelector("input.button[value='Register']")).click();

        // Verify registration was successful by checking the URL
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));

        WebElement logoutLink = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[text()='Log Out']")));
        logoutLink.click();
    }

    @Test
    @SpiraTestCase(testCaseId =23267)
    @Order(1)
    public void testPageLoad() {
        driver.get("https://parabank.parasoft.com/parabank/index.htm");
        expectedResult = "ParaBank | Welcome | Online Banking";
        actualResult = driver.getTitle();

        // Assert that we're on the correct page
        assertEquals(expectedResult, actualResult);
    }

    @Test
    @SpiraTestCase(testCaseId =23268)
    @Order(2)
    public void testEnterUsernameInLoginForm() {
        // Wait for the username field to be visible and input the username
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement usernameField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("username"))
        );

        String expectedUsername = "testuser13";
        usernameField.sendKeys(expectedUsername);

        // Assert that the username field contains the expected value
        assertEquals(expectedUsername, usernameField.getAttribute("value"),
                "Username should match the input value.");
    }

    @Test
    @SpiraTestCase(testCaseId =23269)
    @Order(3)
    public void testEnterPasswordInLoginForm() {
        // Wait for the password field to be visible and input the password
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement passwordField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("password"))
        );

        String expectedPassword = "password13";
        passwordField.sendKeys(expectedPassword);

        // Assert that the password field contains the expected value
        assertEquals(expectedPassword, passwordField.getAttribute("value"),
                "Password should match the input value.");
    }


    @Test
    @SpiraTestCase(testCaseId =23270)
    @Order(4)
    public void testSubmitLoginForm() {
        // Submit the login form
        driver.findElement(By.cssSelector("input[value='Log In']")).click();

        // Assert that the login was successful by checking for a specific element
        WebElement welcomeText = driver.findElement(By.cssSelector("#rightPanel h1.title"));
        String actualWelcomeText = welcomeText.getText();
        String expectedWelcomeText = "Accounts Overview";

        // Assert that the welcome text is displayed correctly
        assertEquals(expectedWelcomeText, actualWelcomeText, "The welcome text should indicate a successful login.");
    }


    @AfterAll
    public static void CloseBrowser()
    {
        driver.close();
    }


}
