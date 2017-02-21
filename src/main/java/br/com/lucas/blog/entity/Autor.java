package br.com.lucas.blog.entity;

import java.util.List;

import javax.persistence.*;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * @author Lucas.
 * 
 *         Classe que representa uma Entidade Autor.
 * 
 */

@Entity
@Table(name = "autores")
public class Autor extends AbstractPersistable<Long> {
	
	@NotBlank
	@Length(min = 3, max = 50)
	@Column(nullable = false, unique = true, length = 50)
	private String nome;
	
	@NotBlank(message = "Este campo não aceita valor em branco.")
	@Length(min = 5, max = 255, message = "Este campo aceita entre 5 e 255 caracteres.")
	@Column(nullable = false, length = 255)
	private String biografia;
	
	@OneToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;
	
	@OneToMany(mappedBy = "autor")
	private List<Postagem> postagens;
	
	@Override
	public void setId(Long id) {
		super.setId(id);
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the biografia
	 */
	public String getBiografia() {
		return biografia;
	}

	/**
	 * @param biografia the biografia to set
	 */
	public void setBiografia(String biografia) {
		this.biografia = biografia;
	}

	/**
	 * @return the usuario
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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