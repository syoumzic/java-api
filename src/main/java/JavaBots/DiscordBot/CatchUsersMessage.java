package JavaBots.DiscordBot;

import botLogic.Logic;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class CatchUsersMessage extends ListenerAdapter {

    private final Discord_Bot bot;
    private final Logic logic;
    CatchUsersMessage(Logic logic, Discord_Bot bot){
        this.logic = logic;
        this.bot = bot;
    }
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);
        Message message = event.getMessage();
        String text = message.getContentRaw();
        String id = message.getAuthor().getId();
        bot.sendMessage(Long.parseLong(id), logic.processMessage('D' + id, text));
    }
}
