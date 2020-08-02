package br.com.rest.forum.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.rest.forum.model.Topico;

/*
 * Não precisa de anotação por que é interface.
 * O spring consegue encontrar automaticamente
 * */
public interface TopicoRepository extends JpaRepository<Topico, Long>  {

	/*
	 * Filtra pelo atributo de um relacionamento 
	 */
	Page<Topico> findByCursoNome(String nome, Pageable paginacao );
	
	@Query("SELECT t FROM Topico t WHERE t.curso = :nomeCurso")
	List<Topico> carregarPorNomeDoCurso(@Param("nomeCurso") String nomeCurso);

}
