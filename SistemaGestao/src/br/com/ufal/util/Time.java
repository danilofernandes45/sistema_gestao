package br.com.ufal.util;

public class Time {
	private int hour = 0;
	private int minute = 0;
	
	public Time(String time) {
		
		if(time.length() == 5) {
			
			int hour = Integer.valueOf( time.substring(0, 2) );
			int minute = Integer.valueOf( time.substring(3) );
			
			if(hour > 23 || hour < 0 || minute < 0 || minute > 59) System.out.println("Hora inválida");
			
			this.hour = hour;
			this.minute = minute;
			
		} else {
			System.out.println("Hora inválida");
		}
		
	}

	public int getHour() {
		return hour;
	}

	public int getMinute() {
		return minute;
	}

}
