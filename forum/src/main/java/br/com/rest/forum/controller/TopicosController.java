package br.com.rest.forum.controller;

import java.net.URI;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.rest.forum.dto.DetalhesDoTopicoDto;
import br.com.rest.forum.dto.TopicoDto;
import br.com.rest.forum.form.AtualizacaoTopicoForm;
import br.com.rest.forum.form.TopicoForm;
import br.com.rest.forum.model.Topico;
import br.com.rest.forum.repository.CursoRepository;
import br.com.rest.forum.repository.TopicoRepository;

/*
 * EndPoint que vai ser o endereço que vai devolver a lista com todos os tópicos que estão cadastrados no sistema
 * */
@RestController
@RequestMapping("/topicos")
public class TopicosController {

	@Autowired
	private TopicoRepository topicoRepository;

	@Autowired
	private CursoRepository cursoRepository;

	/*
	 * @RequestParam essa anotação indica que o parâmetro passado no request é
	 * obrigatorio, se não for inforado irá retornar erro . COmo no nosso caso ainda
	 * deverá ser opcional, vamos colocar required false exemplo da requisição no
	 * postmam: http://localhost:8080/topicos?page=0&size=10&sort=id,desc
	 */
	@GetMapping 
	@Cacheable(value = "listaDeTopicos")
	public Page<TopicoDto> list(@RequestParam(required = false) String nomeCurso,
			@PageableDefault(sort = "id", direction = Direction.DESC, page = 0, size=10) Pageable paginacao) {

		// Topico topico = new Topico("Dúvida","Dúvida de Spring", new Curso("Spring",
		// "Programacao"));
		// return TopicoDto.converter(Arrays.asList(topico,topico,topico));

		// Pageable paginacao = PageRequest.of(pagina, qtd,Direction.ASC, ordenacao);

		if (nomeCurso == null) {
			Page<Topico> topicos = topicoRepository.findAll(paginacao);
			return TopicoDto.converter(topicos);
		} else {
			Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso, paginacao);

			return TopicoDto.converter(topicos);
		}

	}

	/*
	 * Exemplo de criação de um EndPointer ou API REP de POST para cadastrar novo
	 * topico. Necessário @Valid para indicar que a classe usa Bean vlaidation para
	 * validar campos
	 * 
	 * @RequestBody essa anotação indica que os parametros vem no corpo da
	 * requisição no formato Json
	 */
	@PostMapping
	@Transactional
	@CacheEvict(value = "listaDeTopicos", allEntries = true)
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {

		Topico topico = form.converter(cursoRepository);
		topicoRepository.save(topico);

		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		/*
		 * Faz dessa forma para devolver uma resposta mais apropriada no lugar de http
		 * 200 pois nesse caso a resposta foi com sucesso e um novo recurso foi criado
		 * no servidor ( novo Topico).
		 */
		return ResponseEntity.created(uri).body(new TopicoDto(topico));

	}

	@GetMapping("/{id}")
	@CacheEvict(value = "listaDeTopicos", allEntries = true)
	public ResponseEntity<DetalhesDoTopicoDto> detalhar(@PathVariable Long id) {
		/*
		 * Significa que pode ser que exista o elemento ou não.
		 */
		Optional<Topico> topico = topicoRepository.findById(id);
		if (topico.isPresent()) {
			return ResponseEntity.ok(new DetalhesDoTopicoDto(topico.get()));
		}

		return ResponseEntity.notFound().build();
	}

	@PutMapping("/{id}")
	@Transactional
	@CacheEvict(value = "listaDeTopicos", allEntries = true)
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) {
		Optional<Topico> optional = topicoRepository.findById(id);
		if (optional.isPresent()) {
			Topico topico = form.atualizar(id, topicoRepository);
			return ResponseEntity.ok(new TopicoDto(topico));
		}

		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	@Transactional
	@CacheEvict(value = "listaDeTopicos", allEntries = true)
	public ResponseEntity<?> remover(@PathVariable Long id) {
		Optional<Topico> optional = topicoRepository.findById(id);
		if (optional.isPresent()) {
			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}

		return ResponseEntity.notFound().build();
	}

}
