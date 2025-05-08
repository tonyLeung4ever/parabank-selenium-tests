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
public class LogoutTest {
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
    @SpiraTestCase(testCaseId =23260)
    @Order(1)
    public void testLogoutLinkPresence() {
        // Check for the presence of the 'Log Out' link
        WebElement logoutLink = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[text()='Log Out']")));
        assertNotNull(logoutLink, "The 'Log Out' link should be present.");
    }

    @Test
    @SpiraTestCase(testCaseId =23262)
    @Order(2)
    public void testSuccessfulLogout() {
        // Click the 'Log Out' link
        WebElement logoutLink = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[text()='Log Out']")));
        logoutLink.click();

        // Wait for the login panel to appear
        WebElement loginPanel = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("loginPanel")));
        WebElement loginButton = loginPanel.findElement(By.xpath("//input[@value='Log In']"));

        // Use assertAll to ensure both assertions are evaluated
        assertAll(
                () -> assertNotNull(loginPanel, "The login panel should be visible after logging out."),
                () -> assertNotNull(loginButton, "The 'Log In' button should be present after logging out.")
        );
    }

    @AfterAll
    public static void CloseBrowser()
    {
        driver.close();
    }

}
