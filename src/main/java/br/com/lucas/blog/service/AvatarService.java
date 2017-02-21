package br.com.lucas.blog.service;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.com.lucas.blog.entity.Avatar;
import br.com.lucas.blog.repository.AvatarRepository;

/**
 * @author Lucas.
 * 
 *         Classe que representa as regras de negócio de Avatar com seus respectivos métodos.
 * 
 */

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class AvatarService {
	
	private static final Logger LOG = Logger.getLogger(AvatarService.class); 
	//está informando ao servidor para que esta classe seja controlada pelo LOG4j conforme nivel informado no log4j.properties(pode ser INFO, DEBUG, etc...(consultar o site log4j.apache para maiores instruções)

	@Autowired
	private AvatarRepository avatarRepository;
	
	/**
	 * Método para encontrar um avatar por seu id.
	 * 
	 * * @param id o id do avatar a ser encontrado
	 * 
	 * @return um avatar.
	 */
	public Avatar findById(Long id) {
		
		return avatarRepository.findOne(id);
	}

	/**
	 * Método para salvar ou editar um objeto avatar.
	 * 
	 * * @param avatar o avatar a ser salvo ou editado no banco de dados.
	 *
	 */
	@Transactional(readOnly = false)
	public void saveOrUpdate(Avatar avatar) {
		
		avatarRepository.save(avatar);
	}

	/**
	 * Método para capturar um avatar através de seu upload.
	 * 
	 * * @param file o file do upload do avatar a ser carregado.
	 * 
	 * @return um Avatar(foto).
	 */
	public Avatar getAvatarByUpload(MultipartFile file) {
		
		Avatar avatar = new Avatar();
		if(file != null && file.getSize() > 0) {
			try {
				avatar.setTitulo(file.getOriginalFilename());
				avatar.setTipo(file.getContentType());			
				avatar.setAvatar(file.getBytes());
			} catch (IOException e) {
				LOG.error("Ocorreu um erro em AvatarService: " + e.getMessage()); //substituiu o print stack trace pelo Log error.
			}
		}
		return avatar;
	}	
}