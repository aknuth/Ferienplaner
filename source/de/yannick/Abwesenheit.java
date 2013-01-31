package de.yannick;

public class Abwesenheit {
	public int year;
	public int month;
	public int day;
	public int abwesenheit;
	public int personenID;
	
	@Override
	public int hashCode() {
		String result = Integer.toString(year)+Integer.toString(month)+Integer.toString(day)+Integer.toString(personenID);
		return Integer.parseInt(result);
	}
	
	@Override
	public boolean equals(Object obj) {
		Abwesenheit aw = (Abwesenheit)obj;
		if (aw.year==year && aw.month==month && aw.day==day && aw.personenID==personenID){
			return true;
		} else {
			return false;
		}
	}
}
