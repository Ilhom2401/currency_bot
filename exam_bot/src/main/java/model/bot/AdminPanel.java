package model.bot;

import lombok.SneakyThrows;
import model.StateDTO;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import service.AdminBotService;
import service.TelegramBotService;
import service.UserService;
import util.BaseData;
import util.enums.BotState;
import util.enums.Constant;
import util.enums.Role;

import static util.enums.BotState.*;

public class AdminPanel extends TelegramLongPollingBot implements Constant {

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        StateDTO res = UserService.getAndCheck(update);
        BotState state = res.getState();
        Role role = res.getRole();
        boolean isAdmin = res.isAdmin();
        if (update.hasMessage()){
            Message message = update.getMessage();
            if(message.hasText()){
                String text = message.getText();
                if(text.equals("/start")){
                    state = BotState.ADMIN_MAIN_MENU_SEND;
                }
            }

        }else if (update.hasCallbackQuery()){
            switch (update.getCallbackQuery().getData()){

            }
        }
        switch (state){
            case ADMIN_MAIN_MENU_SEND-> execute(AdminBotService.showUsers(update));

            default -> execute(TelegramBotService.setError(update));
        }
        UserService.saveStateAndLan(update, new StateDTO(state, role,isAdmin));

    }



    @Override
    public String getBotUsername() {
        return BaseData.USERNAME;
    }

    @Override
    public String getBotToken() {
        return BaseData.TOKEN;
    }
}
