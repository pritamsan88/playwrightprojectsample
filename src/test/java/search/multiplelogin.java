package search;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class multiplelogin {

    Playwright playwright;
    Browser browser;

    @BeforeTest
    public void setup() {
        playwright = Playwright.create();


        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));


    }

    @Test
    public void login() throws InterruptedException {
        BrowserContext context = browser.newContext(new Browser.NewContextOptions().setViewportSize(1920, 1080));
        Page page = context.newPage();

        List<String[]> users = new ArrayList<>();
        users.add(new String[]{"sanyal.pritam538@gmail.com", "Sanyal88@"});
        users.add(new String[]{"sanyal.pritam358@gmail.com", "Sanyal88@"});
        users.add(new String[]{"abc44@gmail.com", "Sanyal88@"});

        for (String[] user : users) {

            String email = user[0];
            String password = user[1];

            page.navigate("https://www.aardea.com/");
            page.click(" div.account_btn.sec_nav > a");
            page.fill("#CustomerEmail", email);
            page.fill("#CustomerPassword", password);
            Locator loginbutton = page.locator(".arda-crte");
            loginbutton.scrollIntoViewIfNeeded();
            loginbutton.click();
            Locator logoutLink = page.locator(".log_out > a");
            if (logoutLink.count() > 0 && logoutLink.isVisible()) {
                System.out.println("✅ Login successful: " + email);

                Locator orderdetails = page.locator("#orders-tab");
                orderdetails.click();


                Locator noOrderText = page.locator("#order_details > div > p");
                Locator orderdetailstable = page.locator("#order_details > div > table > thead > tr");
                Locator orderdetailsdata = page.locator(" #order_details > div > table > tbody > tr");
                Locator orderids = page.locator("#RowOrder");

                try {
                    // Try to wait for the "no order" text
                    noOrderText.waitFor(new Locator.WaitForOptions().setTimeout(5000).setState(WaitForSelectorState.VISIBLE));

                    // If it appears, treat it as "no orders"
                    String text = noOrderText.textContent().trim();
                    System.out.println("📦 No order has been placed: " + text);

                } catch (PlaywrightException e) {
                    // If wait times out, then assume orders exist and print headers
                    List<String> texts = orderdetailstable.allInnerTexts();
                    List<String> ordertexts = orderdetailsdata.allInnerTexts();
                    //List<String> orderids=orderid.allInnerTexts();


                    System.out.print("📦 Order details found: ");
                    System.out.println();
                    for (int i = 0; i < texts.size(); i++) {
                        //System.out.println(texts.size());
                        System.out.print(texts.get(i).trim());

                       /* if (i < texts.size() - 1) {
                            System.out.print(" | ");
                        }*/
                        System.out.println();

                    }
                    for (int i = 0; i < ordertexts.size(); i++) {
                        System.out.println(ordertexts.get(i).trim());
                        /*if (i < ordertexts.size() - 1) {
                            System.out.print(" | ");
                        }*/
                    }
                    System.out.println();
                    int count = orderids.count();
                    for (int i = 0; i < count; i++) {
                        String text = orderids.nth(i).textContent().trim();
                        System.out.println("Order ID: " + text);
                        if (text != null && !text.isEmpty() && text.contains("#1121")) {
                            System.out.println("order id is present in the list");
                            orderids.nth(i).click();
                            System.out.println("Clicked on order ID: " + text);
                            page.waitForURL("https://www.aardea.com/account/orders/**");
                            Assert.assertTrue(page.url().contains("https://www.aardea.com/account/orders/"), "Order details page not opened");
                            System.out.println("Order details page opened successfully");
                            Assert.assertTrue(text.contains("#1121"), "Order ID does not match");
                            page.goBack();
                            break;

                        } else {
                            System.out.println("order id not present in the list");
                        }
                    }


                }
                page.waitForSelector(".log_out > a");
                logoutLink.click();

            } else {
                System.out.println("❌ Login failed for " + email + password);
            }


        }
    }

    @AfterTest
    public void teardown() {
        if (browser != null) {
            browser.close(); // Closes the browser
        }
        if (playwright != null) {
            playwright.close(); // Closes Playwright instance
        }
        {
            System.out.println("Test completed sucessfully");
        }
    }

}

