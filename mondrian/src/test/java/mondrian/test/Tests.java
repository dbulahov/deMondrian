package mondrian.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.output.OutputFrame;

public class Tests {
    static Consumer<OutputFrame> x = new Consumer<OutputFrame>() {

	@Override
	public void accept(OutputFrame t) {
	    System.out.println(t.getUtf8String());
	    ;
	}
    };

    @Test
    public void main() throws Exception {
	try (MySQLContainer<?> c = (MySQLContainer<?>) new MySQLContainer("mysql:8").withDatabaseName("foodmart")
		.withUsername("myusername").withPassword("mypassword").withLogConsumer(x)) {
	    c.start();

	    String url = c.getJdbcUrl();

//	    Class.forName("com.mysql.jdbc.Driver");
	    Connection con = DriverManager.getConnection(url, "myusername", "mypassword");
	    Statement stmt = con.createStatement();
	    ResultSet rs = stmt.executeQuery("SELECT version() ");
	    rs.next();
	    String resultSetString = rs.getString(1);

	    assertTrue(resultSetString.startsWith("8"),
		    "The database version can be set using a container rule parameter");
	} finally {
	}
    }
}
