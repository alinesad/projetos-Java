package br.com.rest.forum.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.rest.forum.model.Usuario;
import br.com.rest.forum.repository.UsuarioRepository;

/*
 * No Spring, podemos herdar essa classe de outra chamada OncePerRequestFilter, que é um filtro do Spring chamado uma única vez a cada requisição.
 * 
 * Em classe tipo filter não dá para fazer injeção de dependencia. Por isso para usar o tokenService, vamos criar uma construtor dessa classe filter e quem for usar esse 
 * construtor terá de passar o token service como parâmetro
 * 
 * */
public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {
	
	
	private TokenService tokenService ;
	
	private UsuarioRepository repository;
	
	

	public AutenticacaoViaTokenFilter(TokenService tokenService,UsuarioRepository repository) {
	
		this.tokenService = tokenService;
		this.repository = repository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest httpRequest, HttpServletResponse httpResponse, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = recuperarToken(httpRequest);
		
		boolean valido = tokenService.isTokenValido(token);
		
		if(valido) {
			//Esse é o método para falar para o Spring considerar que está autenticado. 
			autenticarCliente(token);
		}
		
		
		filterChain.doFilter(httpRequest, httpResponse);
		
	}

	private void autenticarCliente(String token) {
		
		Long idUsuario = tokenService.getUsuario(token);
		
		Usuario usuario = repository.findById(idUsuario).get();
		
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
		/*
		 *  Spring tem uma classe chamada SecurityContextHolder.getContext().setAuthentication.
		 *   Esse é o método para falar para o Spring considerar que está autenticado. 
		 *   Só que aí preciso passar os dados do usuário
		 * 
		 * */
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
	}

	private String recuperarToken(HttpServletRequest httpRequest) {
		
		String token = httpRequest.getHeader("Authorization");
		
		// Nesserio validar que o token começa com Bearer - que é o tipo de autenticação
		if(token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
			return null;
		}
		return token.substring(7, token.length());
	}

}
