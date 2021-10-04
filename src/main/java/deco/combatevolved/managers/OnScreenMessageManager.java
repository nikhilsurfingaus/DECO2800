package deco.combatevolved.managers;

import java.util.ArrayList;
import java.util.List;

import deco.combatevolved.observers.KeyTypedObserver;
import deco.combatevolved.handlers.KeyboardManager;

public class OnScreenMessageManager extends AbstractManager implements KeyTypedObserver {
    private List<String> messages = new ArrayList<String>();
    boolean isTyping = false;
    String unsentMessage = "";

    public OnScreenMessageManager() {
        GameManager.get().getManager(KeyboardManager.class).registerForKeyTyped(this);
    }

    public List<String> getMessages() {
        return messages;
    }

    public void addMessage(String message) {
        if (messages.size() > 20) {
            messages.remove(0);
        }
        messages.add(message);
    }

    public boolean isTyping() {
        return isTyping;
    }

    public String getUnsentMessage() {
        if (unsentMessage.equals("")) {
            return "Type your message";
        }
        return unsentMessage;
    }

    @Override
	public void notifyKeyTyped(char character) {
		if (character == 't' && !isTyping) {
			isTyping = true;
			GameManager.get().getCamera().setPotate(true);
			return;
		}
		if (isTyping) {
			if (character == ' ') {
				unsentMessage += ' ';
			}
			if (character == '`') {
				isTyping = false;
				GameManager.get().getCamera().setPotate(false);
			} else if (character == '\b') {
				if (unsentMessage.length() > 0) {
					unsentMessage = unsentMessage.substring(0, unsentMessage.length() - 1); // Backspace
				}
			} else if (character == '\r') {
				isTyping = false;
				NetworkManager networkManager = GameManager.get().getManager(NetworkManager.class);

				// We handle the host commands here so that they have privilege over clients.
				if (unsentMessage.startsWith("/")) {
					if (unsentMessage.startsWith("/")) {
						GameManager.get().getManager(CommandsManager.class).callCommand(unsentMessage);
					} else {
						this.addMessage("You do not have permission to use this command.");
					}
				} else {
					// Just a normal message, so send it.
					networkManager.sendChatMessage(unsentMessage, networkManager.getUsername());
				}
				GameManager.get().getCamera().setPotate(false);
				unsentMessage = "";
			} else {
				// Accept input
				if (character < '!' || character > 'z') {
					return;
				}
				unsentMessage += character;
			}
		}
	}
}
