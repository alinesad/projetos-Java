package br.com.rest.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * Para receber os parâmetros de ordenação e paginação diretamente nos métodos
 *  do controller, devemos habilitar o módulo SpringDataWebSupport, adicionando a 
 *  anotação @EnableSpringDataWebSupport na classe ForumApplication.
 *  
 *  @EnableCaching - Habilita o uso de cache na aplicação
 *  
 *  @EnableSwagger2 - Habilita o swagger no nosso projeto. Ele é uma ferramenta para documentar os EndPointes
 *  
 * @author aline.s.divino
 *
 */

@SpringBootApplication
@EnableSpringDataWebSupport
@EnableCaching
@EnableSwagger2
public class ForumApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForumApplication.class, args);
	}

}
