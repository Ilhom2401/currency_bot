package service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Currencies;
import model.Currency;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class CurrencyService {
    public static ArrayList<Currency> currencies;

    {
        try {
            URL url = new URL("https://cbu.uz/uz/arkhiv-kursov-valyut/json/");
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            ObjectMapper objectMapper = new ObjectMapper();
            currencies = objectMapper.readValue(inputStream, new TypeReference<>() {
            });
        } catch (Exception e) {
            System.out.println("error !!!");
        }
    }

    public ArrayList<Currency> getCurrencies() {
        return currencies;
    }

    public static Currency getSelectedCurrency(String name){
        for (Currency currency : currencies) {
            if (currency.getCcy().equals(name))
                return currency;
        }
        return null;
    }
}
