/*
 *  Copyright (c) 2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.wso2.es.ui.integration.test.common;


import org.openqa.selenium.By;
import org.testng.annotations.*;
import org.wso2.carbon.automation.extensions.selenium.BrowserManager;
import org.wso2.es.ui.integration.util.BaseUITestCase;
import org.wso2.es.ui.integration.util.ESWebDriver;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Create a new asset in publisher and publish it to store
 * Check if it can be seen store side and verify details
 */
public class ESRBACAsReviewerTestCase extends BaseUITestCase {

    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {
        super.init("superTenant", "reviewer1");

        currentUserName = userInfo.getUserName().split("@")[0];
        currentUserPwd = userInfo.getPassword();
        driver = new ESWebDriver(BrowserManager.getWebDriver());
        baseUrl = getWebAppURL();
    }

    @Test(groups = "wso2.es.store", description = "verify login to ES Store")
    public void testLoginToPublisherAsReviewer() throws Exception {
        driver.get(baseUrl + PUBLISHER_GADGET_LIST_PAGE);
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys(currentUserName);
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(currentUserPwd);
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        assertEquals("Asset | WSO2 Enterprise Store Publisher", driver.getTitle());
        assertEquals(currentUserName, driver.findElement(By.xpath("/html/body/div/div[1]/header/div/div[2]/div/a/div/span"))
                .getText(), "Logged in user not shown");

    }

    @Test(groups = "wso2.es.publisher", description = "verify not being able to add asset")
    public void testRestrictAddAssetAsReviewer() throws Exception {
        driver.get(baseUrl + PUBLISHER_GADGET_LIST_PAGE);
        assertTrue(!isElementPresent(driver, By.id("Addgadget")), "User who has only internal/reviewer role can add " +
                "asset.");

    }

    @Test(groups = "wso2.es.publisher", description = "verify not being able to edit asset")
    public void testRestrictEditAssetAsReviewer() throws Exception {
        driver.get(baseUrl + PUBLISHER_GADGET_LIST_PAGE);
        driver.findElement(By.cssSelector("h3.ast-name")).click();
        assertTrue(!isElementPresent(driver, By.id("Edit")), "User who has only internal/reviewer role can edit " +
                "asset.");

    }
    @Test(groups = "wso2.es.publisher", description = "verify not being able to version asset")
    public void testRestrictVersionAssetAsReviewer() throws Exception {
        driver.get(baseUrl + PUBLISHER_GADGET_LIST_PAGE);
        driver.findElement(By.cssSelector("h3.ast-name")).click();
        assertTrue(!isElementPresent(driver, By.id("Version")), "User who has only internal/reviewer role can version" +
                "asset.");

    }

    @Test(groups = "wso2.es.store", description = "verify not being able to login to store")
    public void testRestrictLoginToStoreAsReviewer() throws Exception {
        driver.get(baseUrl + STORE_TOP_ASSETS_PAGE);
        driver.findElement(By.cssSelector("span.ro-text")).click();
        assertEquals(driver.findElement(By.cssSelector("h3")).getText(), "You do not have permission to login to this" +
                " application.Please contact your administrator and request permission.");
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() throws Exception {
        driver.get(baseUrl + PUBLISHER_LOGOUT_URL);
        driver.quit();
    }

}