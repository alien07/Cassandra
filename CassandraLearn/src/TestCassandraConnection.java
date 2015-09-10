import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import Beans.Employee;
import Datasource.EmployeDao;

public class TestCassandraConnection {

	static final String CASSANDRA_IP = "192.168.1.40";
	static final String USE_KEYSPACE = "company";
	static final String USE_TABLE = "USE emp";

	public static void main(String[] args) throws Exception {
		String query = "select * from emp";
		EmployeDao employeDao = new EmployeDao();

		employeDao.select(query);
		Class<Employee> sss = Employee.class;
		Employee emp = new Employee();

		// ConnectFactory cassandra = new ConnectFactory(CASSANDRA_IP);
		// cassandra.doConect(USE_KEYSPACE);
		// // cassandra.executeQuery(USE_TABLE);
		//

		//
		// ResultSet resultSet = cassandra.executeQuery(query);
		//
		// List<Employee> list = CassandraUtil.setDataToBean(resultSet,
		// Employee.class);
		//
		// CassandraUtil.printData(resultSet.getColumnDefinitions(), list);
		// testAno();
		System.exit(0);
	}

	private static void testAno() {

		for (Field f : Employee.class.getDeclaredFields()) {
			System.out.println(f.getName() + "|"
					+ printAnnotationList(f.getDeclaredAnnotations()));
		}

	}

	private static String printAnnotationList(Annotation[] declaredAnnotations) {
		String result = "";

		for (Annotation annotation : declaredAnnotations) {

			result += annotation.annotationType().getName() + "|"
					+ annotation.annotationType().getSimpleName() + "\n";
		}

		return result;
	}

}
