package br.com.lucas.blog.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;

import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.web.multipart.MultipartFile;

import br.com.lucas.blog.enumeradores.Perfil;

/**
 * @author Lucas.
 * 
 *         Classe que representa uma Entidade Usuario.
 * 
 */

@Entity
@Table(name = "usuarios")
public class Usuario extends AbstractAuditoria<Long> {
	
	@Column(nullable = false, unique = true)
	private String nome;	
	
	@Column(nullable = false, unique = true)
	private String email;
		
	@Column(name = "senha_hash", nullable = false)
	private String senha;
	
	@Column(name = "data_cadastro", nullable = false)
	private LocalDate dataCadastro;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Perfil perfil;
	
	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinColumn(name = "avatar_id")
	private Avatar avatar;
	
	@OneToMany(mappedBy = "usuario")
	private List<Comentario> comentarios;
	
	@Transient
	private MultipartFile file;
	
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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the senha
	 */
	public String getSenha() {
		return senha;
	}

	/**
	 * @param senha the senha to set
	 */
	public void setSenha(String senha) {
		this.senha = senha;
	}

	/**
	 * @return the dataCadastro
	 */
	public LocalDate getDataCadastro() {
		return dataCadastro;
	}

	/**
	 * @param dataCadastro the dataCadastro to set
	 */
	public void setDataCadastro(LocalDate dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	/**
	 * @return the perfil
	 */
	public Perfil getPerfil() {
		return perfil;
	}

	/**
	 * @param perfil the perfil to set
	 */
	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	/**
	 * @return the avatar
	 */
	public Avatar getAvatar() {
		return avatar;
	}

	/**
	 * @param avatar the avatar to set
	 */
	public void setAvatar(Avatar avatar) {
		this.avatar = avatar;
	}

	/**
	 * @return the comentarios
	 */
	public List<Comentario> getComentarios() {
		return comentarios;
	}

	/**
	 * @param comentarios the comentarios to set
	 */
	public void setComentarios(List<Comentario> comentarios) {
		this.comentarios = comentarios;
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