package br.jus.tjdft.pontointeligente.api.services;

import java.util.Optional;

import br.jus.tjdft.pontointeligente.api.entities.Funcionario;

public interface FuncionarioService {

	/**
	 * Persiste o funcionário
	 * 
	 * @param funcionario
	 * @return
	 */
	Funcionario persistir(Funcionario funcionario);
	
	/**
	 * Busca o funcionário pelo CPF
	 * 
	 * @param cpf
	 * @return
	 */
	Optional<Funcionario> buscarPorCpf(String cpf);
	
	/**
	 * Busca o funcionário pelo e-mail
	 * 
	 * @param email
	 * @return
	 */
	Optional<Funcionario> buscarPorEmail(String email);

	/**
	 * Busca o fucionário pelo ID
	 * 
	 * @param id
	 * @return
	 */
	Optional<Funcionario> buscarPorId(Long id);
}
