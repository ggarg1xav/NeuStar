package chbasic.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverLogLevel;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;


public class UIDriver{
	DesiredCapabilities capability;
	private WebDriver driver;
	private UIDriver instance;
	private String CurrentWindowHandle = null;
	private Actions action;
	protected ThreadLocal<RemoteWebDriver> threadDriver = null;
	//protected ThreadLocal<HtmlUnitDriver> threadDriver = null;
	

	/*public UIDriver(String browserName, String url) throws MalformedURLException {
		capability = new DesiredCapabilities();
		//capability.setBrowserName("internet explorer");
		capability.setBrowserName(browserName);
		//driver = new RemoteWebDriver(new URL(ConstantMapper.HUBUrl), capability);
		threadDriver = new ThreadLocal<RemoteWebDriver>();
        threadDriver.set(new RemoteWebDriver(new URL(ConstantMapper.HUBUrl), capability));
        driver = threadDriver.get() ;
        driver = new Augmenter().augment( driver );
		driver.get(url);
		driver.navigate().to(
				"javascript:document.getElementById('overridelink').click()");
		this.driver.manage().timeouts().implicitlyWait(05, TimeUnit.SECONDS);
		this.driver.get(url);
		driver.manage().window().maximize();
	}*/

	
//Below is for local testing with IE. Above commented constructure will be used in future
	public UIDriver(String exePath, String url) throws MalformedURLException {
		System.setProperty("webdriver.ie.driver", "repository\\exe\\IEDriverServer.exe");
		System.setProperty("webdriver.ie.driver.loglevel", InternetExplorerDriverLogLevel.ERROR.toString());
		threadDriver = new ThreadLocal<RemoteWebDriver>();
		threadDriver.set(new InternetExplorerDriver());
		driver = threadDriver.get();
		driver.get(url);
		this.driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
		// driver.navigate().to(
		// "javascript:document.getElementById('overridelink').click()");
		this.driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
		// this.driver.get(url);
		driver.manage().window().maximize();
	}
	
	//Below is for local testing with HtmlUnit. Above commented constructure will be used in future
		/*public UIDriver(String exePath, String url) throws MalformedURLException {
				System.out.println("*************URL: " + url);
				threadDriver = new ThreadLocal<HtmlUnitDriver>();
		        threadDriver.set(new HtmlUnitDriver(true));
		        driver = threadDriver.get() ;
				driver.get(url);
				driver.navigate().to(
						"javascript:document.getElementById('overridelink').click()");
				this.driver.manage().timeouts().implicitlyWait(05, TimeUnit.SECONDS);
				this.driver.get(url);
				driver.manage().window().maximize();
			}*/	
	
	public UIDriver getDriver(String browserName, String url) throws MalformedURLException {
		instance = new UIDriver(browserName, url);
		return instance;
	}

	// Function for closing IEDriver instance
	public void stop() throws WebDriverException {
		try {
			this.driver.quit();
		} catch (WebDriverException e) {
			System.out.println("Exception" + e);
			throw new WebDriverException(e);
		}
	}

	// Function for getting the page title for current window
	public String getTitle() throws WebDriverException {
		try {
			return this.driver.getTitle();
		} catch (WebDriverException e) {
			System.out.println("Exception" + e);
			throw new WebDriverException(e);
		}
	}

	/**
	 * Call this method to get the element using the By object.
	 * 
	 * @param by
	 * @return
	 * @throws WebDriverException
	 */
	public WebElement findElement(By by) throws WebDriverException {
		WebElement we;
		try {
			we = this.driver.findElement(by);
			return we;
		} catch (WebDriverException e) {
			System.out.println("Exception: " + e);
			throw new WebDriverException(e);
		}
	}

	public List<WebElement> findElements(By by) throws WebDriverException {
		List<WebElement> we;
		try {
			we = this.driver.findElements(by);
			return we;
		} catch (WebDriverException e) {
			System.out.println("Exception: " + e);
			throw new WebDriverException(e);
		}
	}

	public java.util.Set<java.lang.String> getWindowHandles() throws WebDriverException {
		return this.driver.getWindowHandles();

	}

	public String getWindowHandlesMain() throws WebDriverException {
		return this.driver.getWindowHandles().iterator().next();

	}

	public String getCurrentURL() throws WebDriverException {
		try {
			return this.driver.getCurrentUrl();
		} catch (WebDriverException e) {
			System.out.println("Exception: " + e);
			throw new WebDriverException(e);
		}
	}

	public void switchToMessageFrame() throws WebDriverException {
		try {
			this.driver.switchTo().frame("msgFrame");
		} catch (WebDriverException e) {
			System.out.println("Exception: " + e);
			throw new WebDriverException(e);
		}
	}

	public void switchToFrame(String name) throws WebDriverException, InterruptedException {
		try {
			this.driver.switchTo().frame(name);
		} catch (WebDriverException e) {
			System.out.println("Exception: " + e);
			throw new WebDriverException(e);
		}
	}

	public void switchToFrame(WebElement we) throws WebDriverException {
		try {
			// this.driver.switchTo().frame(by);
			this.driver.switchTo().frame(we);
		} catch (WebDriverException e) {
			System.out.println("Exception: " + e);
			throw new WebDriverException(e);
		}
	}

