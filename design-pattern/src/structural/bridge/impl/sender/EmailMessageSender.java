package structural.bridge.impl.sender;

public class EmailMessageSender implements MessageSender {
	@Override
	public void sendMessage() {
		System.out.println("EmailMessageSender: Sending email message...");
		System.out.println("EmailMessageSender: waiting for modify...");
	}
}
