package br.com.lucas.blog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.lucas.blog.entity.Categoria;

/**
 * @author Lucas.
 * 
 *         Classe que representa uma interface de repositório Categoria com os métodos para acesso ao banco de Dados extendendo de JPARepository.
 * 
 */

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

	Categoria findByDescricao(String descricao);
	
	Page<Categoria> findAllByOrderByDescricaoAsc(Pageable pageable); //método para encontrar todas as descrições de forma Asc
		
}