package service;

import model.StateDTO;
import model.User;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import util.BaseData;
import util.enums.BotState;
import util.enums.Role;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserService {
    public static List<User> userList;

    public static void getUserFromBack() {
        List<User> users = FileService.getUsers();
        if (users == null) userList = new ArrayList<>();
        else userList = users;
    }

    private static void saveUser(User user) {
        boolean cantFind = true;
        for (int i = 0; i < userList.size(); i++)
            if (userList.get(i).getChatId().equals(user.getChatId())) {
                userList.set(i, user);
                cantFind = false;
            }
        if (cantFind) userList.add(user);
        FileService.setUsers(userList);
    }

    public static User getByChatId(String chatId) {
        for (User user : userList)
            if (user.getChatId().equals(chatId)) return user;
        return null;
    }

    public static void savePhoneNumber(String phoneNumber, String chatId) {
        for (User user : userList)
            if (user.getChatId().equals(chatId)) {
                user.setPhoneNumber("+" + phoneNumber);
                saveUser(user);
            }
    }

    public static boolean phoneNumberValidation(String phone) {
        if (phone.length() != 13 || !phone.startsWith("+998")) return false;

        for (int i = phone.length() - 1; i > 3; i--)
            if (!Character.isDigit(phone.charAt(i))) return false;
        return true;
    }

    public static boolean checkByPhoneNumber(String number) {
        for (User user : userList)
            if (user.getPhoneNumber() != null && user.getPhoneNumber().equals(number)) return true;
        return false;
    }

    public static User getById(UUID id) {
        for (User user : userList)
            if (user.getId().equals(id)) return user;
        return null;
    }

    public static StateDTO getAndCheck(Update update) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        String chatId = message.getChatId().toString();
        User user = null;
        for (User item : userList)
            if (item.getChatId().equals(chatId)) {
                user = item;
                break;
            }
        if (user == null) {
            var from = message.getFrom();
            user = new User(from.getFirstName(), "", from.getUserName(), chatId, BotState.REGISTER, Role.USER, false);
            saveUser(user);
        }
        if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().equals(BaseData.TOKEN))
            user.setRole(Role.ADMIN);

        return new StateDTO(user.getState(), user.getRole(), user.isAdmin());
    }

    public static void saveStateAndLan(Update update, StateDTO lanStateDTO) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        String chatId = message.getChatId().toString();
        for (User user : userList)
            if (user.getChatId().equals(chatId)) {
                user.setState(lanStateDTO.getState());
                user.setRole(lanStateDTO.getRole());
                saveUser(user);
            }
    }

    public static InputFile getActiveUsers() {
        String fileName = "Users";
        File fileForm = new File(BaseData.FILE_URL + "/ExcelFile/UsersForm.xlsx");
        File file = new File(BaseData.FILE_URL + "/ExcelFile/" + fileName + ".xlsx");

        try (FileInputStream inputStream = new FileInputStream(fileForm)) {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            int a = 0;
            Sheet sheet = workbook.getSheet("UserList");
            int rowNum = 3;
            for (User user : userList) {
                Row row = sheet.getRow(rowNum);
                row.createCell(1).setCellValue(rowNum - 3);
                row.createCell(2).setCellValue(user.getName());
                row.createCell(3).setCellValue(user.getUsername());
                row.createCell(5).setCellValue(user.getState().toString());
                row.createCell(6).setCellValue(user.getCreatedDate());
                row.createCell(7).setCellValue(user.getPhoneNumber());
                rowNum++;
            }
            for (int i = 0; i < 7; i++) {
                sheet.autoSizeColumn(i);
            }
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            outputStream.close();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new InputFile(file);
    }


}
