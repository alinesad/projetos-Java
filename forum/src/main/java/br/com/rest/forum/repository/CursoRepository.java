package br.com.rest.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.rest.forum.model.Curso;

/*
 * Não precisa de anotação por que é interface.
 * O spring consegue encontrar automaticamente
 * */
public interface CursoRepository extends JpaRepository<Curso, Long>  {


	/*
	 * Como está usando o padrão de nomeclatura do SpringData não precisa montar a query. A entidade Curso tem um atributo Nome
	 * */
	Curso findByNome(String nomeCurso);

}
