package com.app.dbrah.uis.activity_home.search_module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SearchHistoryViewModel extends AndroidViewModel {

    private static final int MAX_HISTORY_SIZE = 5;

    private static final String PREF_NAME = "string_list_pref";
    private static final String KEY_STRING_LIST = "string_list";

    private List<String> stringList;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    public SearchHistoryViewModel(Application context) {
        super(context);
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
        stringList = loadStringListFromPrefs();
    }

    public void addString(String newString) {
        if (newString.isEmpty())return;

        stringList.remove(newString);
        stringList.add(newString);
        Collections.sort(stringList, new StringDateComparator());
        // Remove the oldest items if the list size exceeds the maximum allowed size
        while (stringList.size() > MAX_HISTORY_SIZE) {
            stringList.remove(0); // Remove the oldest item
        }

        saveStringListToPrefs();
    }

    public List<String> getStringList() {
        List<String> temp =new ArrayList<>(stringList);
        Collections.reverse(temp);
        return temp;
    }

    private List<String> loadStringListFromPrefs() {
        String json = sharedPreferences.getString(KEY_STRING_LIST, null);
        Type type = new TypeToken<List<String>>() {
        }.getType();
        if (json != null) {
            return gson.fromJson(json, type);
        } else {
            return new ArrayList<>();
        }
    }

    private void saveStringListToPrefs() {
        String json = gson.toJson(stringList);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_STRING_LIST, json);
        editor.apply();
    }

    private static class StringDateComparator implements Comparator<String> {
        private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);

        @Override
        public int compare(String s1, String s2) {
            try {
                Date date1 = dateFormat.parse(s1);
                Date date2 = dateFormat.parse(s2);
                return date1.compareTo(date2);
            } catch (ParseException e) {
                e.printStackTrace();
                return 0; // Handle parsing errors as needed
            }
        }
    }

}