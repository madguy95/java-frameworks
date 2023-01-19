package structural.bridge.impl.message;

import structural.bridge.impl.sender.MessageSender;

public class TextMessage extends Message {
	public TextMessage(MessageSender messageSender) {
		super(messageSender);
	}

	@Override
	public void send() {
		messageSender.sendMessage();
	}
}