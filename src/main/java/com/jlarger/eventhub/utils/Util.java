package com.jlarger.eventhub.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
	
	private static final String REGEX_NUMERO_NA_STRING = ".*\\d.*";
	private static final String REGEX_EMAIL_VALIDO = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
	
	public static Boolean isExisteNumeroNoTexto(String texto) {
		
		Pattern pattern = Pattern.compile(REGEX_NUMERO_NA_STRING);
       
		Matcher matcher = pattern.matcher(texto);
        
        return matcher.matches();
	}

	public static Boolean isExisteLetraMaiusculaNoTexto(String texto) {
		
		Boolean isExisteLetraMaiusculaNoTexto = Boolean.FALSE;
		
		for (char c : texto.toCharArray()) {
            
			if (Character.isUpperCase(c)) {
				isExisteLetraMaiusculaNoTexto = Boolean.TRUE;
				break;
            }
			
        }
		
        return isExisteLetraMaiusculaNoTexto;
	}

	public static Boolean isEmailValido(String email) {
		
		Pattern pattern = Pattern.compile(REGEX_EMAIL_VALIDO);
		
        Matcher matcher = pattern.matcher(email);
        
        return matcher.matches();
	}
	
	public static String leftPad(String input, int length, char padChar) {
       
		if (input.length() >= length) {
            return input;
        }
        
        StringBuilder padded = new StringBuilder();
        
        int paddingAmount = length - input.length();
       
        for (int i = 0; i < paddingAmount; i++) {
            padded.append(padChar);
        }
        
        padded.append(input);
        
        return padded.toString();
    }
	
	public static String getSomenteNumeros(String texto) {
		
		Pattern pattern = Pattern.compile("\\d+");
		
		Matcher matcher = pattern.matcher(texto);

        StringBuilder numerosString = new StringBuilder();

        while (matcher.find()) {
            numerosString.append(matcher.group());
        }
        
        return numerosString.toString();
	}
	
	public static Integer comprarDatasSemHora(Date dataUm, Date dataDois) {
		
		Calendar calendarDataUm = Calendar.getInstance();
		calendarDataUm.setTime(dataUm);
		calendarDataUm.set(Calendar.HOUR_OF_DAY, 0);
		calendarDataUm.set(Calendar.MINUTE, 0);
		calendarDataUm.set(Calendar.SECOND, 0);
		calendarDataUm.set(Calendar.MILLISECOND, 0);
		
		Calendar calendarDataDois = Calendar.getInstance();
		calendarDataDois.setTime(dataDois);
		calendarDataDois.set(Calendar.HOUR_OF_DAY, 0);
		calendarDataDois.set(Calendar.MINUTE, 0);
		calendarDataDois.set(Calendar.SECOND, 0);
		calendarDataDois.set(Calendar.MILLISECOND, 0);
		
		return calendarDataUm.getTime().compareTo(calendarDataDois.getTime());
	}
	
}