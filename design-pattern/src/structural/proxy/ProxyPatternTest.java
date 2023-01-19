package structural.proxy;

/**
 * Proxy design pattern common uses are to control access or to provide a
 * wrapper implementation for better performance.
 * <p>
 * Java RMI package uses proxy pattern. That’s all for proxy design pattern in
 * java.
 * 
 * @author TinhNX
 *
 */
public class ProxyPatternTest {

	public static void main(String[] args) {
		CommandExecutor executor = new CommandExecutorProxy("Pankaj", "wrong_pwd");
		try {
			executor.runCommand("ls -ltr");
			executor.runCommand(" rm -rf abc.pdf");
		} catch (Exception e) {
			System.out.println("Exception Message::" + e.getMessage());
		}

	}
}
