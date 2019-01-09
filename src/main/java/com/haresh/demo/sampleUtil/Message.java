package com.haresh.demo.sampleUtil;

public class Message {

	private String name;
	private String value;
	
	public Message(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public Message(String name) {
        this.name = name;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

}
