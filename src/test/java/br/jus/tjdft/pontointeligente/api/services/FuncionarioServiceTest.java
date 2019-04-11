package br.jus.tjdft.pontointeligente.api.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.jus.tjdft.pontointeligente.api.entities.Empresa;
import br.jus.tjdft.pontointeligente.api.entities.Funcionario;
import br.jus.tjdft.pontointeligente.api.enums.PerfilEnum;
import br.jus.tjdft.pontointeligente.api.repositories.EmpresaRepository;
import br.jus.tjdft.pontointeligente.api.repositories.FuncionarioRepository;
import br.jus.tjdft.pontointeligente.api.utils.PasswordUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FuncionarioServiceTest {

	private static final String CNPJ = "36167790000141";

	private static final String CPF = "12345678909";

	private static final String EMAIL = "email1@email.com";

	@Autowired
	private EmpresaService empresaService;

	@Autowired
	private FuncionarioService funcionarioService;
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	@Before
	public void setUp() {
		Empresa empresa = new Empresa();
		empresa.setRazaoSocial("Empresa de exemplo");
		empresa.setCnpj(CNPJ);
		empresa = this.empresaService.persistir(empresa);
		
		Funcionario funcionario = obterDadosFuncionario("Funcionario 1", CPF, EMAIL);
		funcionario.setEmpresa(empresa);
		
		funcionario = this.funcionarioService.persistir(funcionario);
	}
	
	@After
	public final void tearDown() {
		this.funcionarioRepository.deleteAll();
		this.empresaRepository.deleteAll();
	}
	
	@Test
	public void testPersistFuncionario() {
		Funcionario funcionario = obterDadosFuncionario("Funcionario 2", "45117332016", "email2@email.com");
		
		Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(CNPJ);
		funcionario.setEmpresa(empresa.get());
		
		funcionarioService.persistir(funcionario);
		assertNotNull(funcionario);
	}

	@Test
	public void testBuscarFuncionarioPorId() {
		Optional<Funcionario> funcionario = this.funcionarioService.buscarPorId(2L);
		assertTrue(funcionario.isPresent());
	}
	
	@Test
	public void testBuscarFuncionarioPorEmail() {
		Optional<Funcionario> funcionario = this.funcionarioService.buscarPorEmail(EMAIL);
		assertTrue(funcionario.isPresent());
	}
	
	@Test
	public void testBuscarFuncionarioPorCpf() {
		Optional<Funcionario> funcionario = this.funcionarioService.buscarPorCpf(CPF);
		assertTrue(funcionario.isPresent());
	}
		
	private Funcionario obterDadosFuncionario(String nome, String cpf, String email) {
		Funcionario funcionario = new Funcionario();
		funcionario.setNome(nome);
		funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
		funcionario.setSenha(PasswordUtils.gerarBCrypt("123456"));
		funcionario.setCpf(cpf);
		funcionario.setEmail(email);
		
		return funcionario;
	}
	
}
