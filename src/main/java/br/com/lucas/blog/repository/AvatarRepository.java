package br.com.lucas.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.lucas.blog.entity.Avatar;

/**
 * @author Lucas.
 * 
 *         Classe que representa uma interface de reposit�rio Avatar com os m�todos para acesso ao banco de Dados extendendo de JPARepository.
 * 
 */

public interface AvatarRepository extends JpaRepository<Avatar, Long>{

}