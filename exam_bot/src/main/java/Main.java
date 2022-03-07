import model.bot.ConverterBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import service.ConvertService;
import service.UserService;

public class Main {
    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi api=new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(new ConverterBot());
        System.out.println("STARTED!");
        UserService.getUserFromBack();
        ConvertService.getConvertFromBack();
    }
}
