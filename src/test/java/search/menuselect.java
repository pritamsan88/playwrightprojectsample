package search;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class menuselect {
    Playwright playwright;
    Browser browser;


    @BeforeTest
    public void startup() {
        playwright = Playwright.create();
        browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));

    }

    @Test
    public void menuselect() throws InterruptedException {
        BrowserContext context = browser.newContext(new Browser.NewContextOptions().setViewportSize(1920, 1080));
        Page page = context.newPage();
        page.navigate("https://www.aardea.com/");
        Locator headerproductkink = page.locator(" ul > li.navitem.has-dropdown > a");
        Locator submenuitems = page.locator(" div.menuiteam.item-3 > div > ul >li");
        int submenu = submenuitems.count();
        System.out.println("total submenu items are: " + submenu);
        for (int i = 1; i < submenu; i++) {
            try {

                //headerproductkink.waitFor(new Locator.WaitForOptions().setTimeout(5000).setState(WaitForSelectorState.VISIBLE));
                headerproductkink.hover();
                headerproductkink.click();
                Thread.sleep(2000);

                Locator subitem = page.locator(" div.menuiteam.item-3 > div > ul>li").nth(i).locator("a");
                subitem.waitFor(new Locator.WaitForOptions().setTimeout(3000).setState(WaitForSelectorState.VISIBLE));
                System.out.println("submenu itmes are: " + subitem.textContent());

                if (subitem.isVisible()) {
                    System.out.println("submenu is visible");
                    page.waitForNavigation(() -> subitem.click());
                    page.goBack();

                } else {
                    System.out.println("submenu is not visible" + i);
                }
            }catch (Exception e)
            {
                System.out.println("issue in submenu itmes are: " + e.getMessage());
            }


        }


    }

    @AfterTest
    public void quit() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
        System.out.println("Test Sucessfully completed");

    }

}
