package us.racem.guilds.common.inject;

import lombok.experimental.ExtensionMethod;
import us.racem.guilds.common.extend.ObjectExtensions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

@ExtensionMethod({ ObjectExtensions.class })
public class Registry {
    private Map<Class<?>, Map<String, Object>> registry = new HashMap<>();

    private boolean isInstantiable(Class<?> type) {
        if (type.isPrimitive() ||
           Modifier.isAbstract(type.getModifiers()) ||
           type.isInterface() || type.isArray() ||
           type == String.class || type == Integer.class){
            return false;
        }

        return true;
    }

    @Nullable
    protected Object find(@Nonnull Class<?> type, String tag) {
        if (!registry.containsKey(type)) return null;

        Map<String, Object> bindings = registry.get(type);
        if (bindings.size() > 1 && tag == null) return null;
        else if (bindings.size() <= 1 && tag == null) return bindings.first();
        return bindings.get(tag);
    }

    protected void register(@Nonnull Class<?> type, @Nonnull String tag, @Nonnull Object inst) {
        if (!registry.containsKey(type)) registry.put(type, new HashMap<String, Object>() {{
            put(tag, inst);
        }});
    }
}
