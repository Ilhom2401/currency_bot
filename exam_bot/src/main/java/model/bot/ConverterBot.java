package model.bot;

import lombok.SneakyThrows;
import model.StateDTO;
import model.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import service.*;
import util.BaseData;
import util.enums.BotState;
import util.enums.Constant;
import util.enums.Role;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static util.enums.BotState.*;

public class ConverterBot extends TelegramLongPollingBot implements Constant {

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        StateDTO res = UserService.getAndCheck(update);
        BotState state = res.getState();
        Role role = res.getRole();
        boolean isAdmin = res.isAdmin();

        if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().equals("/start"))
            isAdmin = false;
        if (isAdmin) {
            new AdminPanel().onUpdateReceived(update);
            return;
        }
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                String text = message.getText();
                if (text.equals("admin123")) {
//                    role = Role.ADMIN;
                    state = ADMIN_MENU_SEND;
                }
                if (text.equals("/start")) {
                    state = role.equals(Role.ADMIN) ? ADMIN_MENU_SEND : BotState.REGISTER;
                    execute(TelegramBotService.removeKeyBoardMarkup(update));
                } else if (state.equals(BotState.CONVERT_SUM)) {
                    ConvertService.setConvertSum(update);
                    state = BotState.CONVERT_INFO;
                }
            } else if (message.hasContact() && state.equals(BotState.REGISTER)) state = BotState.CHOOSE_CURRENCY;
            else {
                execute(TelegramBotService.setError(update));
                return;
            }

        } else if (update.hasCallbackQuery()) {
            if (state.equals(BotState.INFO_CURR)) {
                state = BotState.COURSE_INFORMATION;
            } else if (state.equals(BotState.CONVERT_CURR)||state.equals(BotState.CONVERT_FROM2)||state.equals(BotState.CONVERT_FROM)) {
                ConvertService.setConvert(update, BotState.CONVERT_FROM);
                state = BotState.CONVERT_TO;
            } else if (state.equals(BotState.CONVERT_TO)||state.equals(BotState.CONVERT_TO2)||state.equals(CONVERT_TO_EDIT)||state.equals(CONVERT_TO_EDIT2)) {
                ConvertService.setConvert(update, BotState.CONVERT_TO);
                state = BotState.CONVERT_SUM;
            } else if (state.equals(ADMIN_MENU_SEND)) {
                state = BotState.GET_EXEL_FILE;
            }

            switch (update.getCallbackQuery().getData()) {
                case MAIN_MENU -> state = BotState.CHOOSE_CURRENCY;
                case INFO_CURR -> state = BotState.INFO_CURR;
                case CONVERT_CURR -> state = BotState.CONVERT_CURR;
                case NEXT -> {
                    if (state.equals(BotState.CONVERT_TO)) {
                        state = CONVERT_FROM2;
                    }else if (state.equals(BotState.CONVERT_SUM)){
                        state = CONVERT_TO_EDIT2;
                    }else
                    state = INFO_CURR2;
                }
                case PREV -> {
                    if (state.equals(BotState.CONVERT_TO2)) {
                        state = BotState.CONVERT_TO;
                    }else if (state.equals(BotState.CONVERT_TO)){
                        state = BotState.CONVERT_FROM;
                    }else if (state.equals(BotState.CONVERT_SUM)){
                        state = CONVERT_TO_EDIT;
                    }
                    else
                    state = INFO_CURR_PREV;
                }
                case SEND_ADVERTISING -> state = BotState.SEND_ADVERTISING;
                default -> {
                    if (List.of(BotState.INFO_CURR, INFO_CURR_PREV, INFO_CURR2).contains(state)) {
                        state = COURSE_INFORMATION;
                    }
                }
            }
        }
        switch (state) {
            case REGISTER -> execute(TelegramBotService.getRegister(update));
            case CHOOSE_CURRENCY -> execute(TelegramBotService.chooseCurrency(update));
            case INFO_CURR -> execute(TelegramBotService.currencyInfo(update));
            case INFO_CURR_PREV -> execute(TelegramBotService.currencyInfoEdit(update));
            case INFO_CURR2 -> execute(TelegramBotService.currencyInfoEdit2(update));
            case COURSE_INFORMATION -> execute(TelegramBotService.currencyInfoText(update));
            case CONVERT_CURR -> execute(TelegramBotService.convertFrom(update));
            case CONVERT_FROM2 -> execute(TelegramBotService.convertFrom2Edit(update));
            case CONVERT_FROM -> execute(TelegramBotService.convertFromEdit(update));
            case CONVERT_TO -> execute(TelegramBotService.convertTo(update));
            case CONVERT_TO_EDIT2 -> execute(TelegramBotService.convertTo2Edit(update));
            case CONVERT_TO_EDIT -> execute(TelegramBotService.convertToEdit(update));
            case CONVERT_SUM -> execute(TelegramBotService.convertSum(update));
            case CONVERT_INFO -> execute(TelegramBotService.convertInfo(update));
            case ADMIN_MENU_SEND -> execute(AdminBotService.showUsers(update));
            case GET_EXEL_FILE -> execute(AdminBotService.getExelFile(update));
            case SEND_ADVERTISING -> {
                ExecutorService executorService = Executors.newFixedThreadPool(2);
                for (User user : UserService.userList) {
                    executorService.execute(() -> {
                        try {
                            execute(AdminBotService.SendAdd(user.getChatId()));
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    });
                }
                state = CHOOSE_CURRENCY;
            }
            default -> execute(TelegramBotService.setError(update));
        }
        UserService.saveStateAndLan(update, new StateDTO(state, role, isAdmin));
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
