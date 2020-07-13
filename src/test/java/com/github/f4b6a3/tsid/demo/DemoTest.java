package com.github.f4b6a3.tsid.demo;

import com.github.f4b6a3.tsid.TsidCreator;

public class DemoTest {

	private static final String HORIZONTAL_LINE = "----------------------------------------";

	public static void printList() {
		int max = 100;

		System.out.println(HORIZONTAL_LINE);
		System.out.println("### TSID");
		System.out.println(HORIZONTAL_LINE);

		for (int i = 0; i < max; i++) {
			System.out.println(TsidCreator.getTsid1024());
		}

		System.out.println(HORIZONTAL_LINE);
		System.out.println("### TSID string");
		System.out.println(HORIZONTAL_LINE);

		for (int i = 0; i < max; i++) {
			System.out.println(TsidCreator.getTsidString1024());
		}
	}

	public static void main(String[] args) {
		printList();
	}
}
