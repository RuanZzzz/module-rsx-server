package com.modulersx.service;

import java.util.Map;

public interface RedisDemoService {

    Map<String, Object> ping();

    Map<String, Object> setDemoValue(String value);

    Map<String, Object> getDemoValue();
}
