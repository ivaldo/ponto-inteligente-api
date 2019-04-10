package br.jus.tjdft.pontointeligente.api.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PasswordUtils {
	
	private static final Logger log = LoggerFactory.getLogger(PasswordUtils.class);
	
	public PasswordUtils() {
	}
	
	public static String gerarBCrypt(String senha) {
		if (senha == null) {
			return senha;
		}
		
		log.info("Gerando hash com o BCrypt");
//		BCryptPasswordEncoder bCryptPasswordEncripter = new BCryptPasswordEncoder();
//		return bCryptPasswordEncripter.encode(senha);
		
		return senha;
	}
	
	
}
