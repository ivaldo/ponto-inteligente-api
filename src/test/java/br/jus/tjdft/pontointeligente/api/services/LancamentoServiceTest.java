package br.jus.tjdft.pontointeligente.api.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.jus.tjdft.pontointeligente.api.entities.Lancamento;
import br.jus.tjdft.pontointeligente.api.repositories.LancamentoRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LancamentoServiceTest {

	@MockBean
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Before
	public void setUp() {
		BDDMockito.given(this.lancamentoRepository.findByFuncionarioId(Mockito.anyLong(), Mockito.any(Pageable.class))).willReturn(new PageImpl<Lancamento>(new ArrayList<Lancamento>()));
		BDDMockito.given(this.lancamentoRepository.getOne(Mockito.anyLong())).willReturn(new Lancamento());
		BDDMockito.given(this.lancamentoRepository.save(Mockito.any(Lancamento.class))).willReturn(new Lancamento());
	}
	
	@After
	public final void tearDown() {
		this.lancamentoRepository.deleteAll();
	}
	
	@Test
	public void testBuscarLancamentoPorFuncionarioId() {
		Page<Lancamento> lancamentos = this.lancamentoService.buscarPorFuncionarioId(1L, PageRequest.of(0, 10));
		assertNotNull(lancamentos);
	}
	
	@Test
	public void testBuscarLancamentoPorId() {
		Optional<Lancamento> lancamento = this.lancamentoService.buscarPorId(1L);
		assertTrue(lancamento.isPresent());
	}
	
	@Test
	public void testPersistirLancamento() {
		Lancamento lancamento = this.lancamentoService.persistir(new Lancamento());
		assertNotNull(lancamento);
	}
	
}
