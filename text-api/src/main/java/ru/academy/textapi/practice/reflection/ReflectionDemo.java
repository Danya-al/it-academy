package ru.academy.textapi.practice.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class ReflectionDemo {

    public static void main(String[] args) throws Exception {
        Cat cat = new Cat("Мурзик", 3);
        System.out.println("До: " + cat);

        Class<?> ourClass = cat.getClass();

        Method secretMethod = ourClass.getDeclaredMethod("secret", String.class);
        secretMethod.setAccessible(true);

        Field nameField = ourClass.getDeclaredField("name");
        nameField.setAccessible(true);

        nameField.set(cat, "Барсик");
        System.out.println("После: " + cat);

        String newName = (String) nameField.get(cat);
        System.out.println("Прочитали через рефлексию: " + newName);

        String result = (String) secretMethod.invoke(cat, "мяу");
        System.out.println("Результат секретного метода: " + result);

        Cat mappedCat = new Cat("Мурзик", 3);

        Map<String, Object> values = Map.of(
                "name", "Тишка",
                "age", "5"
        );

        ReflectionMapper.fill(mappedCat, values);
        System.out.println(mappedCat);
    }
}