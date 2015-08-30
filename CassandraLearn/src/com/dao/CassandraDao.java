package com.dao;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import com.anotation.ModelType;
import com.anotation.Property;

public abstract class CassandraDao<T> implements Connection, CommmonDAO<T> {

	public Connection connection() {
		return null;

	}

	@Override
	public void save(T t) throws Exception {
		ModelType modelType = t.getClass().getAnnotation(ModelType.class);
		String table = modelType.value();
		Field[] fields = t.getClass().getDeclaredFields();

		Map<String, Object> preparInsert = new HashMap<>();
		for (Field f : fields) {
			Property property = f.getAnnotation(Property.class);
			if (Objects.isNull(property)) {
				throw new Exception("ERROR " + f.getName() + "does not have property anotation");
			}
			String columnName = property.value();
			Object value = f.get(t);
			preparInsert.put(columnName, value);
		}

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("INSERT INTO ");
		stringBuffer.append("table ( ");
		for (Entry<String, Object> entry : preparInsert.entrySet()) {
			stringBuffer.append(entry.getKey());
			stringBuffer.append(",");
		}
		stringBuffer.deleteCharAt(stringBuffer.length() - 1);

	}

}
