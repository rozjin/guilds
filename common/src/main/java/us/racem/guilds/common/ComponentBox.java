package us.racem.guilds.common;

import us.racem.guilds.common.exception.ComponentLifecycleException;
import us.racem.guilds.common.exception.ComponentLifecycleStage;

import java.lang.invoke.MethodHandle;

public class ComponentBox<T> {
    private final MethodHandle[] postConstructs;
    private final MethodHandle[] preDestroys;

    private String id;
    private T component;

    public ComponentBox(MethodHandle[] postConstructs,
                        MethodHandle[] preDestroys,

                        String name, T component) {
        this.postConstructs = postConstructs;
        this.preDestroys = preDestroys;

        this.id = name;
        this.component = component;
    }

    public String name() {
        return id;
    }

    public void postConstruct() throws ComponentLifecycleException {
        if (postConstructs == null) return;
        try {
            for (MethodHandle postConstruct : postConstructs) {
                postConstruct.invoke();
            }
        } catch (Throwable err) {
            throw new ComponentLifecycleException(ComponentLifecycleStage.POST_CONSTRUCT, err);
        }
    }

    public void preDestroy() throws ComponentLifecycleException {
        if (preDestroys == null) return;
        try {
            for (MethodHandle preDestroy : preDestroys) {
                preDestroy.invoke();
            }
        } catch (Throwable err) {
            throw new ComponentLifecycleException(ComponentLifecycleStage.PRE_DESTROY, err);
        }
    }
}
