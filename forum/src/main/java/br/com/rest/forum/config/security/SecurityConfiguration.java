package br.com.rest.forum.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.rest.forum.repository.UsuarioRepository;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private AutenticacaoService autenticacaoService;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	// configuração de autenticação
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());

	}

	// configurações de autorização
	/*
	 * O método anyRequest().authenticated() indica ao Spring Security para bloquear
	 * todos os endpoints que não foram liberados anteriormente com o método
	 * permitAll();
	 * 
	 * 
	 * 
	 * 
	 * Csrf é uma abreviação para cross-site request forgery, que é um tipo de
	 * ataque hacker que acontece em aplicações web
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/topicos").permitAll()
				.antMatchers(HttpMethod.GET, "/topicos/*").permitAll().antMatchers(HttpMethod.POST, "/auth*")
				.permitAll().anyRequest().authenticated() // Demais requisições cliente deverá está autenticado
				// .and().formLogin(); -- não vamos mais usar a autenticação tradicional, onde é
				// solicitado o formulario de login
				.and().csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, usuarioRepository),
						UsernamePasswordAuthenticationFilter.class); // Para o spring que quando fizer autenticação não
																		// é para criar sessão
	}

	// configuração de recurasos estaticos ex. Reqauisições para arquivos de css ,
	// ja script , imagens
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/**.html", "/v2/api-docs", "/webjars/**", "/configuration/**",
				"/swagger-resources/**");
	}

	/*
	 * Criado só para conseguir gerar o hash da senha para o algoritimo usado.
	 * 
	 * Geralmente se utiliza algum algoritmo para gerar um HASH dessa senha e salva
	 * no banco de dados a senha com o HASH.
	 *  O algoritimo que usei foi o BCrypt Esse main() foi só para gerar a senha e colocar no arquivo sql data.sql
	 */
	// public static void main(String[] args) {
	// System.out.println(new BCryptPasswordEncoder().encode("123456"));
	//
	// }

}
