package br.com.lucas.blog.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.lucas.blog.entity.Postagem;

/**
 * @author Lucas.
 * 
 *         Classe que representa uma interface de repositório Postagem com os métodos para acesso ao banco de Dados extendendo de JPARepository.
 * 
 */

public interface PostagemRepository extends JpaRepository<Postagem, Long> {
	
	Page<Postagem> findAllByOrderByDataPostagemDesc(Pageable pageable);

	Postagem findByPermalink(String permalink);

	List<Postagem> findByCategoriasPermalink(String link);

	List<Postagem> findByAutorNome(String nome);

	Page<Postagem> findAllByCategoriasPermalinkOrderByDataPostagemDesc(Pageable pageable, String permalink);

	Page<Postagem> findAllByAutorIdOrderByDataPostagemDesc(Pageable pageable, Long id);

	Page<Postagem> findByTextoContainingIgnoreCaseOrderByDataPostagemDesc(String texto, Pageable pageable);

	Page<Postagem> findAllByTituloContainingIgnoreCaseOrderByDataPostagemDesc(Pageable pageable, String titulo);

	Page<Postagem> findAllByAutorIdAndTituloContainingIgnoreCaseOrderByDataPostagemDesc(Pageable pageable, Long id,
			String titulo);	
}