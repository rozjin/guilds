package us.racem.guilds.common.exception;

public enum ComponentLifecycleStage {
    STARTUP,

    VALIDATE,

    CONSTRUCT,
    POST_CONSTRUCT,
    PRE_DESTROY
}
