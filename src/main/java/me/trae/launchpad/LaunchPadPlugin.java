package me.trae.launchpad;

import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import io.github.trae.di.annotations.type.Application;
import io.github.trae.hytale.framework.HytalePlugin;

import javax.annotation.Nonnull;

@Application
public class LaunchPadPlugin extends HytalePlugin {

    public LaunchPadPlugin(@Nonnull final JavaPluginInit javaPluginInit) {
        super(javaPluginInit);
    }

    @Override
    protected void setup() {
        this.initializePlugin();
    }

    @Override
    protected void shutdown() {
        this.shutdownPlugin();
    }
}