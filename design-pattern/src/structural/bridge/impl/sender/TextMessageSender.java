package structural.bridge.impl.sender;

public class TextMessageSender implements MessageSender {
	@Override
	public void sendMessage() {
		System.out.println("TextMessageSender: Sending text message...");
	}
}
