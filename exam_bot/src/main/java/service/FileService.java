package service;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Convert;
import model.User;
import util.BaseData;

import java.io.*;
import java.util.List;

public class FileService {

    static GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
    static Gson gson = builder.create();
    static String line;
    static StringBuilder str;

    public static void setUsers(List<User> userList) {
        File file = new File(BaseData.FILE_URL + "/user.txt");
        String jsonFile = gson.toJson(userList);
        try (FileWriter fileWriter = new FileWriter(file);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(jsonFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<User> getUsers(){
        File file = new File(BaseData.FILE_URL + "/user.txt");
        str = new StringBuilder();
        try (FileReader fileReader = new FileReader(file); BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            while ((line = bufferedReader.readLine()) != null)
                str.append(line).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String jsonFile = new String(str);
        return gson.fromJson(jsonFile,new TypeToken<List<User>>(){}.getType());
    }

    public static void setConverts(List<Convert> userList) {
        File file = new File(BaseData.FILE_URL + "/convert.txt");
        String jsonFile = gson.toJson(userList);
        try (FileWriter fileWriter = new FileWriter(file);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(jsonFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Convert> getConverts(){
        File file = new File(BaseData.FILE_URL + "/convert.txt");
        str = new StringBuilder();
        try (FileReader fileReader = new FileReader(file); BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            while ((line = bufferedReader.readLine()) != null)
                str.append(line).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String jsonFile = new String(str);
        return gson.fromJson(jsonFile,new TypeToken<List<Convert>>(){}.getType());
    }

    public static void saveFile(File file, String originalName) {
        File file1=new File(BaseData.FILE_URL+"/img/"+originalName);
        try(    FileOutputStream fileOutputStream = new FileOutputStream(file1);
                FileInputStream fileInputStream = new FileInputStream(file) )
        {
                fileOutputStream.write(fileInputStream.readAllBytes());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
