package br.com.rest.forum.config.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.rest.forum.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

	/*
	 * 
	 * Para injetar coisas, parâmetros do Application.properties, usamos a
	 * anotação @Value, e ela recebe como parâmetro o nome da propriedade.
	 * 
	 */
	@Value("${forum.jwt.expiration}")
	private String expiration;

	@Value("${forum.jwt.secret}")
	private String secret;

	public String gerarToken(Authentication authenticate) {

		Date hoje = new Date();

		Date DataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));

		Usuario user = (Usuario) authenticate.getPrincipal(); // authenticate.getPrincipal() devolve dados do usuar
																// logado
		return Jwts.builder().setIssuer("API do Forum da Aluna ") // Indica quem gerou o token
				.setSubject(user.getId().toString()) // Indica quem é o usuário do token
				.setIssuedAt(hoje) // Data em que o token foi criado
				.setExpiration(DataExpiracao) // Data em que o Token irá expirar
				.signWith(SignatureAlgorithm.HS256, secret) // Indica qual é o algoritimo de criptografia do token e a
															// senha da aplicação
				.compact(); /// compacta tudo e transforma numa string

		/*
		 * 
		 * Pela especificação JSON webtoken, o token tem que ser criptografado. Preciso
		 * dizer para ele quem é o algoritmo de criptografia e a senha da minha
		 * aplicação, que é usada para fazer a assinatura e gerar o REST da criptografia
		 * do token
		 */
	}

	public boolean isTokenValido(String token) {

		try {

			/*
			 * O parser Vai descriptografar o token e verificar se está OK
			 */
			Jwts.parser().setSigningKey(this.secret) // chave para descriptografar
					.parseClaimsJws(token); // se o token esativer valido devolve o objeto, saenão devolve uma exception
			return true;

		} catch (Exception e) {
			return false;
		}

	}

	public Long getUsuario(String token) {

		try {

			/*
			 * O parser Vai descriptografar o token e verificar se está OK
			 */
			Claims claims = Jwts.parser().setSigningKey(this.secret) // chave para descriptografar
					.parseClaimsJws(token).getBody(); // se o token esativer valido devolve o objeto, saenão devolve uma exception
			return Long.parseLong(claims.getSubject());

		} catch (Exception e) {
			return null;
		}

	}

}
