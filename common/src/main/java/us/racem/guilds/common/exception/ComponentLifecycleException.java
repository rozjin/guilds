package us.racem.guilds.common.exception;

import lombok.Getter;

import java.util.function.Supplier;

public class ComponentLifecycleException extends Exception {
    @Getter public final ComponentLifecycleStage stage;

    public ComponentLifecycleException(ComponentLifecycleStage stage) {
        this.stage = stage;
    }

    public ComponentLifecycleException(ComponentLifecycleStage stage, Throwable err) {
        super(err);
        this.stage = stage;
    }
}
