package Datasource;


import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

public class ConnectFactory {

	private Cluster cluster;
	private Session session;

	public ConnectFactory(String ip) {
		cluster = Cluster.builder().addContactPoint(ip).build();
	}

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

}
