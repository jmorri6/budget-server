package com.jmorri6.pojo;

public class Note {
	private int id;
	private String desc;

	public Note(int id, String desc) {
		super();
		this.id = id;
		this.desc = desc;
	}
	
	public int getId() {
		return id;
	}
	public String getDesc() {
		return desc;
	}
}