	public void switchToWindow() throws WebDriverException {
		try {
			this.driver.switchTo().window("msgFrame");
		} catch (WebDriverException e) {
			System.out.println("Exception: " + e);
			throw new WebDriverException(e);
		}
	}

	public void switchToWindow(String name) throws WebDriverException {
		try {
			this.driver.switchTo().window(name);
		} catch (WebDriverException e) {
			System.out.println("Exception: " + e);
			throw new WebDriverException(e);
		}
	}

	public void switchTo() throws WebDriverException {
		try {
			CurrentWindowHandle = this.driver.getWindowHandle();
			this.driver.switchTo().window(CurrentWindowHandle);
		} catch (WebDriverException e) {
			System.out.println("Exception: " + e);
			throw new WebDriverException(e);
		}
	}

	public void switchToWindowtitle(String windowtitle) throws WebDriverException {
		try {
			String mainWindowHandle = driver.getWindowHandles().iterator().next();
			driver.switchTo().window(mainWindowHandle);
			this.driver.switchTo().window(windowtitle);
		} catch (WebDriverException e) {
			System.out.println("Exception: " + e);
			throw new WebDriverException(e);
		}
	}

	public void switchToAlert() throws WebDriverException, InterruptedException {
		try {
			Thread.sleep(23000);
			Alert alert = this.driver.switchTo().alert();
			alert.accept();
		} catch (WebDriverException e) {
			System.out.println("Exception: " + e);
			throw new WebDriverException(e);
		}
	}

	public Object executeAsyncScript(String arg0, Object... arg1) throws WebDriverException {
		return null;
	}

	public Object executeScript(String arg0, Object... arg1) throws WebDriverException {
		JavascriptExecutor js = (JavascriptExecutor) this.driver;
		System.out.println("Java Script to be executed: " + arg0);
		return js.executeScript(arg0, arg1);
	}

	public String getWindowHandle() throws WebDriverException {
		try {
			return this.driver.getWindowHandle();
		} catch (WebDriverException e) {
			System.out.println("Exception: " + e);
			throw new WebDriverException(e);
		}
	}

	public Keyboard getKeyboard() throws WebDriverException {
		return ((HasInputDevices) this.driver).getKeyboard();
	}

	public Mouse getMouse() throws WebDriverException {
		return ((HasInputDevices) this.driver).getMouse();
	}

	public boolean isElementPresent(By by) throws WebDriverException {
		if (this.driver.findElements(by).size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void maximize() throws WebDriverException {
		this.driver.manage().window().maximize();
	}

	public String getText() throws WebDriverException {
		try {
			return this.driver.switchTo().alert().getText();
		} catch (WebDriverException e) {
			System.out.println("Exception" + e);
			throw new WebDriverException(e);
		}
	}

	public WebDriver getWindowText(String window) throws WebDriverException {
		try {
			return this.driver.switchTo().window(window);
		} catch (WebDriverException e) {
			System.out.println("Exception" + e);
			throw new WebDriverException(e);
		}
	}

	public void get(String url) throws WebDriverException {
		try {
			this.driver.get(url);
		} catch (WebDriverException e) {
			System.out.println("Exception" + e);
			throw new WebDriverException(e);
		}
	}

	public void switchTodefault() throws WebDriverException {
		try {
			driver.switchTo().defaultContent();
		} catch (WebDriverException e) {
			System.out.println("Exception" + e);
			throw new WebDriverException(e);
		}
	}

	// @SuppressWarnings("static-access")
	public void refresh() throws InterruptedException {
		try {
			this.action = new Actions(driver);
			Thread.sleep(5000);
			action.keyDown(Keys.CONTROL).sendKeys(Keys.F5).perform();
		} catch (WebDriverException e) {
			System.out.println("Exception" + e);
			throw new WebDriverException(e);
		}
	}

	public void setImplicitWait(int wait) {
		driver.manage().timeouts().implicitlyWait(wait, TimeUnit.SECONDS);
	}

	public void clickOnOverrideLink() throws WebDriverException {
		try {
			this.driver.get("javascript:document.getElementById('overridelink').click()");
		} catch (WebDriverException e) {
			System.out.println("Exception" + e);
			throw new WebDriverException(e);
		}
	}

	public void getScreenShotAs(String filepath) {
		try {
			File srcFile = ((TakesScreenshot) this.driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(srcFile, new File(filepath));
			// screenshotFile =
			// ((TakesScreenshot)this.driver).getScreenshotAs(file);
			// String screenshotBase64 =
			// ((TakesScreenshot)driver).getScreenshotAs(base64);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() throws WebDriverException {
		try {
			this.driver.close();
		} catch (WebDriverException e) {
			System.out.println("Exception" + e);
			throw new WebDriverException(e);
		}
	}

	public void switchTotitleWindow(String windowTitle) {
		Set<String> windows = this.driver.getWindowHandles();
		System.out.println("num of win" + windows.size());
		for (String window : windows) {
			System.out.println("Title of the page before - switchingTo: " + driver.getTitle());
			this.driver.switchTo().window(window);
			// iedriver.switchToWindow(window);
			if (driver.getTitle().contains(windowTitle)) {
				System.out.println("Title of the page after - switchingTo: " + driver.getTitle());
				return;
			}
		}
	}

	public boolean isAlertPresent() {
		try {
			this.driver.switchTo().alert();
			return true;
		} // try
		catch (NoAlertPresentException Ex) {
			return false;
		}
	}

}
