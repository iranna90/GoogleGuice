package titangraph.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.SystemUtils;

import com.hazelcast.core.MapLoader;
import com.hazelcast.core.MapStore;

import titangraph.graphtest.Vertex;
import titangraph.utility.JsonParser;

public class VertexStorage implements MapStore<String, Vertex>, MapLoader<String, Vertex> {

	Connection c = null;

	public VertexStorage() {
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Titan_DB", "postgres", "Jun@2016");
			c.setAutoCommit(false);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	@Override
	public Vertex load(String id) {
		/*// get the object from data base
		Vertex returnValue = null;
		try {
			PreparedStatement searchStatement = c.prepareStatement("Select * from VERTEX where id = ?");
			searchStatement.setString(1, id);
			ResultSet res = searchStatement.executeQuery();
			while (res.next()) {
				returnValue = (Vertex) JsonParser.jsonToObject(res.getString("BODY"), Vertex.class);
			}
			res.close();
			searchStatement.close();
			c.commit();
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return returnValue;*/
		return null;
	}

	@Override
	public Map<String, Vertex> loadAll(Collection<String> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<String> loadAllKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll(Collection<String> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void store(String id, Vertex vertex) {
		try {
			// check already exists
			PreparedStatement searchStatement = c.prepareStatement("Select * from VERTEX where id = ?");
			searchStatement.setString(1, id);
			ResultSet res = searchStatement.executeQuery();
			int count = 0;
			while (res.next())
				count++;
			searchStatement.close();
			if (count >= 1) {
				// update
				PreparedStatement updateStatement = c.prepareStatement("UPDATE VERTEX set BODY = ? where ID=?");
				updateStatement.setString(1, JsonParser.objectToJsonString(vertex));
				updateStatement.setString(2, id);
				updateStatement.executeUpdate();
				updateStatement.close();
			} else {
				// create our java prepared statement using a sql update query
				PreparedStatement insertStatement = c.prepareStatement("INSERT INTO VERTEX(ID,BODY) VALUES(?,?)");
				// set the prepared statement parameters
				insertStatement.setString(1, id);
				insertStatement.setString(2, JsonParser.objectToJsonString(vertex));
				insertStatement.executeUpdate();
				insertStatement.close();
			}
			c.commit();
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	@Override
	public void storeAll(Map<String, Vertex> arg0) {
		System.out.println("Store all");
	}

}
