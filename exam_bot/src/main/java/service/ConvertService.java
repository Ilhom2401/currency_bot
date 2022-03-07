package service;

import model.Convert;
import model.User;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import util.enums.BotState;

import java.util.ArrayList;
import java.util.List;

public class ConvertService {
    public static List<Convert> convertList = new ArrayList<>();

    public static void getConvertFromBack() {
        List<Convert> converts = FileService.getConverts();
        if (converts == null) convertList = new ArrayList<>();
        else convertList = converts;
    }

    public static Convert getConvertByChatId(String chatId) {
        for (Convert convert : convertList) {
            if (convert.getChatId().equals(chatId)) {
                return convert;
            }
        }
        return new Convert();
    }

    public static void setConvert(Update update, BotState status) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        String data = update.getCallbackQuery().getData();
        Convert crtConvert = getConvertByChatId(message.getChatId().toString());
        switch (status) {
            case CONVERT_FROM -> crtConvert.setFrom(data);
            case CONVERT_TO -> crtConvert.setTo(data);
            case CONVERT_SUM -> crtConvert.setAmount(data);
        }
        crtConvert.setChatId(message.getChatId().toString());
        crtConvert.setUsername(message.getFrom().getUserName());
        saveConvert(crtConvert);
    }

    public static void setConvertSum(Update update) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        Convert crtConvert = getConvertByChatId(message.getChatId().toString());
        crtConvert.setAmount(message.getText());
        crtConvert.setChatId(message.getChatId().toString());
        saveConvert(crtConvert);
    }

    private static void saveConvert(Convert convert) {
        boolean cantFind = true;
        for (int i = 0; i < convertList.size(); i++)
            if (convertList.get(i).getChatId().equals(convert.getChatId())) {
                convertList.set(i, convert);
                cantFind = false;
            }
        if (cantFind) convertList.add(convert);
        FileService.setConverts(convertList);
    }
}
