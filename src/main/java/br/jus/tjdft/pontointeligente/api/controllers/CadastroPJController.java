package br.jus.tjdft.pontointeligente.api.controllers;

import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;

import javax.validation.Valid;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.jus.tjdft.pontointeligente.api.dtos.CadastroPJDto;
import br.jus.tjdft.pontointeligente.api.entities.Empresa;
import br.jus.tjdft.pontointeligente.api.entities.Funcionario;
import br.jus.tjdft.pontointeligente.api.enums.PerfilEnum;
import br.jus.tjdft.pontointeligente.api.response.Response;
import br.jus.tjdft.pontointeligente.api.services.EmpresaService;
import br.jus.tjdft.pontointeligente.api.services.FuncionarioService;
import br.jus.tjdft.pontointeligente.api.utils.PasswordUtils;

@RestController
@CrossOrigin(origins = "*")
public class CadastroPJController {
	
	private static final Logger log = LoggerFactory.getLogger(CadastroPJController.class);
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@Autowired
	private EmpresaService empresaService;
	
	public CadastroPJController() {
	}

	@RequestMapping(value="/api/hello", produces ="application/json")
    public ResponseEntity<String> greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new ResponseEntity<String>("hello " + name, HttpStatus.OK);
    }
	
	/**
	 * Cadastra uma pessoa jurídica no sistema.
	 * 
	 * @param dto
	 * @param result
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping(path = "/api/cadastrar-pj")
	public ResponseEntity<Response<CadastroPJDto>> cadastrar(@Valid @RequestBody CadastroPJDto dto, 
			BindingResult result) throws NoSuchAlgorithmException {
		
		if (log.isInfoEnabled()) log.info("Cadastrando PJ {}", dto);
		Response<CadastroPJDto> response = new Response<CadastroPJDto>();
		
		validarDadosExistentes(dto, result);
		Empresa empresa = this.converterDtoParaEmpresa(dto);
		Funcionario funcionario = this.converterDtoParaFuncionario(dto);
		
		if (result.hasErrors()) {
			if (log.isWarnEnabled()) log.warn("Dados de cadastro PJ inválidos {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		this.empresaService.persistir(empresa);
		funcionario.setEmpresa(empresa);
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
	private void validarDadosExistentes(@Valid CadastroPJDto dto, BindingResult result) {
		this.empresaService.buscarPorCnpj(dto.getCnpj())
		.ifPresent(emp -> result.addError(new ObjectError("empresa", "Empresa já existente.")));
		
		this.funcionarioService.buscarPorCpf(dto.getCpf())
		.ifPresent(emp -> result.addError(new ObjectError("funcionario", "CPF já existente.")));
		
		this.funcionarioService.buscarPorEmail(dto.getEmail())
		.ifPresent(emp -> result.addError(new ObjectError("funcionario", "E-mail já existente.")));

	}

	/**
	 * Converte os dados do DTO para empresa.
	 * 
	 * @param dto
	 * @return
	 */
	private Empresa converterDtoParaEmpresa(@Valid CadastroPJDto dto) {
		Empresa empresa = new Empresa();
		try {
			BeanUtils.copyProperties(empresa, dto);
		} catch (IllegalAccessException | InvocationTargetException e) {
			if (log.isErrorEnabled()) log.error(ExceptionUtils.getStackTrace(e));
		}
		return empresa;
	}

	/**
	 * Converte os dados do DTO para funcionário.
	 * 
	 * @param dto
	 * @return
	 */
	private Funcionario converterDtoParaFuncionario(@Valid CadastroPJDto dto) {
		Funcionario funcionario = new Funcionario();
		try {
			BeanUtils.copyProperties(funcionario, dto);
		} catch (IllegalAccessException | InvocationTargetException e) {
			if (log.isErrorEnabled()) log.error(ExceptionUtils.getStackTrace(e));
		}
		funcionario.setPerfil(PerfilEnum.ROLE_ADMIN);
		funcionario.setSenha(PasswordUtils.gerarBCrypt(dto.getSenha()));
		return funcionario;
		
	}

	/**
	 * Popula o DTO de cadastro com os dados do funcionário e empresa.
	 * 
	 * @param funcionario
	 * @return
	 */
	private CadastroPJDto converterCadastroPJDto(Funcionario funcionario) {
		CadastroPJDto dto = new CadastroPJDto();
		try {
			BeanUtils.copyProperties(dto, funcionario);
		} catch (IllegalAccessException | InvocationTargetException e) {
			if (log.isErrorEnabled()) log.error(ExceptionUtils.getStackTrace(e));
		}
		dto.setCnpj(funcionario.getEmpresa().getCnpj());
		dto.setRazaoSocial(funcionario.getEmpresa().getRazaoSocial());
		return dto;
	}

}
