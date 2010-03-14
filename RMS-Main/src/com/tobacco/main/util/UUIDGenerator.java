package com.tobacco.main.util;

import java.util.UUID;

public class UUIDGenerator {

	public static String generateUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();

	}

}
