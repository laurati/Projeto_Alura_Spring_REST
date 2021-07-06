package br.com.alura.forum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.forum.modelo.Topico;

public interface TopicoRepository extends JpaRepository<Topico, Long>{
	
	List<Topico> findByCursoNome (String nomeCurso);
	//aqui navego pelos relacionamentos: tenho atributo curso na classe topico
	//e dentro da classe curso, tenho atributo nome
	//o jpa vai montando os joins necessarios

}
