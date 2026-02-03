package com.digibo.core.service.mock;

import com.digibo.core.service.ReportsService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Profile("mock")
public class ReportsServiceMock implements ReportsService {

    @Override
    public List<Map<String, Object>> unauthorizedConditions() {
        List<Map<String, Object>> conditions = new ArrayList<>();
        conditions.add(Map.of(
                "id", 1,
                "conditionName", "Mock Unauthorized Condition 1",
                "createdDate", "2024-01-15",
                "status", "PENDING"
        ));
        conditions.add(Map.of(
                "id", 2,
                "conditionName", "Mock Unauthorized Condition 2",
                "createdDate", "2024-01-20",
                "status", "PENDING"
        ));
        return conditions;
    }

    @Override
    public List<Map<String, Object>> unauthorizedUsers() {
        List<Map<String, Object>> users = new ArrayList<>();
        users.add(Map.of(
                "id", 1,
                "username", "mockuser1",
                "email", "mockuser1@example.com",
                "createdDate", "2024-01-10",
                "status", "UNAUTHORIZED"
        ));
        users.add(Map.of(
                "id", 2,
                "username", "mockuser2",
                "email", "mockuser2@example.com",
                "createdDate", "2024-01-12",
                "status", "UNAUTHORIZED"
        ));
        return users;
    }
}
