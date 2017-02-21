package br.com.lucas.blog.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * @author Lucas.
 * 
 *         Classe que representa uma Entidade Comentario.
 * 
 */

@Entity
@Table(name = "comentarios")
public class Comentario extends AbstractPersistable<Long> implements Comparable<Comentario> {

	@NotBlank
	@Length(min = 5, max = 255, message = "Seu comentário deve conter entre 5 e 255 caracteres.")
	@Column(nullable = false, columnDefinition = "TEXT")
	private String texto;
	
	@Column(name = "data_comentario")
	private LocalDateTime dataComentario;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Postagem postagem;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Usuario usuario;
	
	public int compareTo(Comentario comentario) {

		return comentario.getDataComentario().compareTo(this.dataComentario); //compara a data de comentário de forma recente para mais antigo / para fazer ao contário necessita alterar o retorno para (this.dataComentario.compareTo(comentario.getDataComentario())
	}	
		
	@Override
	public void setId(Long id) {
		super.setId(id);
	}

	/**
	 * @return the texto
	 */
	public String getTexto() {
		return texto;
	}

	/**
	 * @param texto the texto to set
	 */
	public void setTexto(String texto) {
		this.texto = texto;
	}

	/**
	 * @return the dataComentario
	 */
	public LocalDateTime getDataComentario() {
		return dataComentario;
	}

	/**
	 * @param dataComentario the dataComentario to set
	 */
	public void setDataComentario(LocalDateTime dataComentario) {
		this.dataComentario = dataComentario;
	}

	/**
	 * @return the postagem
	 */
	public Postagem getPostagem() {
		return postagem;
	}

	/**
	 * @param postagem the postagem to set
	 */
	public void setPostagem(Postagem postagem) {
		this.postagem = postagem;
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
}