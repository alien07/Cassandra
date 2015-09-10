package Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Datasource.ConnectFactory;

import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ColumnDefinitions.Definition;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

public class CassandraUtil {

	public static <E> List<E> setDataToBean(ResultSet resultSet, Class<E> clazz)
			throws Exception {
		List<E> result = new ArrayList<E>();
		for (Row row : resultSet) {
			E data;
			try {
				data = clazz.newInstance();
				for (Definition column : resultSet.getColumnDefinitions()) {
					setDataToAttribute(data, column.getName(),
							row.getObject(column.getName()));
				}
				result.add(data);
			} catch (Exception e) {
				throw e;
			}
		}

		return result;
	}

	public static ResultSet createKeyspaceWithSimpleStrategy(
			ConnectFactory cassandra, final String keyspaceName,
			final int numOfReplicas) {

		String query = "CREATE KEYSPACE " + keyspaceName + " WITH replication "
				+ "= {'class':'SimpleStrategy', 'replication_factor':"
				+ numOfReplicas + "}";
		// Executing the query
		return cassandra.executeQuery(query);
	}

	public static ResultSet createKeyspaceWithNetworkTopologyStrategy(
			ConnectFactory cassandra, final String keyspaceName,
			final boolean durable_writes, final HashMap<String, Integer> options) {
		String query = "CREATE KEYSPACE "
				+ keyspaceName
				+ " WITH replication "
				+ "= { 'class' : 'NetworkTopologyStrategy'[, '<data center>' : <integer>, '<data center>' : <integer>] . . . }";

		if (!durable_writes) {
			query += "  AND DURABLE_WRITES = false ";
		}

		// Executing the query
		return cassandra.executeQuery(query);
	}

	public static <E> void printData(ColumnDefinitions columnDefinitions,
			List<E> listData) {

		for (Definition column : columnDefinitions) {
			System.out.print(column.getName() + "|");
		}
		System.out.println("");

		for (E data : listData) {
			for (Definition column : columnDefinitions) {
				System.out.print(getValueFromClazz(data, column.getName())
						+ "|");
			}
			System.out.println("");
		}
	}

	// --------- [private section] ---------

	private static <E> E setDataToAttribute(E clazz, String columnName,
			Object value) {

		Field f;
		try {
			f = clazz.getClass().getDeclaredField(columnName);
			f.setAccessible(true);// Very important, this allows the setting to
			// work.
			f.set(clazz, value);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return clazz;
	}

	private static <E> Object getValueFromClazz(E clazz, String columnName) {
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
