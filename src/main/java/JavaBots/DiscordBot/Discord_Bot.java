package JavaBots.DiscordBot;
import JavaBots.Bot;
import botLogic.Logic;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.User;

public class Discord_Bot implements Bot {
    private static JDA jda;

    public Discord_Bot(String token, Logic logic) {
        jda = JDABuilder.createDefault(token).build();
        jda.addEventListener(new CatchUsersMessage(logic, this));
    }

    /**
     * Отправка сообщения пользователю
     *
     * @param id   чата
     * @param text текст сообщения
     */
    @Override
    public void sendMessage(Long id, String text) {
        User user = jda.retrieveUserById(id).complete();
        try{
            user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(text).queue());
        } catch (Exception ignored){}
    }
}
