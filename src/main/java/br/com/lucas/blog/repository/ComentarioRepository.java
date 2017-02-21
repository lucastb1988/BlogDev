package br.com.lucas.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.lucas.blog.entity.Comentario;

/**
 * @author Lucas.
 * 
 *         Classe que representa uma interface de repositório Comentário com os métodos para acesso ao banco de Dados extendendo de JPARepository.
 * 
 */

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

}