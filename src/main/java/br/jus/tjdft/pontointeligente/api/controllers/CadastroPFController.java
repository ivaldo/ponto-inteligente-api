package br.jus.tjdft.pontointeligente.api.controllers;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.jus.tjdft.pontointeligente.api.dtos.CadastroPFDto;
import br.jus.tjdft.pontointeligente.api.entities.Empresa;
import br.jus.tjdft.pontointeligente.api.entities.Funcionario;
import br.jus.tjdft.pontointeligente.api.enums.PerfilEnum;
import br.jus.tjdft.pontointeligente.api.response.Response;
import br.jus.tjdft.pontointeligente.api.services.EmpresaService;
import br.jus.tjdft.pontointeligente.api.services.FuncionarioService;
import br.jus.tjdft.pontointeligente.api.utils.PasswordUtils;

@RestController
@CrossOrigin(origins = "*")
public class CadastroPFController {
	
	private static final Logger log = LoggerFactory.getLogger(CadastroPFController.class);
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@Autowired
	private EmpresaService empresaService;
	
	public CadastroPFController() {
	}
	
	/**
	 * Cadastra uma pessoa física no sistema.
	 * 
	 * @param dto
	 * @param result
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping(path = "/api/cadastrar-pf", produces ="application/json")
	public ResponseEntity<Response<CadastroPFDto>> cadastrar(@Valid @RequestBody CadastroPFDto dto, 
			BindingResult result) throws NoSuchAlgorithmException {
		
		if (log.isInfoEnabled()) log.info("Cadastrando PF {}", dto);
		Response<CadastroPFDto> response = new Response<CadastroPFDto>();
		
		validarDadosExistentes(dto, result);
		Funcionario funcionario = this.converterDtoParaFuncionario(dto);
		
		if (result.hasErrors()) {
			if (log.isWarnEnabled()) log.warn("Dados de cadastro PJ inválidos {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		Optional<Empresa> empresa = empresaService.buscarPorCnpj(dto.getCnpj());
		empresa.ifPresent(emp -> funcionario.setEmpresa(emp));
		this.funcionarioService.persistir(funcionario);
		
		response.setData(this.converterCadastroPJDto(funcionario));
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Verifica se a empresa ou funcionário já existem na base de dados.
	 * 
	 * @param dto
	 * @param result
	 */
	private void validarDadosExistentes(@Valid CadastroPFDto dto, BindingResult result) {
		Optional<Empresa> empresa = empresaService.buscarPorCnpj(dto.getCnpj());
		
		if (!empresa.isPresent()) {
			result.addError(new ObjectError("empresa", "Empresa não cadastrada."));
		}
		
		this.funcionarioService.buscarPorCpf(dto.getCpf())
		.ifPresent(emp -> result.addError(new ObjectError("funcionario", "CPF já existente.")));
		
		this.funcionarioService.buscarPorEmail(dto.getEmail())
		.ifPresent(emp -> result.addError(new ObjectError("funcionario", "E-mail já existente.")));

	}

	/**
	 * Converte os dados do DTO para funcionário.
	 * 
	 * @param dto
	 * @return
	 */
	private Funcionario converterDtoParaFuncionario(@Valid CadastroPFDto dto) {
		Funcionario funcionario = new Funcionario();
		try {
			BeanUtils.copyProperties(funcionario, dto);
		} catch (IllegalAccessException | InvocationTargetException e) {
			if (log.isErrorEnabled()) log.error(ExceptionUtils.getStackTrace(e));
		}
		funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
		funcionario.setSenha(PasswordUtils.gerarBCrypt(dto.getSenha()));
		dto.getQtHorasAlmoco().ifPresent(qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtdHorasAlmoco)));
		dto.getQtdHorasTrabalhoDia().ifPresent(qtdHorasTrabalhoDia -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(qtdHorasTrabalhoDia)));
		dto.getValorHora().ifPresent(valorHora -> funcionario.setValorHora(new BigDecimal(valorHora)));
		return funcionario;
	}

	/**
	 * Popula o DTO de cadastro com os dados do funcionário e empresa.
	 * 
	 * @param funcionario
	 * @return
	 */
	private CadastroPFDto converterCadastroPJDto(Funcionario funcionario) {
		CadastroPFDto dto = new CadastroPFDto();
		try {
			BeanUtils.copyProperties(dto, funcionario);
		} catch (IllegalAccessException | InvocationTargetException e) {
			if (log.isErrorEnabled()) log.error(ExceptionUtils.getStackTrace(e));
		}
		dto.setCnpj(funcionario.getEmpresa().getCnpj());
		return dto;
	}

}
