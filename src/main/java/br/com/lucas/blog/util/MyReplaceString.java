package br.com.lucas.blog.util;

import java.text.Normalizer;

/**
 * @author Lucas.
 * 
 *         Classe utilitária para auxiliar na montagem do atributo permalink da Entidade Categoria.
 * 
 */

public class MyReplaceString {

	/**
	 * Método estático para formatar o atributo permalink da Entidade Categoria.
	 * 
	 * * @param value o valor a ser formatado.
	 * 
	 * @return um link formatado.
	 */
	public static String formatarPermalink(String value) {
		
		//Titulo da Postagem = "Persistência com JPA!" será transformado utilizando permalink em -> ex."persistencia_com_jpa"
		
		String link = value.trim(); //método trim elimina espaços em branco do titulo
		
		link = link.toLowerCase(); //método lowerCase transforma todas as letras do titulo em minuscula
		
		link = Normalizer.normalize(link, Normalizer.Form.NFD); // método normalize remove os acentos da letra do titulo sem remover a letra
		 
		link = link.replaceAll("\\s", "_"); //substitui todos os espaços por underline
		
		link = link.replaceAll("\\W", ""); //substitui caracteres especiais(ex. !) por vazio
		
		link = link.replaceAll("\\_+", "_"); //se caso tiver 2 ou + underlines no titulo será substituido por apenas 1 underline
			
		return link;
	}
}