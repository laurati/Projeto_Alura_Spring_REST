package br.com.alura.forum.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.AtualizacaoTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

@RestController
@RequestMapping("/topicos")
public class TopicosController {
	
	@Autowired
	private TopicoRepository topicoRepository;
	
	@Autowired
	private CursoRepository cursoRepository;

	/*@RequestMapping("/topicos")
	public List<TopicoDto> lista(){
		List<Topico> topicos = topicoRepository.findAll();
		return TopicoDto.converter(topicos);
	}*/
	
		
	@GetMapping
	public List<TopicoDto> lista(String nomeCurso){
		if(nomeCurso == null) {
			List<Topico> topicos = topicoRepository.findAll();
			return TopicoDto.converter(topicos);
		}else {
			List<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso);
			return TopicoDto.converter(topicos);
		}
	}
	
	//TopicoDto - dados que saem da api de volta pro cliente
	//O parametro do get vem da URL, por isso nao tem o @RequestBody
	
	/*@PostMapping
	public void cadastrar(@RequestBody TopicoForm form) {
		Topico topico = form.converter(cursoRepository);
		topicoRepository.save(topico);
	}*/
	//RequestBody - Indicar ao Spring que os par??metros enviados no corpo da 
	//requisi????o devem ser atribu??dos ao par??metro do m??todo. Estou indicando
	//que os par??metros que passei est??o vindo do corpo da requisi????o.
	
	//TopicoForm - ?? um dto de dados que chegam do cliente para a api
	
	//N??o ?? legal esse m??todo ser void, pois quero retornar um c??digo
	//de resposta personalizado. Para m??todos void, ser?? devolvida uma 
	//resposta sem conte??do, juntamente com o c??digo HTTP 200 (OK), caso 
	//a requisi????o seja processada com sucesso.
	
	
//  @ResponseBody (acima do m??todo) Indicar ao Spring que o 
//	retorno do m??todo deve ser devolvido como resposta.
//	Obs: Por padr??o, o Spring considera que o retorno do m??todo 
//	?? o nome da p??gina que ele deve carregar, mas ao utilizar a 
//	anota????o @ResponseBody, indicamos que o retorno do m??todo 
//	deve ser serializado e devolvido no corpo da resposta.
//	Mas se uso a anota????o @RestController a?? o ele j?? tem a 
//	anota????o @ResponseBody embutida.
	
	
	@PostMapping
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {
	    Topico topico = form.converter(cursoRepository);
	    topicoRepository.save(topico);

		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}
	
	//O @Valid serve para avisar ao spring que irei utilizar as validacoes
	//que passei no TopicoForm
	
	//Obs: No postman, O cabe??alho Content-Type (header) serve para indicar 
	//o tipo de conte??do que est?? sendo enviado ao servidor (application/json).
	
	
	@GetMapping("/{id}")
	public ResponseEntity<TopicoDto> detalhar(@PathVariable Long id) {
		Optional <Topico> optional = topicoRepository.findById(id);
		if(optional.isPresent()) {
			return ResponseEntity.ok(new TopicoDto(optional.get())); 
		}
		return ResponseEntity.notFound().build();
	}
	//Optional - pode ser que tenha um registro ou nao. Se tiver, ir?? retornar
	//200, se n??o tiver, retorna 404.
	
	//@PathVariable avisa ao spring que ser?? passado um parametro apos a url /topicos
	
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) {
		Optional <Topico> optional = topicoRepository.findById(id);
		if(optional.isPresent()) {
			Topico topico = form.atualizar(id, topicoRepository);
			return ResponseEntity.ok(new TopicoDto(topico));
		}
		return ResponseEntity.notFound().build();    
	}
	
	//@transactional serve para comitar a atualiza????o no banco de dados
	//posso colocar ele no post e delete tb.
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<TopicoDto> remover(@PathVariable Long id) {
		Optional <Topico> optional = topicoRepository.findById(id);
		if(optional.isPresent()) {
			topicoRepository.deleteById(id);
	        return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build(); 
	}
	
	
	
	
	
}
