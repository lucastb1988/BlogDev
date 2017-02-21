package br.com.lucas.blog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import br.com.lucas.blog.entity.Autor;

/**
 * @author Lucas.
 * 
 *         Classe que representa uma interface de repositório Autor com os métodos para acesso ao banco de Dados extendendo de JPARepository.
 * 
 */

public interface AutorRepository extends JpaRepository<Autor, Long> {
	
	Autor findByUsuario(Long id);

	Page<Autor> findAllByOrderByNomeAsc(Pageable pageable);
	
	Autor findByNome(String nome);
	
	@Modifying
	@Query("update Autor a set a.nome = ?1, a.biografia = ?2 where a.id = ?3")
	void updateNomeAndBiografia(String nome, String biografia, Long id);	
}