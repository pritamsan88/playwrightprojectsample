package KeweEnergy;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Random;


public class EntoEndTest {
    Playwright playwright;
    Browser browser;

    @BeforeTest
    public void browserstartup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));


    }

    @Test
    public void operation() {
        BrowserContext context = browser.newContext(new Browser.NewContextOptions().setViewportSize(1920, 1080));
        Page page = context.newPage();
        page.navigate("https://keweenergy.com/");
        Locator homepage = page.locator("text=Home");
        page.waitForNavigation(() -> homepage.click());
        //// validated home page or not
        Locator headericon = page.locator("#offcanvasResponsive > ul > li");
        int count = headericon.count();
        if (count > 0) {
            Locator option = headericon.nth(0);
            Locator optclass = option.locator("a");
            String active = optclass.getAttribute("class");
            if (active != null && active.contains("active")) {
                System.out.println("You are in home page");

            }
        }
        Locator shoppage = page.locator(" a.headershop_btn");
        page.waitForNavigation(() -> shoppage.click());
        System.out.println("You are in shop page");

        Locator listofproduct = page.locator(" div.card-body > a");
        int allproductcount = listofproduct.count();

        for (int i = 0; i < 3; i++) {

            Random rand = new Random();
            int random = rand.nextInt(allproductcount);
            Locator productselect = listofproduct.nth(random);
            productselect.scrollIntoViewIfNeeded();
            System.out.println("Clicked on product: " + productselect.innerText());
            page.waitForNavigation(() -> productselect.click());
            Locator addtocart = page.locator("//span[contains(text(),'Add to cart')]");
            addtocart.click();
            Locator progressbar=page.locator(".progress-done");
            try {
                progressbar.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            }catch (PlaywrightException e)
            {
                System.out.println("⚠️ Progress bar did not become visible within timeout.");
                page.waitForTimeout(2000);
                page.goBack();
                return;

            }
            String progressPercent = progressbar.getAttribute("data-done");
            System.out.println(" In cart Progress: " + progressPercent + "%");
            int progress=Integer.parseInt(progressPercent);
            if(progress>=50)
            {
                System.out.println("Progress bar reached at least 50%");
            }
            else {
                System.out.println("Progress bar did not reach 50%");
            }
            Locator totalprice=page.locator("#cart_total > p.price");
            if(totalprice.isVisible())
            {
                String price = totalprice.innerText();
                System.out.println("All total price is found :"+price);

            }else
            {
                System.out.println("All total price not found :");
            }
            page.waitForTimeout(20000);
            page.goBack();


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
    }
}
