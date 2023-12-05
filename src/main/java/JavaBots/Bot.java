package JavaBots;

/**
 * Интерфейс для управления ботом
 */
public interface Bot{

    /**
     * Отправка сообщения пользователю
     * @param id чата
     * @param text текст сообщения
     */
    void sendMessage(Long id, String text);
}