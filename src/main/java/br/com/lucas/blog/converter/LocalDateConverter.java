package br.com.lucas.blog.converter;

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true) // ir� aplicar a convers�o do LocalDate
public class LocalDateConverter implements AttributeConverter<LocalDate, Date>{ // Converter LocalDate(inserido em dataCadastro) para Date. 
	//Se n�o realizar esta convers�o o campo dataCadastro no bd estar� como BLOB.
	
	public Date convertToDatabaseColumn(LocalDate localDate) {

		return Date.valueOf(localDate); // ir� atribuir ao Date o valor do LocalDate.
	}

	public LocalDate convertToEntityAttribute(Date date) {

		return date.toLocalDate(); // ir� atribuir ao LocalDate o valor do Date (� necess�rio realizar a convers�o normal e a convers�o inversa para que possa atribuir corretamente ao bd.
	}	
}