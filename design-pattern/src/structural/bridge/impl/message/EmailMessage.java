package structural.bridge.impl.message;

import structural.bridge.impl.sender.MessageSender;

public class EmailMessage extends Message {
	public EmailMessage(MessageSender messageSender) {
		super(messageSender);
	}

	@Override
	public void send() {
		messageSender.sendMessage();
	}
}