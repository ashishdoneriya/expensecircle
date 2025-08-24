package com.csetutorials.expensecircle.jsonconverters;

import com.csetutorials.expensecircle.utilities.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.type.CollectionType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Set;

@Converter(autoApply = true)
public class SetLongToJsonConverter implements AttributeConverter<Set<Long>, String> {

	private static final CollectionType type = Constants.objectMapper
		.getTypeFactory()
		.constructCollectionType(Set.class, Long.class);

	@Override
	public String convertToDatabaseColumn(Set<Long> stringList) {
		if (stringList == null) {
			return null;
		}
		try {
			return Constants.objectMapper.writeValueAsString(stringList);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("Error converting list to JSON", e);
		}
	}

	@Override
	public Set<Long> convertToEntityAttribute(String dbData) {
		if (dbData == null) {
			return null;
		}
		try {
			return Constants.objectMapper.readValue(dbData, type);
		} catch (Exception e) {
			throw new IllegalArgumentException("Error converting JSON to list", e);
		}
	}
}
