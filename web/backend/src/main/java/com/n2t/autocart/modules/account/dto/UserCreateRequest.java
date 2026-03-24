package com.n2t.autocart.modules.account.dto;

import java.util.Set;

public record UserCreateRequest(
	String email,
	String password,
	Set<Integer> roleIds
) {
	public UserCreateRequest {
		roleIds = roleIds == null ? Set.of() : Set.copyOf(roleIds);
	}
}
