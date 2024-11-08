package com.sparta.nanglangeats.domain.user.enums;

public enum UserRole {

	CUSTOMER(Authority.CUSTOMER),
	OWNER(Authority.OWNER),
	MANAGER(Authority.MANAGER),
	MASTER(Authority.MASTER)
	;

	private final String authority;

	UserRole(String authority) {
		this.authority = authority;
	}

	public String getAuthority() {
		return authority;
	}

	private static class Authority {
		public static final String CUSTOMER = "ROLE_CUSTOMER";
		public static final String OWNER = "ROLE_OWNER";
		public static final String MANAGER = "ROLE_MANAGER";
		public static final String MASTER = "ROLE_MASTER";
	}
}
