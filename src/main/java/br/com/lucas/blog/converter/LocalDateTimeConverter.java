package br.com.lucas.blog.converter;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true) // ir� aplicar a convers�o do LocalDateTime
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> { // Converter LocalDateTime para DateTimeStamp. 
	//Se n�o realizar esta convers�o o campo dataCadastro no bd estar� como BLOB.
	
	public Timestamp convertToDatabaseColumn(LocalDateTime localDateTime) {
		
		return Timestamp.valueOf(localDateTime); // ir� atribuir ao TimeStamp(data e hora) o valor do LocalDateTime.
	}

	public LocalDateTime convertToEntityAttribute(Timestamp timestamp) {

		return timestamp != null ? timestamp.toLocalDateTime() : null; // ir� atribuir ao LocalDateTime o valor do TimeStamp(data e hora) (� necess�rio realizar a convers�o normal e a convers�o inversa para que possa atribuir corretamente ao bd.
	}	
}