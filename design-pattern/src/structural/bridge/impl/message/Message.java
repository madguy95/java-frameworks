package structural.bridge.impl.message;

import structural.bridge.impl.sender.MessageSender;

public abstract class Message {
	MessageSender messageSender;

	public Message(MessageSender messageSender) {
		this.messageSender = messageSender;
	}

	abstract public void send();
}
