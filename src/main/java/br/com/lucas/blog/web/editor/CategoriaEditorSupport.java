package br.com.lucas.blog.web.editor;

import java.util.Collection;

import org.springframework.beans.propertyeditors.CustomCollectionEditor;

import br.com.lucas.blog.entity.Categoria;
import br.com.lucas.blog.service.CategoriaService;

/**
 * @author Lucas.
 * 
 *         Classe que representa o editor de Categoria extendendo de CustomCollectionEditor no qual irá converter o objeto categoria em uma list categoria.
 * 
 */

public class CategoriaEditorSupport extends CustomCollectionEditor {
	
	private CategoriaService categoriaService;

	public CategoriaEditorSupport(Class<? extends Collection> collectionType, CategoriaService categoriaService) {
		super(collectionType);
		
		this.categoriaService = categoriaService;
	}

	@Override
	protected Object convertElement(Object element) {

		Long id = Long.valueOf((String) element);
		
		Categoria categoria = categoriaService.findById(id);
		
		return super.convertElement(categoria);
	}	
}