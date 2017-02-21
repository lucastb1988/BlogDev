package br.com.lucas.blog.entity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Lucas.
 * 
 *         Classe que representa uma Entidade Postagem.
 * 
 */

@Entity
@Table(name = "postagens")
public class Postagem extends AbstractPersistable<Long> {

	@NotBlank
	@Length(min = 5, max = 60)
	@Column(nullable = false, unique = true, length = 60)
	private String titulo;
	
	//columnDefinition foi criado para informar ao hibernate/bd que será permitido um texto maior do que o tradicional (varchar 255)
	@NotBlank
	@Column(nullable = false, columnDefinition = "LONGTEXT")
	private String texto;
	
	@Column(nullable = false, unique = true, length = 60)
	private String permalink;
	
	@DateTimeFormat(iso = ISO.DATE_TIME, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@Column(name = "data_postagem", nullable = false)
	private LocalDateTime dataPostagem;
	
	@ManyToOne
	@JoinColumn(name = "autor_id")
	private Autor autor;	
	
	//@JsonIgnore	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "postagens_has_categorias", //nomeando a tabela Postagem/Categoria
			joinColumns = @JoinColumn(name = "postagem_id"), //relacionando a tabela referente a esta classe
			inverseJoinColumns = @JoinColumn(name = "categoria_id") //relacionando a tabela inversa referente a outra classe 
			)
	private List<Categoria> categorias;
	
	@OneToMany(mappedBy = "postagem", fetch = FetchType.EAGER)
	private List<Comentario> comentarios;
	
	@Override
	public void setId(Long id) {
		super.setId(id);
	}

	/**
	 * @return the titulo
	 */
	public String getTitulo() {
		return titulo;
	}

	/**
	 * @param titulo the titulo to set
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
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
	 * @return the dataPostagem
	 */
	public LocalDateTime getDataPostagem() {
		return dataPostagem;
	}

	/**
	 * @param dataPostagem the dataPostagem to set
	 */
	public void setDataPostagem(LocalDateTime dataPostagem) {
		this.dataPostagem = dataPostagem;
	}

	/**
	 * @return the autor
	 */
	public Autor getAutor() {
		return autor;
	}

	/**
	 * @param autor the autor to set
	 */
	public void setAutor(Autor autor) {
		this.autor = autor;
	}

	/**
	 * @return the categorias
	 */
	public List<Categoria> getCategorias() {
		return categorias;
	}

	/**
	 * @param categorias the categorias to set
	 */
	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

	/**
	 * @return the comentarios
	 */
	public List<Comentario> getComentarios() {
		
		if(comentarios != null) {
			Collections.sort(comentarios); //ordena a lista de comentarios por postagem na forma desejada (Asc ou Desc)
		}		
		
		return comentarios;
	}

	/**
	 * @param comentarios the comentarios to set
	 */
	public void setComentarios(List<Comentario> comentarios) {
		this.comentarios = comentarios;
	}		
}