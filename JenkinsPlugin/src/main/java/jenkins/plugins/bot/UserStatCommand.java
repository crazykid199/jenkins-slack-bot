package jenkins.plugins.bot;

import hudson.Extension;
import hudson.model.Hudson;
import hudson.model.User;

import java.util.Collection;
import java.util.Collections;

@Extension
public class UserStatCommand extends AbstractTextSendingCommand {

    private static final String SYNTAX = " <username>";
    private static final String HELP = SYNTAX + " - prints information about a Jenkins user";

    @Override
    public Collection<String> getCommandNames() {
        return Collections.singleton("userstat");
    }

    @Override
	protected String getReply(Bot bot, JBotSender sender, String[] args) {
		if (args.length < 2) {
		    return giveSyntax(sender.getNickname(), args[0]);
		}
		String userName = args[1];
		User user = User.get(userName, false);
		if (user != null) {
		    
		    String checkPermission = checkPermission(user, sender);
		    if (checkPermission != null) {
		        return checkPermission;
		    }
		    
			StringBuilder buf = new StringBuilder();
			buf.append(userName).append(":");
			
			if (!userName.equals(user.getFullName())) {
				buf.append("\n").append("Full name: ").append(user.getFullName());
			}
			
			if (user.getDescription() != null) {
				buf.append("\n").append("Description: ").append(user.getDescription());
			}
			
//			Mailer.UserProperty mailProperty = user.getProperty(Mailer.UserProperty.class);
//			if (mailProperty != null) {
//				buf.append("\n").append("E-mail: ").append(mailProperty.getAddress());
//			}
			
			return buf.toString();
		} else {
			return sender.getNickname() + ": don't know a user named " + userName;
		}
	}

	private String checkPermission(User user, JBotSender sender) {
        if (!user.hasPermission(Hudson.READ)) {
            return sender.getNickname() + ": you may not read that user!"; 
        }
        return null;
    }

    @Override
	public String getHelp() {
		return HELP;
	}

	private String giveSyntax(String sender, String cmd) {
		return sender + ": syntax is: '" + cmd +  SYNTAX + "'";
	}

}
