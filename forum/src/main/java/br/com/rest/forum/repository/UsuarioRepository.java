package br.com.rest.forum.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.rest.forum.model.Usuario;

/*
 * Não precisa de anotação por que é interface.
 * O spring consegue encontrar automaticamente
 * */
public interface UsuarioRepository extends JpaRepository<Usuario, Long>  {

	Optional<Usuario> findByEmail(String email);

}
