package me.trae.launchpad.commands;

import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import io.github.trae.di.annotations.type.component.Component;
import io.github.trae.hf.Manager;
import io.github.trae.hf.Module;
import io.github.trae.hytale.framework.utility.UtilMessage;
import io.github.trae.utilities.UtilString;
import me.trae.launchpad.LaunchPadPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

@Component
public class LaunchPadCommand extends AbstractCommand implements Module<LaunchPadPlugin, Manager<LaunchPadPlugin>> {

    public LaunchPadCommand() {
        super("launchpad", "Plugin Information");
    }

    @Nullable
    @Override
    protected CompletableFuture<Void> execute(@Nonnull final CommandContext commandContext) {
        final CommandSender sender = commandContext.sender();

        UtilMessage.message(sender, "LaunchPad", "Plugin Information:");
        UtilMessage.message(sender, UtilString.pair("Author", "<yellow>Trae"));
        UtilMessage.message(sender, UtilString.pair("Version", "<green>%s".formatted(this.getPlugin().getManifest().getVersion().toString())));

        return null;
    }
}