package us.racem.guilds.common.extend;

import com.google.common.collect.Iterables;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class ObjectExtensions {
    @FunctionalInterface
    public interface CheckedFunction<T, R> {
        R apply(T t) throws Exception;
    }

    public static <T, R> Stream<R> map(Stream<T> stream, CheckedFunction<? super T, ? extends R> fn) {
        return stream.map((T t) -> {
            try {
                return fn.apply(t);
            } catch (Exception err) {
                throw new RuntimeException(err);
            }
        });
    }

    public static <T extends Annotation> T getAnnotation(Class<?> klass, Class<T> type) {
        Objects.requireNonNull(type);
        if (!klass.isAnnotationPresent(type)) return null;
        return (T) klass.getAnnotation(type);
    }

    public static <K, V> V first(Map<K, V> map) {
        return Iterables.getOnlyElement(map.values());
    }
}