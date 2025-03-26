package com.github.lpgflow.infrastructure.security;

import java.util.List;

public interface AuthenticatedUserProvider {

    String getCurrentUserName();

    List<String> getCurrentUserRoles();
}
