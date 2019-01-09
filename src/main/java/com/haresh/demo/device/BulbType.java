package com.haresh.demo.device;

public enum BulbType {
	RED("red-bulb"),GREEN("green-bulb"),BLUE("blue-bulb");
	
	private String bulbName;
	
	private BulbType(String bulbName) {
		this.bulbName = bulbName;
	}
	
	public String getBulbName() {
		return bulbName;
	}
}
