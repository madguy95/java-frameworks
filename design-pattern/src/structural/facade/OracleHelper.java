package structural.facade;

public class OracleHelper {
	public static Connection getOracleDBConnection() {
		// get Oracle DB connection using connection parameters
		return null;
	}

	public void generateOraclePDFReport(String tableName, Connection con) {
		// get data from table and generate pdf report
		System.out.println("Get data from table Oracle and generate PDF report");
	}

	public void generateOracleHTMLReport(String tableName, Connection con) {
		// get data from table and generate pdf report
		System.out.println("Get data from table Oracle and generate HTML report");
	}

}
