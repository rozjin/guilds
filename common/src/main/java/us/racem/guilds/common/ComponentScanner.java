package us.racem.guilds.common;

import com.google.common.collect.Iterables;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import us.racem.guilds.common.exception.ComponentLifecycleException;
import us.racem.guilds.common.exception.ComponentLifecycleStage;
import us.racem.guilds.common.mark.Koyomi;
import us.racem.guilds.common.mark.Scan;

import java.util.Set;

import static org.reflections.scanners.Scanners.*;

public class ComponentScanner {
    private Reflections reflector;
    private ComponentFactory factory;

    public Object application;

    public ComponentScanner() throws ComponentLifecycleException {
        // TODO: Remove reflections hack, kind of breacks the lifecycle

        Reflections preReflector = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forJavaClassPath())
                .filterInputsBy(new FilterBuilder().excludePackage("us.racem.guilds.koyomi"))
                .setScanners(TypesAnnotated, MethodsAnnotated, MethodsReturn));
        Set<Class<?>> applicationKlasses = preReflector.getTypesAnnotatedWith(Koyomi.class);
        Class<?> applicationKlass = Iterables.getOnlyElement(applicationKlasses);

        String[] paths = null;
        if (applicationKlass.isAnnotationPresent(Scan.class)) paths = applicationKlass.getAnnotation(Scan.class).paths();
        else paths = new String[] { applicationKlass.getPackage().getName() };

        factory = new ComponentFactory();
        reflector = new Reflections(new ConfigurationBuilder()
                .forPackages(paths)
                .setScanners(TypesAnnotated, MethodsAnnotated, FieldsAnnotated, ConstructorsAnnotated));

        try {
            application = factory.createApp(applicationKlass);
        } catch (ComponentLifecycleException err) {
            throw new ComponentLifecycleException(ComponentLifecycleStage.STARTUP, err);
        }
    }
}
