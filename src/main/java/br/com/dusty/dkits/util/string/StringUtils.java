package br.com.dusty.dkits.util.string;

import br.com.dusty.dkits.Main;
import br.com.dusty.dkits.util.text.Text;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.ArrayList;

public class StringUtils {
	
	private static final char[] ALPHANUMERIC = "ABCDEFGHIKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
	
	/**
	 * Divide uma {@link String} em outras (não-formatadas) de tamanho máximo 'max', inserindo-as, em ordem, em uma {@link ArrayList}.
	 *
	 * @param s
	 * @param max
	 * @return
	 */
	public static ArrayList<String> fancySplit(String s, int max) {
		ArrayList<String> arrayList = new ArrayList<>();
		
		String[] fragments = Text.clearFormatting(s).split(" ");
		for(int i = 0; i < fragments.length; i++){
			StringBuilder fragment = new StringBuilder();
			fragment.append(fragments[i]);
			
			while(i + 1 < fragments.length && (fragment.length() + 1 + fragments[i + 1].length()) <= max){
				fragment.append(" ");
				fragment.append(fragments[i + 1]);
				i++;
			}
			
			arrayList.add(fragment.toString());
		}
		
		return arrayList;
	}
	
	/**
	 * Gera uma {@link String} alfanumérica aleatória com diferenciação entre maiúsculas e minúsculas.
	 *
	 * @param length
	 * @return
	 */
	public static String randomString(int length) {
		StringBuilder stringBuilder = new StringBuilder(length);
		
		for(int i = 0; i <= length - 1; i++){
			int index = Main.RANDOM.nextInt(ALPHANUMERIC.length);
			stringBuilder.append(ALPHANUMERIC[index]);
		}
		
		return stringBuilder.toString();
	}
	
	/**
	 * Cria uma {@link String} que indica uma duração de tempo com dias, horas, minutos e segundos.
	 *
	 * @param millis
	 * @return
	 */
	public static String period(long millis) {
		PeriodFormatter periodFormatter = new PeriodFormatterBuilder().appendDays()
		                                                              .appendSuffix("d")
		                                                              .appendSeparator(" ")
		                                                              .appendHours()
		                                                              .appendSuffix("h")
		                                                              .appendSeparator(" ")
		                                                              .appendMinutes()
		                                                              .appendSuffix("min")
		                                                              .appendSeparator(" ")
		                                                              .appendSeconds()
		                                                              .appendSuffix("s")
		                                                              .toFormatter();
		
		Period period = new Period(millis);
		
		return periodFormatter.print(period.toPeriod());
	}
	
	/**
	 * Cria uma {@link ArrayList} contendo todas as {@link String}'s do parâmetro 'arrayList' que iniciarem com o parâmetro 'start'.
	 *
	 * @param start
	 * @param arrayList
	 * @return
	 */
	public static ArrayList<String> sortOut(String start, ArrayList<String> arrayList) {
		for(String s : arrayList){
			if(!s.toLowerCase().startsWith(start.toLowerCase())){
				arrayList.remove(s);
			}
		}
		
		arrayList.sort(String::compareToIgnoreCase);
		
		return arrayList;
	}
}
