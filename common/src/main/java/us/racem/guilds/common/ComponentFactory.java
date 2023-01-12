package us.racem.guilds.common;

import com.google.inject.Inject;
import com.google.inject.Injector;
import lombok.experimental.ExtensionMethod;
import us.racem.guilds.common.exception.ComponentLifecycleException;
import us.racem.guilds.common.exception.ComponentLifecycleStage;
import us.racem.guilds.common.extend.ObjectExtensions;
import us.racem.guilds.common.mark.Component;
import us.racem.guilds.common.mark.Koyomi;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.perfectable.introspection.Introspections.introspect;

@ExtensionMethod({ ObjectExtensions.class })
public class ComponentFactory {
    private Injector injector;
    private MethodHandles.Lookup lookup = MethodHandles.lookup();

    private String idOf(Class<?> type) {
        if (type.isAnnotationPresent(Koyomi.class)) return type.getSimpleName().toLowerCase();

        String name = type.getAnnotation(Component.class).name();
        return (name == null || name.isEmpty()) ?
                type.getSimpleName().toLowerCase() :
                name;
    }

    private Constructor<?> find(Class<?> type) throws ComponentLifecycleException {
        return introspect(type)
                .constructors()
                .filter(c ->
                        c.isAnnotationPresent(Inject.class)
                        || c.getParameterCount() == 0)
                .requiringModifier(Modifier.PUBLIC)
                .option()
                .orElseThrow(() -> new ComponentLifecycleException(ComponentLifecycleStage.VALIDATE));
    }

    private MethodHandle[] find(Class<?> type, Class<? extends Annotation> mark) throws IllegalAccessException {
        return introspect(type)
                .methods()
                .annotatedWith(mark)
                .asAccessible()
                .stream()
                .map(lookup::unreflect)
                .toArray(MethodHandle[]::new);
    }

    public <T> ComponentBox<T> create(Class<?> type) throws ComponentLifecycleException {
        try {
            Constructor<?> constructor = find(type);
            Object inst = constructor.newInstance();

            MethodHandle[] postConstructs = find(type, PostConstruct.class);
            MethodHandle[] preDestroys = find(type, PreDestroy.class);

            String name = idOf(type);
            return new ComponentBox<>(postConstructs, preDestroys, name, (T) inst);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException err) {
            throw new ComponentLifecycleException(ComponentLifecycleStage.CONSTRUCT, err);
        }
    }
}
