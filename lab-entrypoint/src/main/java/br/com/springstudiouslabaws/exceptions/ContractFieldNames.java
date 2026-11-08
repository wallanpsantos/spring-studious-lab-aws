package br.com.springstudiouslabaws.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.RecordComponent;
import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Converte o caminho Java da validação para o nome publicado no JSON.
 */
public final class ContractFieldNames {

    private static final Pattern SEGMENT = Pattern.compile("([^.\\[]+)(\\[\\d+])?");

    private ContractFieldNames() {
    }

    public static String toJsonPath(Class<?> root, String fieldPath) {
        if (fieldPath == null || fieldPath.isBlank()) {
            return fieldPath;
        }
        StringBuilder jsonPath = new StringBuilder();
        Class<?> current = root;
        Matcher matcher = SEGMENT.matcher(fieldPath);
        while (matcher.find()) {
            String javaName = matcher.group(1);
            String index = matcher.group(2);
            if (!jsonPath.isEmpty()) {
                jsonPath.append('.');
            }
            jsonPath.append(jsonName(current, javaName));
            if (index != null) {
                jsonPath.append(index);
            }
            current = nextType(current, javaName);
        }
        return jsonPath.isEmpty() ? fieldPath : jsonPath.toString();
    }

    private static String jsonName(Class<?> type, String javaName) {
        if (type == null || !type.isRecord()) {
            return javaName;
        }
        for (RecordComponent component : type.getRecordComponents()) {
            if (component.getName().equals(javaName)) {
                JsonProperty property = jsonProperty(type, component);
                return property == null || property.value().isBlank() ? javaName : property.value();
            }
        }
        return javaName;
    }

    private static JsonProperty jsonProperty(Class<?> type, RecordComponent component) {
        JsonProperty onAccessor = annotation(component.getAccessor());
        if (onAccessor != null) {
            return onAccessor;
        }
        try {
            JsonProperty onField = annotation(type.getDeclaredField(component.getName()));
            if (onField != null) {
                return onField;
            }
        } catch (NoSuchFieldException exception) {
            // O compilador pode guardar @JsonProperty só no parâmetro do construtor canônico.
        }
        return constructorParameter(type, component);
    }

    private static JsonProperty annotation(java.lang.reflect.AnnotatedElement element) {
        if (element == null) {
            return null;
        }
        JsonProperty property = element.getDeclaredAnnotation(JsonProperty.class);
        return property == null || property.value().isBlank() ? null : property;
    }

    private static JsonProperty constructorParameter(Class<?> type, RecordComponent component) {
        RecordComponent[] components = type.getRecordComponents();
        Class<?>[] parameterTypes = new Class<?>[components.length];
        int index = -1;
        for (int i = 0; i < components.length; i++) {
            parameterTypes[i] = components[i].getType();
            if (components[i].getName().equals(component.getName())) {
                index = i;
            }
        }
        if (index < 0) {
            return null;
        }
        try {
            Annotation[] annotations = type.getDeclaredConstructor(parameterTypes).getParameterAnnotations()[index];
            for (Annotation annotation : annotations) {
                if (annotation instanceof JsonProperty property && !property.value().isBlank()) {
                    return property;
                }
            }
        } catch (NoSuchMethodException | ArrayIndexOutOfBoundsException exception) {
            return null;
        }
        return null;
    }

    private static Class<?> nextType(Class<?> type, String javaName) {
        if (type == null || !type.isRecord()) {
            return null;
        }
        for (RecordComponent component : type.getRecordComponents()) {
            if (!component.getName().equals(javaName)) {
                continue;
            }
            Class<?> elementType = listElement(component);
            return elementType == null ? component.getType() : elementType;
        }
        return null;
    }

    private static Class<?> listElement(RecordComponent component) {
        Type generic = component.getGenericType();
        if (generic instanceof ParameterizedType parameterized) {
            return classArgument(parameterized);
        }
        AnnotatedType annotated = component.getAnnotatedType();
        if (annotated instanceof AnnotatedParameterizedType parameterized
                && parameterized.getType() instanceof ParameterizedType raw) {
            return classArgument(raw);
        }
        return null;
    }

    private static Class<?> classArgument(ParameterizedType parameterized) {
        if (parameterized.getActualTypeArguments().length != 1) {
            return null;
        }
        Type argument = parameterized.getActualTypeArguments()[0];
        return argument instanceof Class<?> elementType ? elementType : null;
    }
}
