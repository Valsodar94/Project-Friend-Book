package com.friendBook.model;

import java.util.Random;

public interface RandomCodeGenerator {
	static int generateRandomCode() {
		Random rand = new Random();
		return rand.nextInt(8000000) + 10000000;
	}
}