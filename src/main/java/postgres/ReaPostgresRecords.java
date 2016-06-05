package postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class ReaPostgresRecords {
	public static void main(String args[]) {
		Connection c = null;
		PreparedStatement stmt = null;
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Titan_DB", "postgres", "Jun@2016");
			c.setAutoCommit(false);
			// create our java prepared statement using a sql update query
			PreparedStatement ps = c.prepareStatement("INSERT INTO VERTEX(ID,BODY) VALUES(?,?)");
			// set the prepared statement parameters
			ps.setString(1, "21");
			ps.setString(2, "abc");
			ps.executeUpdate();
			ps.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Records created successfully");
	}
}
