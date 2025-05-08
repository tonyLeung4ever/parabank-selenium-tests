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
public class TransferFundsTest {
    private static ChromeDriver driver;
    private String expectedResult;
    private String actualResult;
    private WebElement element;
    private static WebDriverWait wait;
    private static String savingsAccountNumber;

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

        // Create a Savings Account
        savingsAccountNumber = createSavingsAccount();

    }
    private static String createSavingsAccount() {
        // Navigate to the account opening page and select savings account
        driver.findElement(By.linkText("Open New Account")).click();
        Select accountType = new Select(driver.findElement(By.id("type")));
        accountType.selectByVisibleText("SAVINGS");

        WebElement accountDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fromAccountId")));
        Select select = new Select(accountDropdown);
        wait.until(driver -> select.getOptions().size() > 0);  // Ensure options are loaded
        select.selectByIndex(0);

        driver.findElement(By.cssSelector("input.button[value='Open New Account']")).click();

        // Wait for and extract the new account number
        WebElement newAccountLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("newAccountId")));
        savingsAccountNumber = newAccountLink.getText();

        // Refresh page to reload account dropdowns
        driver.navigate().refresh();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fromAccountId")));

        return savingsAccountNumber;
    }


    @Test
    @SpiraTestCase(testCaseId = 23220)
    @Order(1)
    public void testNavigateToTransferFunds() {
        // Click on the "Transfer Funds" link
        driver.findElement(By.linkText("Transfer Funds")).click();

        // Check the current URL
        String currentUrl = driver.getCurrentUrl();
        assertEquals("https://parabank.parasoft.com/parabank/transfer.htm", currentUrl,
                "URL should be the Transfer Funds page.");
    }

    @Test
    @SpiraTestCase(testCaseId = 23221)
    @Order(2)
    public void testEnterTransferAmount() {
        // Enter amount in the transfer field
        WebElement amountField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("amount")));
        amountField.sendKeys("100"); // Enter the amount to transfer

        // Assert that the entered value matches
        assertEquals("100", amountField.getAttribute("value"), "Transfer amount should be 100.");
    }

    @Test
    @SpiraTestCase(testCaseId = 23222)
    @Order(3)
    public void testSelectFromAccount() {
        // Wait for the dropdown to be visible and reinitialize it to get fresh options
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("fromAccountId")));
        WebElement fromAccountDropdown = driver.findElement(By.id("fromAccountId"));
        Select fromAccountSelect = new Select(fromAccountDropdown);

        // Adding a wait to ensure dropdown options are fully loaded
        wait.until(driver -> fromAccountSelect.getOptions().size() > 0);

        boolean accountFound = fromAccountSelect.getOptions().stream()
                .anyMatch(option -> option.getText().equals(savingsAccountNumber));

        assertTrue(accountFound, "Savings account number is not available in the dropdown.");
        fromAccountSelect.selectByVisibleText(savingsAccountNumber);
    }


    @Test
    @SpiraTestCase(testCaseId = 23223)
    @Order(4)
    public void testSelectToAccount() {
        WebElement toAccountDropdown = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("toAccountId"))
        );
        Select toAccountSelect = new Select(toAccountDropdown);

        // Adding a 5-second wait before selection
        wait.until(ExpectedConditions.elementToBeClickable(toAccountDropdown));

        boolean differentAccountSelected = false;
        for (WebElement option : toAccountSelect.getOptions()) {
            if (!option.getText().equals(savingsAccountNumber)) {
                toAccountSelect.selectByVisibleText(option.getText());
                differentAccountSelected = true;
                break;
            }
        }

        assertTrue(differentAccountSelected, "No other accounts available to select as To account.");
    }

    @Test
    @SpiraTestCase(testCaseId = 23225)
    @Order(5)
    public void testTransferFunds() {
        // Click the Transfer button
        WebElement transferButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input.button[value='Transfer']")));
        transferButton.click();

        // Wait for the success message to be visible
        WebElement transferCompleteTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#showResult h1.title")));

        // Assert that the title indicates a successful transfer
        String expectedTitle = "Transfer Complete!";
        String actualTitle = transferCompleteTitle.getText().trim();

        assertEquals(expectedTitle, actualTitle, "Success title should match.");
    }

    @Test
    @SpiraTestCase(testCaseId = 23226)
    @Order(6)
    public void testCheckSavingsAccountBalance() {
        driver.findElement(By.linkText("Accounts Overview")).click();

        WebElement savingsBalanceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//tr[td/a[text()='" + savingsAccountNumber + "']]/td[2]")
        ));

        String balanceText = savingsBalanceElement.getText().trim().replace("$", "").replace(",", "");
        double savingsAccountBalance = Double.parseDouble(balanceText);

        // Adjust expected value based on correct transfer behavior (-100 or remaining balance)
        assertEquals(100.0, savingsAccountBalance, "Savings account balance should be 100 after the transfer.");
    }


    @AfterAll
    public static void CloseBrowser()
    {
        driver.close();
    }
}
