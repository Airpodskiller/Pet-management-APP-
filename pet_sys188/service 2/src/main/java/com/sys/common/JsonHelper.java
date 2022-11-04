package com.sys.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.ObjectTypeAdapter;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class JsonHelper {
    public static String ToJson(Object obj){
        try {
            Gson gson = new GsonBuilder().create();
            try {
                Field factories = Gson.class.getDeclaredField("factories");
                factories.setAccessible(true);
                Object o = factories.get(gson);
                Class<?>[] declaredClasses = Collections.class.getDeclaredClasses();
                for (Class c : declaredClasses) {
                    if ("java.util.Collections$UnmodifiableList".equals(c.getName())) {
                        Field listField = c.getDeclaredField("list");
                        listField.setAccessible(true);
                        List<TypeAdapterFactory> list = (List<TypeAdapterFactory>) listField.get(o);
                        int i = list.indexOf(ObjectTypeAdapter.FACTORY);
                        list.set(i, MapTypeAdapter.FACTORY);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return gson.toJson(obj);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static <T> T ToModel(String json, Class<T> tclass){
        try{
            Gson gson = new Gson();
            try {
                Field factories = Gson.class.getDeclaredField("factories");
                factories.setAccessible(true);
                Object o = factories.get(gson);
                Class<?>[] declaredClasses = Collections.class.getDeclaredClasses();
                for (Class c : declaredClasses) {
                    if ("java.util.Collections$UnmodifiableList".equals(c.getName())) {
                        Field listField = c.getDeclaredField("list");
                        listField.setAccessible(true);
                        List<TypeAdapterFactory> list = (List<TypeAdapterFactory>) listField.get(o);
                        int i = list.indexOf(ObjectTypeAdapter.FACTORY);
                        list.set(i, MapTypeAdapter.FACTORY);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return gson.fromJson(json,tclass);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Map> ToList(String json){
        try{
            Gson gson = new Gson();
            try {
                Field factories = Gson.class.getDeclaredField("factories");
                factories.setAccessible(true);
                Object o = factories.get(gson);
                Class<?>[] declaredClasses = Collections.class.getDeclaredClasses();
                for (Class c : declaredClasses) {
                    if ("java.util.Collections$UnmodifiableList".equals(c.getName())) {
                        Field listField = c.getDeclaredField("list");
                        listField.setAccessible(true);
                        List<TypeAdapterFactory> list = (List<TypeAdapterFactory>) listField.get(o);
                        int i = list.indexOf(ObjectTypeAdapter.FACTORY);
                        list.set(i, MapTypeAdapter.FACTORY);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return gson.fromJson(json, List.class);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
