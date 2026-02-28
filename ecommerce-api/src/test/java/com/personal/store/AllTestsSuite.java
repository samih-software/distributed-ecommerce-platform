package com.personal.store;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("junit-jupiter")
@SelectClasses({
        AuthenticationControllerIntegrationTest.class,
        OrderFlowIntegrationTest.class,

})
public class AllTestsSuite {
}