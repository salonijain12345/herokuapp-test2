package projectself.heroapp;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;

public class SAT {

    public static void main(String[] args) throws InterruptedException {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.setCapability("se:cdpVersion", "");
        LoggingPreferences logs = new LoggingPreferences();
        logs.enable(LogType.BROWSER, Level.ALL);

        ChromeOptions options1 = new ChromeOptions();
        options1.setCapability("goog:loggingPrefs", logs);
        WebDriver driver = new ChromeDriver(options1);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Actions actions = new Actions(driver);
        JavascriptExecutor js = (JavascriptExecutor) driver;

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("https://admin:admin@the-internet.herokuapp.com/");
        
        // A/B Testing
        driver.findElement(By.linkText("A/B Testing")).click();
        driver.navigate().back();

        // Add/Remove Elements
        driver.findElement(By.linkText("Add/Remove Elements")).click();
        driver.findElement(By.xpath("//*[@id=\"content\"]/div/button")).click();
        driver.findElement(By.className("added-manually")).click();
        driver.navigate().back();

        // Basic Auth
        driver.findElement(By.linkText("Basic Auth")).click();
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div#content>div>p"))
        );
        String text = driver.findElement(By.cssSelector("div#content>div>p")).getText();
        System.out.print(text);
        Assert.assertEquals(text, "Congratulations! You must have the proper credentials.");
        driver.navigate().back();

        // Broken Images
        driver.findElement(By.linkText("Broken Images")).click();
        List<WebElement> images = driver.findElements(By.tagName("img"));
        List<String> url = new ArrayList<>();
        for (WebElement element : images)
            url.add(element.getAttribute("src"));

        for (String imageurl : url) {
            System.out.println(imageurl);
            System.out.println(RestAssured.given().when().get(imageurl).statusCode());
        }
        driver.navigate().back();

        // Challenging DOM
        driver.findElement(By.linkText("Challenging DOM")).click();
        List<WebElement> rows = driver.findElements(By.xpath("//table[@class='large-10 columns']//tbody/tr"));

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            System.out.println("Row starts with: " + cells.get(0).getText());

