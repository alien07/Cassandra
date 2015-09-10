package com.dao;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;

import javax.persistence.Column;
import javax.persistence.Table;

import Utils.ReadPropertiesFileUtil;

import com.anotation.Property;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ColumnDefinitions.Definition;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public abstract class CassandraDao<T> implements Connection, CommmonDAO<T> {

	private static final String DB_SCHEMA = "db.schema";
	private static final String DB_SERVER_IP = "db.server.ip";
	private Properties prop = ReadPropertiesFileUtil.readDbConfig();
	private Cluster cluster;
	private Session session;
	private Builder builder;

	private final Class<T> type;
	private HashMap<String, String> columns;

	public CassandraDao(Class<T> type) {
		this.type = type;
	}

	public Class<T> getType() {
		return this.type;
	}

	@Override
	public Connection connection() {
		return this;

	}

	@SuppressWarnings("unchecked")
	public CassandraDao() {
		this.type = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		builder = Cluster.builder();
		cluster = builder.addContactPoint(prop.getProperty(DB_SERVER_IP))
				.build();
		doConect(prop.getProperty(DB_SCHEMA));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dao.CommmonDAO#select(java.lang.String)
	 */
	@Override
	public List<T> select(String query) throws Exception {
		List<T> result = new ArrayList<T>();
		ResultSet resultSet = executeQuery(query);
		result = setDataToBean(resultSet);
		printData(resultSet.getColumnDefinitions(), result);
		return result;
	}

	@Override
	public void save(T t) throws Exception {
		Table table = t.getClass().getAnnotation(Table.class);
		String tableName = table.name();
		Field[] fields = t.getClass().getDeclaredFields();

		Map<String, Object> preparInsert = new HashMap<>();
		for (Field f : fields) {
			Property property = f.getAnnotation(Property.class);
			if (Objects.isNull(property)) {
				throw new Exception("ERROR " + f.getName()
						+ "does not have property anotation");
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

	// ================ public ==============

	public Session doConect() {
		session = cluster.connect();
		return session;
	}

	public Session doConect(String keyspaceName) {
		session = cluster.connect(keyspaceName);
		return session;
	}

	public ResultSet executeQuery(String query) {
		return session.execute(query);
	}

	// ================ private ==============

	/**
	 * 
	 * set
	 * 
	 * @param resultSet
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "null" })
	private List<T> setDataToBean(ResultSet resultSet) throws Exception {
		List<T> result = new ArrayList<T>();
		if (columns == null) {
			columns = getColumnMappingWithField();
		}
		for (Row row : resultSet) {
			T data = null;
			try {
				data = getType().newInstance();
				for (Definition column : resultSet.getColumnDefinitions()) {
					String columnName = column.getName();
					String fieldName = columns.containsKey(columnName) ? columns
							.get(columnName) : null;

					setDataToAttribute(data, fieldName,
							row.getObject(columnName));
				}
				result.add(data);
			} catch (Exception e) {
				throw e;
			}
		}

		return result;
	}

	/**
	 * 
	 * Mapping field between data base and bean.
	 * 
	 * Key = column name <br/>
	 * Value = field name
	 * 
	 * @param clazz
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SecurityException
	 */
	private HashMap<String, String> getColumnMappingWithField()
			throws SecurityException, InstantiationException,
			IllegalAccessException {
		HashMap<String, String> result = new HashMap<String, String>();
		Field[] fields = getType().newInstance().getClass().getDeclaredFields();

		for (Field f : fields) {
			Column column = f.getAnnotation(Column.class);
			String key = "";
			String value = f.getName();
			if (Objects.isNull(column)) {
				key = f.getName();
			} else {
				key = column.name();
			}
			result.put(key, value);
		}

		return result;
	}

	/**
	 * set value to field
	 * 
	 * @param clazz
	 * @param fieldName
	 * @param value
	 * @return
	 */
	private T setDataToAttribute(T clazz, String fieldName, Object value) {

		Field f;
		try {
			f = clazz.getClass().getDeclaredField(fieldName);
			f.setAccessible(true);// Very important, this allows the setting to
			// work.
			f.set(clazz, value);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return clazz;
	}

	private void printData(ColumnDefinitions columnDefinitions, List<T> listData)
			throws SecurityException, InstantiationException,
			IllegalAccessException {

		for (Definition column : columnDefinitions) {
			System.out.print(column.getName() + "|");
		}
		System.out.println("");
		if (columns == null) {
			columns = getColumnMappingWithField();
		}
		for (T data : listData) {
			for (String f : columns.values()) {

			}

			for (Definition column : columnDefinitions) {
				System.out.print(getValueFromClazz(data,
						columns.getOrDefault(column.getName(), ""))
						+ "|");
			}
			System.out.println("");
		}
	}

	private Object getValueFromClazz(T clazz, String columnName) {
		Object result = null;
		Field f;
		try {
			f = clazz.getClass().getDeclaredField(columnName);
			f.setAccessible(true);// Very important, this allows the setting to
			// work.
			result = f.get(clazz);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return result;
	}

}
