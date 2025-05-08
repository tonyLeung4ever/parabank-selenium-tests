import com.inflectra.spiratest.addons.junitextension.SpiraTestConfiguration;
import com.inflectra.spiratest.addons.junitextension.SpiraTestCase;
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
public class RegistrationTest {
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
    @SpiraTestCase(testCaseId = 23191)
    @Order(1)
    public void testNavigateToRegistrationPage() {
        // Navigate to the registration page
        driver.get("https://parabank.parasoft.com/parabank/register.htm");

        // Verify that we are on the registration page
        String actualResult = driver.getCurrentUrl();
        String expectedResult = "https://parabank.parasoft.com/parabank/register.htm";
        assertEquals(expectedResult, actualResult, "Should navigate to the registration page.");
    }

    @Test
    @SpiraTestCase(testCaseId = 23192)
    @Order(2)
    public void testEnterFirstName() {
        WebElement firstNameField = driver.findElement(By.name("customer.firstName"));
        String expectedFirstName = "John";
        firstNameField.sendKeys(expectedFirstName);

        assertEquals(expectedFirstName, firstNameField.getAttribute("value"),
                "The first name should match the input value.");
    }

    @Test
    @SpiraTestCase(testCaseId = 23193)
    @Order(3)
    public void testEnterLastName() {
        WebElement lastNameField = driver.findElement(By.name("customer.lastName"));
        String expectedLastName = "Doe";
        lastNameField.sendKeys(expectedLastName);

        assertEquals(expectedLastName, lastNameField.getAttribute("value"),
                "The last name should match the input value.");
    }

    @Test
    @SpiraTestCase(testCaseId = 23194)
    @Order(4)
    public void testEnterStreetAddress() {
        WebElement streetField = driver.findElement(By.name("customer.address.street"));
        String expectedStreet = "123 Main St";
        streetField.sendKeys(expectedStreet);

        assertEquals(expectedStreet, streetField.getAttribute("value"),
                "The street address should match the input value.");
    }

    @Test
    @SpiraTestCase(testCaseId = 23195)
    @Order(5)
    public void testEnterCity() {
        WebElement cityField = driver.findElement(By.name("customer.address.city"));
        String expectedCity = "Anytown";
        cityField.sendKeys(expectedCity);

        assertEquals(expectedCity, cityField.getAttribute("value"),
                "The city should match the input value.");
    }

    @Test
    @SpiraTestCase(testCaseId = 23196)
    @Order(6)
    public void testEnterState() {
        WebElement stateField = driver.findElement(By.name("customer.address.state"));
        String expectedState = "CA";
        stateField.sendKeys(expectedState);

        assertEquals(expectedState, stateField.getAttribute("value"),
                "The state should match the input value.");
    }

    @Test
    @SpiraTestCase(testCaseId = 23197)
    @Order(7)
    public void testEnterZipCode() {
        WebElement zipField = driver.findElement(By.name("customer.address.zipCode"));
        String expectedZip = "12345";
        zipField.sendKeys(expectedZip);

        assertEquals(expectedZip, zipField.getAttribute("value"),
                "The zip code should match the input value.");
    }

    @Test
    @SpiraTestCase(testCaseId = 23198)
    @Order(8)
    public void testEnterPhoneNumber() {
        WebElement phoneField = driver.findElement(By.name("customer.phoneNumber"));
        String expectedPhone = "1234567890";
        phoneField.sendKeys(expectedPhone);

        assertEquals(expectedPhone, phoneField.getAttribute("value"),
                "The phone number should match the input value.");
    }

    @Test
    @SpiraTestCase(testCaseId = 23199)
    @Order(9)
    public void testEnterSSN() {
        WebElement ssnField = driver.findElement(By.name("customer.ssn"));
        String expectedSSN = "123-45-6789";
        ssnField.sendKeys(expectedSSN);

        assertEquals(expectedSSN, ssnField.getAttribute("value"),
                "The SSN should match the input value.");
    }

    @Test
    @SpiraTestCase(testCaseId = 23200)
    @Order(10)
    public void testEnterUsername() {
        WebElement usernameField = driver.findElement(By.name("customer.username"));
        String expectedUsername = "testuser12";
        usernameField.sendKeys(expectedUsername);

        assertEquals(expectedUsername, usernameField.getAttribute("value"),
                "The username should match the input value.");
    }

    @Test
    @SpiraTestCase(testCaseId = 23201)
    @Order(11)
    public void testEnterPassword() {
        WebElement passwordField = driver.findElement(By.name("customer.password"));
        String expectedPassword = "password12";
        passwordField.sendKeys(expectedPassword);

        assertEquals(expectedPassword, passwordField.getAttribute("value"),
                "The password should match the input value.");
    }

    @Test
    @SpiraTestCase(testCaseId = 23202)
    @Order(12)
    public void testEnterRepeatedPassword() {
        WebElement repeatedPasswordField = driver.findElement(By.name("repeatedPassword"));
        String expectedRepeatedPassword = "password12";
        repeatedPasswordField.sendKeys(expectedRepeatedPassword);

        assertEquals(expectedRepeatedPassword, repeatedPasswordField.getAttribute("value"),
                "The repeated password should match the input value.");
    }


    @Test
    @SpiraTestCase(testCaseId = 23204)
    @Order(13)
    public void testVerifyRegistrationSuccessMessage() {
        driver.findElement(By.cssSelector("input[value='Register']")).click();
        // Verify the presence of the success message
        WebElement successMessage = driver.findElement(By.cssSelector("#rightPanel p"));
        String actualSuccessText = successMessage.getText();
        String expectedSuccessText = "Your account was created successfully. You are now logged in.";
        assertEquals(expectedSuccessText, actualSuccessText, "The success message should indicate successful registration.");
    }


    @AfterAll
    public static void CloseBrowser()
    {
        driver.close();
    }


}
