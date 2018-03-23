package br.com.ufal.util;

public class Date {
	private int month = 1;
	private int day = 1;
	private int year = 2018;
	
	public Date(String date) {
		
		if( date.length() == 10 ) {
			
			int day = Integer.valueOf( date.substring(0, 2) );
			int month = Integer.valueOf( date.substring(3, 5) );
			int year = Integer.valueOf( date.substring(6) );
			
			int[] dayPerMonth = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
			
			if(month <= 0 || month > 12) System.out.printf("Mês inválido", month);
			if(day == 29 && (year%4 != 0 || (year%100 == 0 && year%400 != 0))) System.out.println("Ano não bissexto");
			if(day <= 0 || day > dayPerMonth[month]) System.out.println("Dia inválido");
			
			this.month = month;
			this.day = day;
			this.year = year;
			
		} else {
			System.out.println("Data inválida");
		}
		
	}
	
	public int getMonth() {
		return month;
	}
	public int getDay() {
		return day;
	}
	public int getYear() {
		return year;
	}	
	
}
