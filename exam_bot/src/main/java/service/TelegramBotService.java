package service;


import model.Convert;
import model.Currency;
import model.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import util.enums.Constant;
import util.enums.Role;

import java.util.ArrayList;
import java.util.List;

public class TelegramBotService implements Constant {
    public static SendMessage error(Update update) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText("❗️ ❗️ ❗️");
        List<List<InlineKeyboardButton>> list = new ArrayList<>();
        list.add(backButton(message.getChatId().toString()));
        sendMessage.setReplyMarkup(new InlineKeyboardMarkup(list));
        return sendMessage;
    }

    public static List<InlineKeyboardButton> backButton(String chatId) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText("⬅️ Back");
        inlineKeyboardButton.setCallbackData("MAIN_MENU");
        List<InlineKeyboardButton> inlineKeyboardButtons1 = new ArrayList<>();
        inlineKeyboardButtons1.add(inlineKeyboardButton);
        return inlineKeyboardButtons1;
    }

    public static List<InlineKeyboardButton> backButton1(String chatId) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText("⬅️ Back");
        inlineKeyboardButton.setCallbackData("1");
        List<InlineKeyboardButton> inlineKeyboardButtons1 = new ArrayList<>();
        inlineKeyboardButtons1.add(inlineKeyboardButton);
        return inlineKeyboardButtons1;
    }

    public static List<InlineKeyboardButton> nextButton2(String chatId) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText("➡️ Next");
        inlineKeyboardButton.setCallbackData("2");
        List<InlineKeyboardButton> inlineKeyboardButtons1 = new ArrayList<>();
        inlineKeyboardButtons1.add(inlineKeyboardButton);
        return inlineKeyboardButtons1;
    }

    public static SendMessage getRegister(Update update) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setSelective(true);
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(false);
        List<KeyboardRow> rowList = new ArrayList<>();
        markup.setKeyboard(rowList);
        KeyboardRow row = new KeyboardRow();
        rowList.add(row);
        KeyboardButton button = new KeyboardButton("MY PHONE_NUMBER");
        button.setRequestContact(true);
        row.add(button);
        KeyboardRow row1 = new KeyboardRow();
        rowList.add(row1);
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        User user = UserService.getByChatId(message.getChatId().toString());
        assert user != null;
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), user.getName() + ", share your contact me...");
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }

    public static SendMessage setError(Update update) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        return new SendMessage(message.getChatId().toString(), Constant.ERROR);
    }

    public static SendMessage removeKeyBoardMarkup(Update update) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), "⬅️⬅️⬅️");
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        return sendMessage;
    }

    public static SendMessage chooseCurrency(Update update) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(List.of(List.of(INFO_CURR, CONVERT_CURR)));
        sendMessage.setReplyMarkup(markup);
        sendMessage.setText(CHOOSE_OPTION);
        return sendMessage;
    }

    public static EditMessageText currencyInfoEdit(Update update) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        List<List<String>> rows = new ArrayList<>();
        List<String> row = new ArrayList<>();
        CurrencyService currencyService = new CurrencyService();
        for (int i = 0; i < currencyService.getCurrencies().size() / 2; i++) {
            row.add(currencyService.getCurrencies().get(i).getCcy());
            if ((i + 1) % 4 == 0) {
                rows.add(row);
                row = new ArrayList<>();
            }
        }
        rows.add(row);
        rows.add(List.of(NEXT));
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(rows);
        EditMessageText editMessageText = new EditMessageText(CHOOSE_CURR);
        editMessageText.setChatId(message.getChatId().toString());
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setReplyMarkup(markup);
        return editMessageText;
    }

    public static EditMessageText currencyInfoEdit2(Update update) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        List<List<String>> rows = new ArrayList<>();
        List<String> row = new ArrayList<>();
        CurrencyService currencyService = new CurrencyService();
        for (int i = currencyService.getCurrencies().size() / 2; i < currencyService.getCurrencies().size(); i++) {
            row.add(currencyService.getCurrencies().get(i).getCcy());
            if ((i + 1) % 4 == 0) {
                rows.add(row);
                row = new ArrayList<>();
            }
        }
        rows.add(row);
        rows.add(List.of(PREV));
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(rows);
        EditMessageText editMessageText = new EditMessageText(CHOOSE_CURR);
        editMessageText.setChatId(message.getChatId().toString());
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setReplyMarkup(markup);
        return editMessageText;
    }

    public static SendMessage currencyInfo(Update update) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        List<List<String>> rows = new ArrayList<>();
        List<String> row = new ArrayList<>();
        CurrencyService currencyService = new CurrencyService();
        for (int i = 0; i < currencyService.getCurrencies().size() / 2; i++) {
            row.add(currencyService.getCurrencies().get(i).getCcy());
            if ((i + 1) % 4 == 0) {
                rows.add(row);
                row = new ArrayList<>();
            }
        }
        rows.add(row);
        rows.add(List.of(NEXT));
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(rows);
        sendMessage.setText(CHOOSE_CURR);
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }

    public static SendMessage currencyInfo2(Update update) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        List<List<String>> rows = new ArrayList<>();
        List<String> row = new ArrayList<>();
        CurrencyService currencyService = new CurrencyService();
        for (int i = currencyService.getCurrencies().size() / 2; i < currencyService.getCurrencies().size(); i++) {
            row.add(currencyService.getCurrencies().get(i).getCcy());
            if ((i + 1) % 4 == 0) {
                rows.add(row);
                row = new ArrayList<>();
            }
        }
        rows.add(row);
        rows.add(List.of(PREV));
        InlineKeyboardMarkup markup = InlineKeyboardService.createMarkup(rows);
        sendMessage.setText(CHOOSE_CURR);
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }

    public static SendMessage currencyInfoText(Update update) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        Currency selectedCurrency = CurrencyService.getSelectedCurrency(update.getCallbackQuery().getData());
        assert selectedCurrency != null;
        sendMessage.setText(selectedCurrency.getNominal() + " " + selectedCurrency.getCcy() + " = " + selectedCurrency.getRate() + " " + SOM + "\n\n" + Date + selectedCurrency.getDate());
        List<InlineKeyboardButton> inlineKeyboardButtons = backButton(message.getChatId().toString());
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(List.of(inlineKeyboardButtons));
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }

    public static SendMessage convertFrom(Update update) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        SendMessage sendMessage = currencyInfo(update);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(CONVERT_FROM);
        return sendMessage;
    }

    public static EditMessageText convertFrom2Edit(Update update) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        EditMessageText editMessageText = currencyInfoEdit2(update);
        editMessageText.setText(CONVERT_FROM);
        editMessageText.setChatId(message.getChatId().toString());
        editMessageText.setMessageId(message.getMessageId());
        return editMessageText;
    }

    public static EditMessageText convertFromEdit(Update update) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        EditMessageText editMessageText = currencyInfoEdit(update);
        editMessageText.setText(CONVERT_FROM);
        editMessageText.setChatId(message.getChatId().toString());
        editMessageText.setMessageId(message.getMessageId());
        return editMessageText;
    }

    public static SendMessage convertTo(Update update) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        SendMessage sendMessage = currencyInfo(update);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(CONVERT_TO);
        return sendMessage;
    }

    public static EditMessageText convertToEdit(Update update) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        EditMessageText editMessageText = currencyInfoEdit(update);
        editMessageText.setText(CONVERT_TO);
        editMessageText.setChatId(message.getChatId().toString());
        editMessageText.setMessageId(message.getMessageId());
        return editMessageText;
    }

    public static EditMessageText convertTo2Edit(Update update) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        EditMessageText editMessageText = currencyInfoEdit2(update);
        editMessageText.setText(CONVERT_TO);
        editMessageText.setChatId(message.getChatId().toString());
        editMessageText.setMessageId(message.getMessageId());
        return editMessageText;
    }

    public static SendMessage convertSum(Update update) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(CONVERT_SUM);
        return sendMessage;
    }

    public static SendMessage convertSum2(Update update) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(CONVERT_SUM);
        return sendMessage;
    }

    public static SendMessage convertInfo(Update update) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        Convert convertByChatId = ConvertService.getConvertByChatId(message.getChatId().toString());
        assert convertByChatId != null;
        Currency fromCurr = CurrencyService.getSelectedCurrency(convertByChatId.getFrom());
        Currency toCurr = CurrencyService.getSelectedCurrency(convertByChatId.getTo());
        assert fromCurr != null;
        assert toCurr != null;
        sendMessage.setText(convertByChatId.getAmount() + " " + fromCurr.getCcy() + " - " + (Double.parseDouble(convertByChatId.getAmount()) * Double.parseDouble(fromCurr.getRate()) / Double.parseDouble(toCurr.getRate()) + " " + toCurr.getCcy()));
        List<InlineKeyboardButton> inlineKeyboardButtons = backButton(message.getChatId().toString());
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(List.of(inlineKeyboardButtons));
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }
}

