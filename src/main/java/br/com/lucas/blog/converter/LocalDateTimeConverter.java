package br.com.lucas.blog.converter;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true) // irá aplicar a conversão do LocalDateTime
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> { // Converter LocalDateTime para DateTimeStamp. 
	//Se não realizar esta conversão o campo dataCadastro no bd estará como BLOB.
	
	public Timestamp convertToDatabaseColumn(LocalDateTime localDateTime) {
		
		return Timestamp.valueOf(localDateTime); // irá atribuir ao TimeStamp(data e hora) o valor do LocalDateTime.
	}

	public LocalDateTime convertToEntityAttribute(Timestamp timestamp) {

		return timestamp != null ? timestamp.toLocalDateTime() : null; // irá atribuir ao LocalDateTime o valor do TimeStamp(data e hora) (é necessário realizar a conversão normal e a conversão inversa para que possa atribuir corretamente ao bd.
	}	
}