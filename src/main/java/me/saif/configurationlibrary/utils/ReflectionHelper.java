package me.saif.configurationlibrary.utils;

import java.lang.reflect.Field;

public class ReflectionHelper {

    public static Field[] getAllFields(Class<?> clazz) {
        Field[] publicFields = clazz.getFields();
        Field[] privateFields = clazz.getDeclaredFields();

        Field[] all = new Field[privateFields.length + publicFields.length];

        for (int i = 0; i < all.length; i++) {
            all[i] = i < publicFields.length ? publicFields[i] : privateFields[i-publicFields.length];
        }
        return all;
    }

}
