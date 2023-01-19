package structural.bridge.impl;

import structural.bridge.impl.message.Message;
import structural.bridge.impl.message.TextMessage;
import structural.bridge.impl.sender.EmailMessageSender;
import structural.bridge.impl.sender.MessageSender;
import structural.bridge.impl.sender.TextMessageSender;

/**
 * Decouples an abstraction so two classes can vary independently.
 * @author TinhNX
 *
 */
public class MessageTest {

	public static void main(String[] args) {
		MessageSender textMessageSender = new TextMessageSender();
		Message textMessage = new TextMessage(textMessageSender);
		textMessage.send();
		MessageSender emailMessageSender = new EmailMessageSender();
		Message emailMessage = new TextMessage(emailMessageSender);
		emailMessage.send();
	}
}
