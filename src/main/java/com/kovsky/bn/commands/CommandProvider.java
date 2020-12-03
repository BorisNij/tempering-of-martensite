package com.kovsky.bn.commands;

import com.kovsky.bn.ApiService;
import com.kovsky.bn.AuthService;
import com.kovsky.bn.Cache;
import com.kovsky.bn.UserConsole;

import java.util.Map;

public class CommandProvider {
    private final UserConsole view;
    private final AuthService authService;
    private final ApiService apiService;
    private final Map<String, Cache> itemCaches;

    public CommandProvider(UserConsole view, AuthService authService, ApiService apiService, Map<String, Cache> itemCaches) {
        this.view = view;
        this.authService = authService;
        this.apiService = apiService;
        this.itemCaches = itemCaches;
    }
}
