package us.racem.guilds.common;

import lombok.experimental.ExtensionMethod;
import org.slf4j.Logger;
import us.racem.guilds.common.extend.ObjectExtensions;
import us.racem.guilds.common.inject.Injector;
import us.racem.guilds.common.inject.Registry;

import javax.inject.Inject;

public class ComponentContainer {
    @Inject private Logger log;
    @Inject private Injector injector;

    public ComponentContainer() {

    }


}
