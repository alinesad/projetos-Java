package br.com.rest.forum.controller;



import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.rest.forum.config.security.TokenService;
import br.com.rest.forum.controller.dto.TokenDto;
import br.com.rest.forum.form.LoginForm;

@Controller
@RequestMapping("/auth")
public class AutenticacaoController {
	
	@Autowired
	private AuthenticationManager autManager;
	
	@Autowired
	private TokenService tokenService;
	
	@PostMapping
	public ResponseEntity<TokenDto> autenticar(@RequestBody @Valid LoginForm form){
		
		UsernamePasswordAuthenticationToken dadosLogin = form.converter();
		
		try {
			Authentication authenticate = autManager.authenticate(dadosLogin);
			
			String token = tokenService.gerarToken(authenticate);
			//System.out.println(token);
			/*
			 * Além do token, também precisamos dizer para o cliente qual o tipo de autenticação, como ele vai fazer a autenticação nas próximas requisições.
			 *  No http existe uma parte que fala sobre autenticação. 
			 * Tem vários métodos. Um deles é o bearer. Junto com o token, eu vou mandar para ele o tipo de autenticação que ele tem que fazer nas próximas requisições. 
			 * */
			return ResponseEntity.ok(new TokenDto(token,"Bearer"));
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
		
		
		
		
	}

}
