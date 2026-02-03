package com.digibo.core.service;

import java.util.List;
import java.util.Map;

public interface ReportsService {

    List<Map<String, Object>> unauthorizedConditions();

    List<Map<String, Object>> unauthorizedUsers();
}
