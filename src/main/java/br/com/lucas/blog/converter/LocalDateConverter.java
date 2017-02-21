package br.com.lucas.blog.converter;

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true) // irá aplicar a conversão do LocalDate
public class LocalDateConverter implements AttributeConverter<LocalDate, Date>{ // Converter LocalDate(inserido em dataCadastro) para Date. 
	//Se não realizar esta conversão o campo dataCadastro no bd estará como BLOB.
	
	public Date convertToDatabaseColumn(LocalDate localDate) {

		return Date.valueOf(localDate); // irá atribuir ao Date o valor do LocalDate.
	}

	public LocalDate convertToEntityAttribute(Date date) {

		return date.toLocalDate(); // irá atribuir ao LocalDate o valor do Date (é necessário realizar a conversão normal e a conversão inversa para que possa atribuir corretamente ao bd.
	}	
}