package dev.vrba.pyro.discord.modules.core;

import dev.vrba.pyro.discord.modules.PyroModule;
import dev.vrba.pyro.discord.modules.core.commands.PingCommand;
import dev.vrba.pyro.discord.modules.core.commands.acl.AllowCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CorePyroModule extends PyroModule {

    private final AllowCommand allowCommand;

    @Autowired
    public CorePyroModule(@NotNull final AllowCommand allowCommand) {
        this.allowCommand = allowCommand;
    }

    @Override
    public void register(@NotNull JDA api) {
        api.getPresence()
           .setActivity(Activity.watching("out for bombs on VŠE"));

        registerCommands(api,
            new PingCommand(),
            allowCommand
        );
    }
}
