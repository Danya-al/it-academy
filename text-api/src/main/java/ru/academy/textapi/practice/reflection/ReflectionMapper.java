package ru.academy.textapi.practice.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReflectionMapper {

    public static void fill(Object target, Map<String, ?> values)
            throws ReflectiveOperationException {
        Class<?> targetClass = target.getClass();
        Map<Field, Object> convertedValues = new LinkedHashMap<>();

        for (var entry : values.entrySet()) {
            Field field = targetClass.getDeclaredField(entry.getKey());
            int modifiers = field.getModifiers();

            if (Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers)) {
                throw new IllegalArgumentException("Нельзя заполнить поле " + field.getName());
            }

            Object value = convert(entry.getValue(), field.getType());
            field.setAccessible(true);
            convertedValues.put(field, value);
        }

        for (var entry : convertedValues.entrySet()) {
            entry.getKey().set(target, entry.getValue());
        }
    }

    private static Object convert(Object value, Class<?> type) {
        if (value == null) {
            if (type.isPrimitive()) {
                throw new IllegalArgumentException("Тип " + type.getSimpleName() + " не принимает null");
            }
            return null;
        }

        if (type.isInstance(value)) {
            return value;
        }

        String text = value.toString();

        if (type == String.class) return text;
        if (type == byte.class || type == Byte.class) return new BigDecimal(text).byteValueExact();
        if (type == short.class || type == Short.class) return new BigDecimal(text).shortValueExact();
        if (type == int.class || type == Integer.class) return new BigDecimal(text).intValueExact();
        if (type == long.class || type == Long.class) return new BigDecimal(text).longValueExact();
        if (type == float.class || type == Float.class) return Float.valueOf(text);
        if (type == double.class || type == Double.class) return Double.valueOf(text);
        if (type == LocalDateTime.class) return LocalDateTime.parse(text);

        throw new IllegalArgumentException("Неподдерживаемый тип поля " + type.getSimpleName());
    }
}