package co.nvqa.ninja_demo.glue;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author Daniel Joi Partogi Hutapea
 */
@SuppressWarnings("Duplicates")
@ScenarioScoped
public class Sample02Steps
{
    private static final long DELAY_IN_MILLIS = 1_000;
    private Scenario scenario;
    private WebDriver webDriver;

    public Sample02Steps()
    {
    }

    @Before("@LaunchBrowser")
    public void launchBrowser(Scenario scenario)
    {
        this.scenario = scenario;

        WebDriverManager webDriverManager = WebDriverManager.chromedriver();
        webDriverManager.setup();

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-extensions");
        chromeOptions.addArguments("--window-size=1024,768");
        chromeOptions.addArguments("--window-position=0,0");

        webDriver = new ChromeDriver(chromeOptions);
    }

    @After("@CloseBrowser")
    public void closeBrowser()
    {
        if(webDriver!=null)
        {
            webDriver.quit();
        }
    }

    @Given("^I am on \"([^\"]*)\"$")
    public void iAmOn(String url)
    {
        webDriver.get(url);
        takesScreenshot();
        pause(DELAY_IN_MILLIS);
    }
    
    @When("^I type \"([^\"]*)\" for username and \"([^\"]*)\" for password")
    public void iTypeCredentials(String username, String password)
    {
    	WebElement firstLabel = webDriver.findElement(By.xpath("//form[not(contains(@style,'display: none'))]//label[1]"));
		String content = firstLabel.getText();
		String usernameSelector = "//form[not(contains(@style,'display: none'))]//input[1]";
		String passwordSelector = "//form[not(contains(@style,'display: none'))]//input[2]";
		if(content.toLowerCase()=="password");
		{
			usernameSelector = "//form[not(contains(@style,'display: none'))]//input[2]";
			passwordSelector = "//form[not(contains(@style,'display: none'))]//input[1]";
		}
    WebElement searchInputPass = webDriver.findElement(By.xpath(passwordSelector));
    searchInputPass.sendKeys(password);
    scenario.write("Password typed on 'Password' field.");
    takesScreenshot();
    
    WebElement searchInputUsername = webDriver.findElement(By.xpath(usernameSelector));
    searchInputUsername.sendKeys(username);
    scenario.write("Username typed on 'Username' field.");
    takesScreenshot();
    pause(DELAY_IN_MILLIS);
    }
    
    

    @And("^I click submit button$")
    public void iClickSubmitButton()
    {
    		String selector = "//form[not(contains(@style,'display: none'))]//button[1]";
        WebElement submitButtonWe = webDriver.findElement(By.xpath(selector));
        submitButtonWe.click();
        scenario.write("User clicked 'Submit' button.");
        takesScreenshot();
        pause(DELAY_IN_MILLIS);
    }

    @Then("^I should see \"([^\"]*)\"$")
    public void isShouldSeeTheTitleIs(String expectedPageTitle)
    {
        WebElement firstHeadingWe = webDriver.findElement(By.xpath("//p[@id='result']"));
        String resultContent = firstHeadingWe.getText();
        scenario.write("Result = "+resultContent);
        Assert.assertEquals("Result is different.", expectedPageTitle, resultContent);
        takesScreenshot();
        pause(DELAY_IN_MILLIS);
    }

    private void pause(long millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch(InterruptedException ignored)
        {
        }
    }

    private void takesScreenshot()
    {
        final byte[] screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES);
        scenario.embed(resizeScreenshot(screenshot), "image/png");
    }

    private byte[] resizeScreenshot(byte[] originalByteScreenshot)
    {
        try
        {
            ByteArrayInputStream bais = new ByteArrayInputStream(originalByteScreenshot);
            BufferedImage originalBi = ImageIO.read(bais);
            bais.close();

            int width = originalBi.getWidth();
            int height = originalBi.getHeight();

            int resizedWidth = width/3;
            int resizedHeight = height/3;

            Image resizedImage = originalBi.getScaledInstance(resizedWidth, resizedHeight, Image.SCALE_AREA_AVERAGING);
            BufferedImage resizedBi = new BufferedImage(resizedWidth, resizedHeight, originalBi.getType());
            resizedBi.getGraphics().drawImage(resizedImage, 0, 0 , null);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resizedBi, "PNG", baos);
            byte[] resizedByteScreenshot = baos.toByteArray();
            baos.close();

            return resizedByteScreenshot;
        }
        catch(IOException ignored)
        {
            return originalByteScreenshot;
        }
    }
}
