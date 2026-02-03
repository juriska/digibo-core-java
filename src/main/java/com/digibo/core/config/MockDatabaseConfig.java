package com.digibo.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * MockDatabaseConfig - Configuration for mock profile
 * When mock profile is active, no database connection is required
 */
@Configuration
@Profile("mock")
public class MockDatabaseConfig {
    // No beans needed - mock services don't use database
    // This class exists to document the mock profile behavior
}
