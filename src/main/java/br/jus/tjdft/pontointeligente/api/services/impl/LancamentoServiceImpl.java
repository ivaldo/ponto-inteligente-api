package br.jus.tjdft.pontointeligente.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.jus.tjdft.pontointeligente.api.entities.Lancamento;
import br.jus.tjdft.pontointeligente.api.repositories.LancamentoRepository;
import br.jus.tjdft.pontointeligente.api.services.LancamentoService;

@Service
public class LancamentoServiceImpl implements LancamentoService {
	
	private static final Logger log = LoggerFactory.getLogger(LancamentoServiceImpl.class);

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Override
	public Page<Lancamento> buscarPorFuncionarioId(Long funcionarioId, PageRequest pageRequest) {
		if (log.isInfoEnabled()) log.info("Buscando lancamentos do funcionario ID {} ", funcionarioId);
		return lancamentoRepository.findByFuncionarioId(funcionarioId, pageRequest);
	}

	@Override
	public Optional<Lancamento> buscarPorId(Long id) {
		if (log.isInfoEnabled()) log.info("Buscando lancamentos ID {} ", id);
		return Optional.ofNullable(lancamentoRepository.getOne(id));
	}

	@Override
	public Lancamento persistir(Lancamento lancamento) {
		if (log.isInfoEnabled()) log.info("Persistindo lancamento {} ", lancamento);
		return lancamentoRepository.save(lancamento);
	}

	@Override
	public void remover(Long id) {
		if (log.isInfoEnabled()) log.info("Removendo o lancamento ID {} ", id);
		lancamentoRepository.deleteById(id);
	}

}
