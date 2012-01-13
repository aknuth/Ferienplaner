package de.dwpbank.wpdirect.servlet;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Person {
	public Person(){}
	public Person(String name, int holidays){
		this.name=name;
		this.holidays=holidays;
	}
	private String name;
	private int holidays;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public int getHolidays() {
		return holidays;
	}
	public void setHolidays(int holidays) {
		this.holidays = holidays;
	}
}
