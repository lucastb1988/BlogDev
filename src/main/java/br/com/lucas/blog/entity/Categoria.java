package br.com.lucas.blog.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * @author Lucas.
 * 
 *         Classe que representa uma Entidade Categoria.
 * 
 */

@Entity
@Table(name = "categorias")
public class Categoria extends AbstractAuditoria<Long> {

	@NotBlank
	@Length(min = 3, max = 30)
	@Column(nullable = false, unique = true, length = 30)
	private String descricao;

	@Column(nullable = false, unique = true, length = 30)
	private String permalink;

	@ManyToMany
	@JoinTable(
			name = "postagens_has_categorias", //nomeando a tabela Postagem/Categoria
			joinColumns = @JoinColumn(name = "categoria_id"), //relacionando a tabela referente a esta classe
			inverseJoinColumns = @JoinColumn(name = "postagem_id") //relacionando a tabela inversa referente a outra classe 
			)
	private List<Postagem> postagens;

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * @return the permalink
	 */
	public String getPermalink() {
		return permalink;
	}

	/**
	 * @param permalink the permalink to set
	 */
	public void setPermalink(String permalink) {
		this.permalink = permalink;
	}

	/**
	 * @return the postagens
	 */
	public List<Postagem> getPostagens() {
		return postagens;
	}

	/**
	 * @param postagens the postagens to set
	 */
	public void setPostagens(List<Postagem> postagens) {
		this.postagens = postagens;
	}
}