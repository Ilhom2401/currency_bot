package service;

import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import util.enums.Constant;

import java.util.List;

public class AdminBotService implements Constant {
    public static SendDocument getExelFile(Update update){
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        InputFile activeUsers = UserService.getActiveUsers();
        SendDocument sendDocument = new SendDocument(message.getChatId().toString(), activeUsers);
        sendDocument.setCaption("Users");
        InlineKeyboardButton button = new InlineKeyboardButton("⬅️ Back");
        button.setCallbackData(ADMIN_MAIN_MENU_SEND);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(List.of(
                List.of(button)
        ));
        sendDocument.setReplyMarkup(inlineKeyboardMarkup);
        return sendDocument;
    }

    public static SendMessage showUsers(Update update) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(List.of(List.of("Show users", "Show converters","Send Advertising")));
        sendMessage.setReplyMarkup(markup);
        sendMessage.setText(CHOOSE_OPTION);
        return sendMessage;
    }

    public static SendMessage SendAdd(String chatId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(ADD_TEXT);
        return sendMessage;
    }
}
