package de.dwpbank.wpdirect.servlet;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Day {
	private int index;
	private int absenseType;
	private String month;
	/**public Day(){};
	public Day(int index,int absenseType,String month){
		this.index = index;
		this.absenseType = absenseType;
		this.month = month;
	}*/
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
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
}
