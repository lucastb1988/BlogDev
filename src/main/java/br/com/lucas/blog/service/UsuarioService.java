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
 *         Classe que representa as regras de negócio de Usuário com seus respectivos métodos.
 * 
 */

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;	

	/**
	 * Método para editar o perfil do objeto usuário.
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
	 * Método para encontrar por paginação/ordenação(Asc/Desc) a lista da entidade Usuário.
	 * 
	 * * @param page a page a ser buscada .
	 * * @param size o size a ser representado na tela por página.
	 * * @param field o field a ser representado pelo campo na tela por página.
	 * * @param order o order a ser representado pela ordenação informada na tela por página.
	 * 
	 * @return uma ou (+) página(s) de usuário(s).
	 */
	public Page<Usuario> findByPaginationOrderByField(int page, int size, String field, String order) {
		
		Sort sort = new Sort(new Order(Direction.fromString(order), field));
		
		return usuarioRepository.findAll(new PageRequest(page, size, sort));
	}
	
	/**
	 * Método para encontrar por paginação a lista da entidade Usuário.
	 * 
	 * * @param page a page a ser buscada .
	 * * @param size o size a ser representado na tela por página.
	 * 
	 * @return uma ou (+) página(s) de usuário(s).
	 */
	public Page<Usuario> findByPagination(int page, int size) {
		
		Pageable pageable = new PageRequest(page, size);
		
		return usuarioRepository.findAllByOrderByNomeAsc(pageable);
	}
	
	/**
	 * Método para editar o atributo senha do objeto usuário.
	 * 
	 * * @param usuario o usuario a ser editado no banco de dados.
	 *
	 */
	@Transactional(readOnly = false) //Será utilizado transação pois trata-se de uma atualização
	public void updateSenha(Usuario usuario) {
		
		String hash = new BCryptPasswordEncoder().encode(usuario.getSenha()); // criptografa a senha ao salvar
		
		Usuario uPersistente = usuarioRepository.findOne(usuario.getId()); //criando para Auditoria
		
		uPersistente.setSenha(hash); // setar no usuario a senha criptografada (hash configurado acima)
		
		//atualizar senha referente ao id capturado
		//usuarioRepository.updateSenha(usuario.getSenha(), usuario.getId());
		
		usuarioRepository.save(uPersistente); 
	}
	
	/**
	 * Método para editar os atributos nome e email do objeto usuário.
	 * 
	 * * @param usuario o usuario a ser editado no banco de dados.
	 *
	 */
	@Transactional(readOnly = false) //Será utilizado transação pois trata-se de uma atualização
	public void updateNomeAndEmail(Usuario usuario) {
		
		Usuario uPersistente = usuarioRepository.findOne(usuario.getId()); //criando para Auditoria
		uPersistente.setNome(usuario.getNome());
		uPersistente.setEmail(usuario.getEmail());
		
		//atualizar nome e email referentes ao id capturado
		//usuarioRepository.updateNomeAndEmail(usuario.getNome(), usuario.getEmail(), usuario.getId());
	
		usuarioRepository.save(uPersistente); 
	}	
	
	/**
	 * Método para salvar um objeto usuário.
	 * 
	 * * @param usuario o usuario a ser salvo no banco de dados.
	 *
	 */
	@Transactional(readOnly = false) //Será utilizado transação pois trata-se de uma inserção
	public void save(Usuario usuario) {
		
		if(usuario.getDataCadastro() == null) { //verifica se usuario não tem data de cadastro
			usuario.setDataCadastro(LocalDate.now()); // se não existir data de cadastro significa que ainda não foi criado, setar a data de Cadastro para now para iniciar o save
		}
		
		String hash = new BCryptPasswordEncoder().encode(usuario.getSenha()); // criptografa a senha ao salvar
		
		usuario.setPerfil(Perfil.LEITOR); //quando finalizar o cadastro automaticamente seta o mesmo como Leitor
		
		usuario.setSenha(hash); // setar no usuario a senha criptografada (hash configurado acima)
		
		usuarioRepository.save(usuario); // e salva no banco
	}
	
	/**
	 * Método para deletar um objeto usuário.
	 * 
	 * * @param id o id a ser buscado para realizar a exclusão no banco de dados.
	 *
	 */
	//método para deletar um usuário por id
	@Transactional(readOnly = false) //Será utilizado transação pois trata-se de uma exclusão
	public void delete(Long id) {
		
		usuarioRepository.delete(id);
	}	
	
	/**
	 * Método para encontrar um usuário através de seu email.
	 * 
	 * * @param email o email do usuário a ser encontrado.
	 * 
	 * @return um usuário.
	 */
	public Usuario findByEmail(String email) {
		
		return usuarioRepository.findByEmail(email);
	}
	
	/**
	 * Método para encontrar um usuário através de seu avatar.
	 * 
	 * * @param avatar o avatar do usuário a ser encontrado.
	 * 
	 * @return um usuário.
	 */
	public Usuario findByAvatar(Avatar avatar) {
		
		return usuarioRepository.findByAvatar(avatar);
	}
	
	/**
	 * Método para encontrar um usuário por seu id.
	 * 
	 * * @param id o id do usuário a ser encontrado.
	 * 
	 * @return um usuário.
	 */
	public Usuario findById(Long id) {
		
		return usuarioRepository.findOne(id);
	}	

	/**
	 * Método para encontrar toda lista de usuário(s).
	 * 
	 * @return uma lista de usuários.
	 */
	public List<Usuario> findAll() {		
		
		return usuarioRepository.findAll();
	}
}