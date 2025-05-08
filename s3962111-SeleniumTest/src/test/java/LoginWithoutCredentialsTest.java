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

@SpiraTestConfiguration (
//following are REQUIRED
        url = "https://rmit.spiraservice.net/",
        login = "s3962111",
        rssToken = "{E5D54B1D-0483-4DE5-9AF2-60F2340E9440}",
        projectId = 282
)


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginWithoutCredentialsTest {
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
    }

    @Test
    @SpiraTestCase(testCaseId = 23174)
    @Order(1)
    public void testPageLoad() {
        driver.get("https://parabank.parasoft.com/parabank/index.htm");
        expectedResult = "ParaBank | Welcome | Online Banking";
        actualResult = driver.getTitle();

        // Assert that we're on the correct page
        assertEquals(expectedResult, actualResult);
    }

    @Test
    @SpiraTestCase(testCaseId = 23177)
    @Order(2)
    public void testUsernameFieldPresence() {
        // Find the username field and assert it is present
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement usernameField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("username"))
        );
//        WebElement usernameField = driver.findElement(By.name("username"));
        assertNotNull(usernameField, "Username field should be present on the main page.");
    }

    @Test
    @SpiraTestCase(testCaseId = 23179)
    @Order(3)
    public void testPasswordFieldPresence() {
        // Find the password field and assert it is present
        WebElement passwordField = driver.findElement(By.name("password"));
        assertNotNull(passwordField, "Password field should be present on the main page.");
    }

    @Test
    @SpiraTestCase(testCaseId = 23180)
    @Order(4)
    public void testLoginButtonPresence(){
        //Find login button and assert it is present
        WebElement loginButton = driver.findElement(By.cssSelector("input[type='submit']"));
        assertNotNull(loginButton, "Login button should be present on the login page.");
    }

    @Test
    @SpiraTestCase(testCaseId = 23182)
    @Order(5)
    public void testLoginRedirectUrl() {
        // Leave username and password blank, click login
        driver.findElement(By.cssSelector("input[value='Log In']")).click();

        // Check if the URL is the expected login page URL
        String actualResult = driver.getCurrentUrl();
        String baseUrl = actualResult.split(";")[0]; // Extract part before session ID
        String expectedResult = "https://parabank.parasoft.com/parabank/login.htm";
        assertEquals(expectedResult, baseUrl,
                "User should be redirected to the login page when credentials are missing.");
    }

    @Test
    @SpiraTestCase(testCaseId = 23184)
    @Order(6)
    public void testErrorMessageDisplayed() {
        // Leave username and password blank, click login
        driver.findElement(By.cssSelector("input[value='Log In']")).click();

        // Verify the presence of the error message
        WebElement errorMessage = driver.findElement(By.cssSelector(".error"));
        String actualErrorText = errorMessage.getText();
        String expectedErrorText = "Please enter a username and password.";
        assertEquals(expectedErrorText, actualErrorText,
                "The error message should indicate missing credentials.");
    }

    @AfterAll
    public static void CloseBrowser()
    {
        driver.close();
    }


}
