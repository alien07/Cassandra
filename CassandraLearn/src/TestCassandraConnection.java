import java.util.List;

import Beans.Employee;
import Datasource.ConnectFactory;
import Utils.CassandraUtil;

import com.datastax.driver.core.ResultSet;

public class TestCassandraConnection {

	static final String CASSANDRA_IP = "192.168.1.40";
	static final String USE_KEYSPACE = "company";
	static final String USE_TABLE = "USE emp";

	public static void main(String[] args) throws Exception {
		ConnectFactory cassandra = new ConnectFactory(CASSANDRA_IP);
		cassandra.doConect(USE_KEYSPACE);
		// cassandra.executeQuery(USE_TABLE);

		String query = "select * from emp";

		ResultSet resultSet = cassandra.executeQuery(query);

		List<Employee> list = CassandraUtil.setDataToBean(resultSet,
				Employee.class);

		CassandraUtil.printData(resultSet.getColumnDefinitions(), list);
		System.exit(0);
	}
}
