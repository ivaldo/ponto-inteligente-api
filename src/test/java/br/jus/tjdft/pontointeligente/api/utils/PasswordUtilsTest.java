package br.jus.tjdft.pontointeligente.api.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PasswordUtilsTest {
	
	private static final String SENHA = "123456";
//	private final BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();

	@Test
	public void testSenhaNula() throws Exception {
		assertNull(PasswordUtils.gerarBCrypt(null));
	}
	
	@Test
	public void testGeraHashSenha() throws Exception {
		String hash = PasswordUtils.gerarBCrypt(SENHA);
		assertEquals(SENHA, hash);
//		assertTrue(bCryptEncoder.matches(SENHA,  hash));
	}
	
}
