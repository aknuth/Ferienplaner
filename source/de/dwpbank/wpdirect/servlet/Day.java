package de.dwpbank.wpdirect.servlet;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Day {
	private int day;
	private int absenseType;
	private String month;
	private String name;
	public Day(){};
	public Day(int day,int absenseType,String month, String name){
		this.day = day;
		this.absenseType = absenseType;
		this.month = month;
		this.name = name;
	}
	public int getAbsenseType() {
		return absenseType;
	}
	public void setAbsenseType(int absenseType) {
		this.absenseType = absenseType;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	@Override
	public int hashCode() {
		int result = 2;
		result = 37 * result + (int)(day>>>32);
		result = 37 * result + (int)(absenseType>>>32);
		result = 37*result + month.hashCode(); 
		result = 37*result + name.hashCode(); 
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		Day d = (Day)obj;
		return d.day==day && d.month.equals(month) && d.name.equals(name) && d.absenseType==absenseType; 
	}
	
	@Override
	public String toString() {
		return name+"|"+month+"|"+day+"|"+absenseType;
	}
}
