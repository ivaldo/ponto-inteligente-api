package br.jus.tjdft.pontointeligente.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.jus.tjdft.pontointeligente.api.entities.Funcionario;
import br.jus.tjdft.pontointeligente.api.repositories.FuncionarioRepository;
import br.jus.tjdft.pontointeligente.api.services.FuncionarioService;

@Service
public class FuncionarioServiceImpl implements FuncionarioService {

	private static final Logger log = LoggerFactory.getLogger(FuncionarioServiceImpl.class);

	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Override
	public Funcionario persistir(Funcionario funcionario) {
		if (log.isInfoEnabled()) log.info("Persistindo o funcionario {}", funcionario);
		return this.funcionarioRepository.save(funcionario);
	}

	@Override
	public Optional<Funcionario> buscarPorCpf(String cpf) {
		if (log.isInfoEnabled()) log.info("Buscando o funcionário pelo CPF {}", cpf);
		return Optional.ofNullable(funcionarioRepository.findByCpf(cpf));
	}

	@Override
	public Optional<Funcionario> buscarPorEmail(String email) {
		if (log.isInfoEnabled()) log.info("Buscando o funcionário pelo email {}", email);
		return Optional.ofNullable(funcionarioRepository.findByEmail(email));
	}

	@Override
	public Optional<Funcionario> buscarPorId(Long id) {
		if (log.isInfoEnabled()) log.info("Buscando o funcionário pelo ID {}", id);
		return funcionarioRepository.findById(id);
	}
	
}