            if (cells.get(0).getText().equals("Iuvaret5")) {
                WebElement editBtn = cells.get(cells.size() - 2).findElement(By.tagName("a"));
                editBtn.click();
                System.out.println("Clicked Edit for Iuvaret5");
                driver.navigate().back();

                rows = driver.findElements(By.xpath("//table[@class='large-10 columns']//tbody/tr"));
                row = rows.get(5);
                cells = row.findElements(By.tagName("td"));
                WebElement deleteBtn = cells.get(cells.size() - 1).findElement(By.tagName("a"));
                deleteBtn.click();
                System.out.println("Clicked Delete for Iuvaret5");
                break;
            }
        }
        driver.navigate().back();

        // Checkboxes
        driver.findElement(By.linkText("Checkboxes")).click();
        driver.findElement(By.xpath("(//input[@type='checkbox'])[1]")).click();
        driver.navigate().back();

        // Context Menu
        driver.findElement(By.linkText("Context Menu")).click();
        
        actions.contextClick(driver.findElement(By.id("hot-spot"))).perform();
        Alert alert = driver.switchTo().alert();
        System.out.println(alert.getText());
        alert.accept();
        driver.navigate().back();

        // Digest Authentication
        driver.findElement(By.linkText("Digest Authentication")).click();
        System.out.println("Digest Authentication completed");
        driver.navigate().back();

        // Disappearing Elements
        driver.findElement(By.linkText("Disappearing Elements")).click();
        driver.findElement(By.linkText("Portfolio")).click();
       System.out.print(driver.findElement(By.xpath("/html/body/h1")).getText());
       driver.navigate().back();
        driver.findElement(By.linkText("Contact Us")).click();
        System.out.print(driver.findElement(By.xpath("/html/body/h1")).getText());
        driver.navigate().back();
        driver.findElement(By.linkText("About")).click();
        System.out.print(driver.findElement(By.xpath("/html/body/h1")).getText());
        driver.navigate().back();
        driver.findElement(By.linkText("Home")).click();


        // Drag and Drop
        driver.findElement(By.linkText("Drag and Drop")).click();
        WebElement source = driver.findElement(By.id("column-a"));
        WebElement target = driver.findElement(By.id("column-b"));
        actions.dragAndDrop(source, target).build().perform();
        driver.navigate().back();
        
        //dropdown
        driver.findElement(By.linkText("Dropdown")).click();
        WebElement dropdownElement = driver.findElement(By.id("dropdown"));
        Select dropdown = new Select(dropdownElement);
        dropdown.selectByVisibleText("Option 1");
        dropdown.selectByIndex(2);
        driver.navigate().back();

        //Dynamic Controls
        driver.findElement(By.linkText("Dynamic Controls")).click();
        driver.findElement(By.xpath("//div[@id='checkbox']//input[@type='checkbox']")).click();
        driver.findElement(By.xpath("//button[text()='Remove']")).click();
        driver.findElement(By.xpath("//button[text()='Enable']")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//button[text()='Disable']")).click();
        driver.navigate().back();
        
        //Dynamic Loading
        
        driver.findElement(By.linkText("Dynamic Loading")).click();
        driver.findElement(By.linkText("Example 1: Element on page that is hidden")).click();
        driver.findElement(By.xpath("//button[text()='Start']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='finish']/h4")));
        System.out.println(driver.findElement(By.xpath("//div[@id='finish']/h4")).getText());
        driver.navigate().back();
        
        driver.findElement(By.linkText("Example 2: Element rendered after the fact")).click();
        driver.findElement(By.xpath("//button[text()='Start']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='finish']/h4")));
        System.out.println(driver.findElement(By.xpath("//div[@id='finish']/h4")).getText());
        driver.navigate().back();
        driver.navigate().back();
        
        //Entry Ad
        driver.findElement(By.linkText("Entry Ad")).click();
        driver.findElement(By.cssSelector(".modal-footer p")).click();
        driver.navigate().back();
        
        //exit_intent
        driver.findElement(By.linkText("Exit Intent")).click();
        //move courser to toll bar
        Thread.sleep(4000);
        System.out.println(driver.findElement(By.cssSelector(".modal-footer p")).getText());
        driver.findElement(By.cssSelector(".modal-footer p")).click();
        driver.navigate().back();
        
        //File Download
        driver.findElement(By.linkText("File Download")).click();
        driver.findElement(By.linkText("sampleFile.jpeg")).click();
        
        //file upload
        driver.findElement(By.linkText("File Upload")).click();
        driver.findElement(By.id("file-upload")).sendKeys("C:\\Users\\saloni jain\\Downloads\\samplefile.jpeg");
        driver.findElement(By.id("file-submit")).click();
        driver.navigate().back();
        
        //Floating Menu
        driver.findElement(By.linkText("Floating Menu")).click();
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        Thread.sleep(2000);
       WebElement menu= driver.findElement(By.id("menu"));
       if(menu.isDisplayed())
       System.out.println("Floating menu is visible after scroll.");
        driver.navigate().back();
        
          //Forgot Password
        driver.findElement(By.linkText("Forgot Password")).click();
        driver.findElement(By.id("email")).sendKeys("abc@gmail.com");
        driver.findElement(By.id("form_submit")).click();
        driver.navigate().back();
        driver.navigate().back();
        
         //Form Authentication
        driver.findElement(By.linkText("Form Authentication")).click();
        driver.findElement(By.id("username")).sendKeys("tomsmith");
        driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");
        driver.findElement(By.className("radius")).click();
        driver.findElement(By.linkText("Logout")).click();
        Thread.sleep(2000);
        driver.navigate().back();
        driver.navigate().back();
        driver.navigate().back();
        
        //Frames
        driver.findElement(By.linkText("Frames")).click();
        driver.findElement(By.linkText("Nested Frames")).click();
        driver.switchTo().frame("frame-top");
        driver.switchTo().frame("frame-left");
	    System.out.println(driver.findElement(By.tagName("body")).getText());
	    driver.navigate().back();
	    
	    driver.findElement(By.linkText("Nested Frames")).click();
	    driver.switchTo().frame("frame-top");
		driver.switchTo().frame("frame-middle");
	    System.out.println(driver.findElement(By.tagName("body")).getText());
	    driver.navigate().back();
	    
	    driver.findElement(By.linkText("iFrame")).click();
	    driver.findElement(By.cssSelector(".tox-notification__dismiss")).click();
	    driver.switchTo().frame("mce_0_ifr");
	    System.out.println(driver.findElement(By.tagName("body")).getText());
        driver.navigate().back();
        driver.navigate().back();
        
	  //Geolocation
        driver.findElement(By.linkText("Geolocation")).click();  
        driver.findElement(By.xpath("//button[text()='Where am I?']")).click();
        Thread.sleep(1000);
        System.out.println("Location result: " + driver.findElement(By.id("demo")).getText());
        driver.navigate().back();
        
        //Horizontal Slider
        driver.findElement(By.linkText("Horizontal Slider")).click(); 
        
        actions.clickAndHold(driver.findElement(By.tagName("input"))).moveByOffset(40, 0).release().perform();
        System.out.println( driver.findElement(By.id("range")).getText());
        driver.navigate().back();
        
          //Hovers
          driver.findElement(By.linkText("Hovers")).click();
          actions.moveToElement(driver.findElement(By.className("figure"))).perform();
          driver.findElement(By.linkText("View profile")).click();
          driver.navigate().back();
          driver.navigate().back();
          
          //Infinite Scroll
        driver.findElement(By.linkText("Infinite Scroll")).click();
        for(int i=0;i<5;i++)
        { js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        Thread.sleep(2000); 
        System.out.println("Scrolled down: " + (i + 1));}
          driver.navigate().back();
        
          //Inputs
        driver.findElement(By.linkText("Inputs")).click();
        driver.findElement(By.tagName("input")).sendKeys("25");
        System.out.println(driver.findElement(By.tagName("input")).getAttribute("value"));
        driver.navigate().back();
        
           //JQuery UI Menus
	       driver.findElement(By.linkText("JQuery UI Menus")).click();
	       actions.moveToElement(driver.findElement(By.id("ui-id-3"))).perform();
	       driver.findElement(By.id("ui-id-4")).click();
	       driver.findElement(By.id("ui-id-5")).click();
           driver.navigate().back();
        driver.navigate().back();
           JavaScript Alerts
           driver.findElement(By.linkText("JavaScript Alerts")).click();
           driver.findElement(By.xpath("//button[text()='Click for JS Alert']")).click();
           driver.switchTo().alert().accept();
           System.out.println(driver.findElement(By.id("result")).getText());
           Assert.assertEquals(driver.findElement(By.id("result")).getText(), "You successfully clicked an alert");
           Thread.sleep(1000);
           driver.findElement(By.xpath("//button[text()='Click for JS Confirm']")).click();
           driver.switchTo().alert().dismiss();
           System.out.println(driver.findElement(By.id("result")).getText());
           Thread.sleep(1000);
           driver.findElement(By.xpath("//button[text()='Click for JS Prompt']")).click();
           driver.switchTo().alert().sendKeys("hey");
           driver.switchTo().alert().accept();
           Assert.assertEquals(driver.findElement(By.id("result")).getText(), "You entered: hey");
           System.out.println(driver.findElement(By.id("result")).getText());
           driver.navigate().back();
           
           JavaScript onload event error
           driver.findElement(By.linkText("JavaScript onload event error")).click();
           LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
           boolean jsErrorFound = false;
           for (LogEntry entry : logEntries) {
               if (entry.getLevel().toString().equals("SEVERE")) {
                   System.out.println("JavaScript Error Found: " + entry.getMessage());
                   jsErrorFound = true;
               }
           }
           driver.navigate().back();
           
           //Key Presses
           driver.findElement(By.linkText("Key Presses")).click();
          
           driver.findElement(By.id("target")).sendKeys(Keys.SPACE);
           Thread.sleep(1000);
           System.out.println(driver.findElement(By.id("result")).getText());
           driver.findElement(By.id("target")).sendKeys(Keys.ESCAPE);
           Thread.sleep(1000);
           System.out.println(driver.findElement(By.id("result")).getText());
           driver.navigate().back();
           
             //Large & Deep DOM
        driver.findElement(By.linkText("Large & Deep DOM")).click();
        js.executeScript("window.scrollBy(0, 3000)");

        // Find a specific cell (e.g., Cell 50.50)
        WebElement cell = driver.findElement(By.xpath("//td[text()='50.50']"));
        System.out.println("Found Cell Text: " + cell.getText());
        
        //Multiple Windows
        
        
        //driver.close();
    }
}
