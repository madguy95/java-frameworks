package structural.facade;

public class MySqlHelper {
	public static Connection getMySqlDBConnection() {
		// get MySql DB connection using connection parameters
		return null;
	}

	public void generateMySqlPDFReport(String tableName, Connection con) {
		// get data from table and generate pdf report
		System.out.println("Get data from table mysql and generate pdf report");
	}

	public void generateMySqlHTMLReport(String tableName, Connection con) {
		// get data from table and generate pdf report
		System.out.println("Get data from table mysql and generate HTML report");
	}
}
