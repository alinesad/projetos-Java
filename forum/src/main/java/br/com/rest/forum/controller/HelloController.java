package br.com.rest.forum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/*
 * Aanotação fara o Spring faça o gerenciamento da classe
 * */
@Controller
public class HelloController {
	
	/*
	 * @RequestMapping - Para indicar a URL que fará o spring chamar esse método
	 * @ResponseBody -topicoO spring saberá que a string do método deve ser devolvida direto para o navegador
	 * */
	@RequestMapping("/")
	@ResponseBody
	public String hello() {
		return "Hello world";
	}

}
