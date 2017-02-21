package br.com.lucas.blog.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lucas.blog.entity.Avatar;
import br.com.lucas.blog.entity.Usuario;
import br.com.lucas.blog.enumeradores.Perfil;
import br.com.lucas.blog.repository.UsuarioRepository;

/**
 * @author Lucas.
 * 
 *         Classe que representa as regras de neg�cio de Usu�rio com seus respectivos m�todos.
 * 
 */

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;	

	/**
	 * M�todo para editar o perfil do objeto usu�rio.
	 * 
	 * * @param usuario o usuario a ser editado no banco de dados.
	 *
	 */
	@Transactional(readOnly = false)
	public void updatePerfil(Usuario usuario) {

		Usuario persistente = usuarioRepository.findOne(usuario.getId()); //encontra o id e atribui a variavel persistente
		persistente.setPerfil(usuario.getPerfil()); //seta o perfil capturado pelo usuario e atribui a variavel persistente criada acima
		
		usuarioRepository.save(persistente); //salva o objeto persistente	
	}
	

	/**
	 * M�todo para encontrar por pagina��o/ordena��o(Asc/Desc) a lista da entidade Usu�rio.
	 * 
	 * * @param page a page a ser buscada .
	 * * @param size o size a ser representado na tela por p�gina.
	 * * @param field o field a ser representado pelo campo na tela por p�gina.
	 * * @param order o order a ser representado pela ordena��o informada na tela por p�gina.
	 * 
	 * @return uma ou (+) p�gina(s) de usu�rio(s).
	 */
	public Page<Usuario> findByPaginationOrderByField(int page, int size, String field, String order) {
		
		Sort sort = new Sort(new Order(Direction.fromString(order), field));
		
		return usuarioRepository.findAll(new PageRequest(page, size, sort));
	}
	
	/**
	 * M�todo para encontrar por pagina��o a lista da entidade Usu�rio.
	 * 
	 * * @param page a page a ser buscada .
	 * * @param size o size a ser representado na tela por p�gina.
	 * 
	 * @return uma ou (+) p�gina(s) de usu�rio(s).
	 */
	public Page<Usuario> findByPagination(int page, int size) {
		
		Pageable pageable = new PageRequest(page, size);
		
		return usuarioRepository.findAllByOrderByNomeAsc(pageable);
	}
	
	/**
	 * M�todo para editar o atributo senha do objeto usu�rio.
	 * 
	 * * @param usuario o usuario a ser editado no banco de dados.
	 *
	 */
	@Transactional(readOnly = false) //Ser� utilizado transa��o pois trata-se de uma atualiza��o
	public void updateSenha(Usuario usuario) {
		
		String hash = new BCryptPasswordEncoder().encode(usuario.getSenha()); // criptografa a senha ao salvar
		
		Usuario uPersistente = usuarioRepository.findOne(usuario.getId()); //criando para Auditoria
		
		uPersistente.setSenha(hash); // setar no usuario a senha criptografada (hash configurado acima)
		
		//atualizar senha referente ao id capturado
		//usuarioRepository.updateSenha(usuario.getSenha(), usuario.getId());
		
		usuarioRepository.save(uPersistente); 
	}
	
	/**
	 * M�todo para editar os atributos nome e email do objeto usu�rio.
	 * 
	 * * @param usuario o usuario a ser editado no banco de dados.
	 *
	 */
	@Transactional(readOnly = false) //Ser� utilizado transa��o pois trata-se de uma atualiza��o
	public void updateNomeAndEmail(Usuario usuario) {
		
		Usuario uPersistente = usuarioRepository.findOne(usuario.getId()); //criando para Auditoria
		uPersistente.setNome(usuario.getNome());
		uPersistente.setEmail(usuario.getEmail());
		
		//atualizar nome e email referentes ao id capturado
		//usuarioRepository.updateNomeAndEmail(usuario.getNome(), usuario.getEmail(), usuario.getId());
	
		usuarioRepository.save(uPersistente); 
	}	
	
	/**
	 * M�todo para salvar um objeto usu�rio.
	 * 
	 * * @param usuario o usuario a ser salvo no banco de dados.
	 *
	 */
	@Transactional(readOnly = false) //Ser� utilizado transa��o pois trata-se de uma inser��o
	public void save(Usuario usuario) {
		
		if(usuario.getDataCadastro() == null) { //verifica se usuario n�o tem data de cadastro
			usuario.setDataCadastro(LocalDate.now()); // se n�o existir data de cadastro significa que ainda n�o foi criado, setar a data de Cadastro para now para iniciar o save
		}
		
		String hash = new BCryptPasswordEncoder().encode(usuario.getSenha()); // criptografa a senha ao salvar
		
		usuario.setPerfil(Perfil.LEITOR); //quando finalizar o cadastro automaticamente seta o mesmo como Leitor
		
		usuario.setSenha(hash); // setar no usuario a senha criptografada (hash configurado acima)
		
		usuarioRepository.save(usuario); // e salva no banco
	}
	
	/**
	 * M�todo para deletar um objeto usu�rio.
	 * 
	 * * @param id o id a ser buscado para realizar a exclus�o no banco de dados.
	 *
	 */
	//m�todo para deletar um usu�rio por id
	@Transactional(readOnly = false) //Ser� utilizado transa��o pois trata-se de uma exclus�o
	public void delete(Long id) {
		
		usuarioRepository.delete(id);
	}	
	
	/**
	 * M�todo para encontrar um usu�rio atrav�s de seu email.
	 * 
	 * * @param email o email do usu�rio a ser encontrado.
	 * 
	 * @return um usu�rio.
	 */
	public Usuario findByEmail(String email) {
		
		return usuarioRepository.findByEmail(email);
	}
	
	/**
	 * M�todo para encontrar um usu�rio atrav�s de seu avatar.
	 * 
	 * * @param avatar o avatar do usu�rio a ser encontrado.
	 * 
	 * @return um usu�rio.
	 */
	public Usuario findByAvatar(Avatar avatar) {
		
		return usuarioRepository.findByAvatar(avatar);
	}
	
	/**
	 * M�todo para encontrar um usu�rio por seu id.
	 * 
	 * * @param id o id do usu�rio a ser encontrado.
	 * 
	 * @return um usu�rio.
	 */
	public Usuario findById(Long id) {
		
		return usuarioRepository.findOne(id);
	}	

	/**
	 * M�todo para encontrar toda lista de usu�rio(s).
	 * 
	 * @return uma lista de usu�rios.
	 */
	public List<Usuario> findAll() {		
		
		return usuarioRepository.findAll();
	}
}