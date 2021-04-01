package com.yojana.helpers;

public class FloatHelper {
	public static Float add(Float a, Float b) {
		if (a == null && b == null) {
			return 0.0f;
		} else if (a == null) {
			return b;
		} else if (b == null) {
			return a;
		} else {
			return a + b;
		}
	}

}
