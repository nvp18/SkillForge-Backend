package com.skillforge.backend;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({
        "com.skillforge.backend.unittests"
})
public class AllTests {
}
