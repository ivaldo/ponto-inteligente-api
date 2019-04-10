package br.jus.tjdft.pontointeligente.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.jus.tjdft.pontointeligente.api.entities.Empresa;
import br.jus.tjdft.pontointeligente.api.repositories.EmpresaRepository;
import br.jus.tjdft.pontointeligente.api.services.EmpresaService;

@Service
public class EmpresaServiceImpl implements EmpresaService {

	private static final Logger log = LoggerFactory.getLogger(EmpresaServiceImpl.class);
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	@Override
	public Optional<Empresa> buscarPorCnpj(String cnpj) {
		if (log.isInfoEnabled()) log.info("Buscando uma empresa para o CNPJ {}", cnpj);
		return Optional.ofNullable(empresaRepository.findByCnpj(cnpj));
	}

	@Override
	public Empresa persistir(Empresa empresa) {
		if (log.isInfoEnabled()) log.info("Persistindo a empresa {}", empresa);
		return this.empresaRepository.save(empresa);
	}

}
