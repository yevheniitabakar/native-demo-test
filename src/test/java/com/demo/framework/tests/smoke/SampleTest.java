package com.demo.framework.tests.smoke;

import com.demo.framework.tests.BaseTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class SampleTest extends BaseTest {

    @Test(description = "Framework bootstrap sanity check")
    public void driverStarts() {
        assertNotNull(driver(), "Driver should be initialized for the test session");
    }
}
