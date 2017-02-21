package br.com.lucas.blog.entity;

import javax.persistence.*;

import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Lucas.
 * 
 *         Classe que representa uma Entidade Avatar.
 * 
 */

@Entity
@Table(name = "avatares")
public class Avatar extends AbstractPersistable<Long> {

	@Column(nullable = false)
	private String titulo;
	
	@Column(nullable = false)
	private String tipo;
	
	@Lob
	@Column(nullable = false)
	private byte[] avatar;
	
	@Transient
	private MultipartFile file;
	
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
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the avatar
	 */
	public byte[] getAvatar() {
		return avatar;
	}

	/**
	 * @param avatar the avatar to set
	 */
	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
	}

	/**
	 * @return the file
	 */
	public MultipartFile getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(MultipartFile file) {
		this.file = file;
	}	
}