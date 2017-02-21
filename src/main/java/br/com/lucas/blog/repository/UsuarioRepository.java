package br.com.lucas.blog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import br.com.lucas.blog.entity.Avatar;
import br.com.lucas.blog.entity.Usuario;

/**
 * @author Lucas.
 * 
 *         Classe que representa uma interface de repositório Usuário com os métodos para acesso ao banco de Dados extendendo de JPARepository.
 * 
 */

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	Page<Usuario> findAllByOrderByNomeAsc(Pageable pageable);

	Usuario findByEmail(String email);

	Usuario findByAvatar(Avatar avatar);

	@Modifying //informa que trata-se de uma query que será alterada
	@Query("update Usuario u set u.nome = ?1, u.email = ?2 where u.id = ?3") //informa que será setado nome e email informando o respectivo id
	void updateNomeAndEmail(String nome, String email, Long id);

	@Modifying //informa que trata-se de uma query que será alterada
	@Query("update Usuario u set u.senha = ?1 where u.id = ?2") //informa que será setado a senha informando o respectivo id
	void updateSenha(String senha, Long id);
}