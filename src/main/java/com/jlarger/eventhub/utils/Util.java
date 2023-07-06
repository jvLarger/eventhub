package com.jlarger.eventhub.utils;

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

}