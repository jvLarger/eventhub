package com.jlarger.eventhub.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
	
	private static final String REGEX_NUMERO_NA_STRING = ".*\\d.*";
	private static final String REGEX_EMAIL_VALIDO = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

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
	
	public static String formatarApresentacaoDeDataComHoraEDiaSemana(Date data, LocalTime hora) {
		
		Calendar dataBase = Calendar.getInstance();
		dataBase.setTime(data);
		
		String diaSemana = getDiaSemana(dataBase);
		String mes = getMes(dataBase);
		String horaFormatada = hora.format(TIME_FORMATTER);
		
		StringBuilder dataFormatada = new StringBuilder();
		dataFormatada.append(diaSemana.substring(0, 3));
		dataFormatada.append(", ");
		dataFormatada.append(dataBase.get(Calendar.DAY_OF_MONTH));
		dataFormatada.append(" de ");
		dataFormatada.append(mes);
		dataFormatada.append(" • ");
		dataFormatada.append(horaFormatada);
		
		return dataFormatada.toString();
	}

	private static String getDiaSemana(Calendar dataBase) {
		
		int diaSemana = dataBase.get(Calendar.DAY_OF_WEEK);

		switch (diaSemana) {
			case 1: {
				return "Domingo";
			}
			case 2: {
				return "Segunda-Feira";
			}
			case 3: {
				return "Terça-Feira";
			}
			case 4: {
				return "Quarta-Feira";
			}
			case 5: {
				return "Quinta-Feira";
			}
			case 6: {
				return "Sexta-Feira";
			}
			case 7: {
				return "Sábado";
			}
		}
		return null;
	}
	
	private static String getMes(Calendar dataBase) {
		
		int mes = dataBase.get(Calendar.MONTH);

		switch (mes) {
			case 0: {
				return "Janeiro";
			}
			case 1: {
				return "Fevereiro";
			}
			case 2: {
				return "Março";
			}
			case 3: {
				return "Abril";
			}
			case 4: {
				return "Maio";
			}
			case 5: {
				return "Junho";
			}
			case 6: {
				return "Julho";
			}
			case 7: {
				return "Agosto";
			}
			case 8: {
				return "Setembro";
			}
			case 9: {
				return "Outubro";
			}
			case 10: {
				return "Novembro";
			}
			case 11: {
				return "Dezembro";
			}
		}
		return null;
	}
	
}