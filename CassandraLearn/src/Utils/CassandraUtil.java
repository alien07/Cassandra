package Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ColumnDefinitions.Definition;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

public class CassandraUtil {

	public static <E> List<E> setDataToBean(ResultSet resultSet, Class<E> clazz)
			throws Exception {
		List<E> result = new ArrayList<E>();
		for (Row row : resultSet) {
			E emp;
			try {
				emp = clazz.newInstance();
				for (Definition column : resultSet.getColumnDefinitions()) {
					setDataToAttribute(emp, column.getName(),
							row.getObject(column.getName()));
				}
				result.add(emp);
			} catch (Exception e) {
				throw e;
			}
		}

		return result;
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
