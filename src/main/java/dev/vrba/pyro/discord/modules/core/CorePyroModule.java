package dev.vrba.pyro.discord.modules.core;

import dev.vrba.pyro.discord.modules.PyroModule;
import dev.vrba.pyro.discord.modules.core.commands.PingCommand;
import dev.vrba.pyro.discord.modules.core.commands.acl.AllowCommand;
import dev.vrba.pyro.discord.modules.core.commands.acl.DenyCommand;
import dev.vrba.pyro.discord.modules.core.commands.acl.ListCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CorePyroModule extends PyroModule {

    private final AllowCommand allowCommand;

    private final DenyCommand denyCommand;

    private final ListCommand listCommand;

    @Autowired
    public CorePyroModule(
            @NotNull final AllowCommand allowCommand,
            @NotNull final DenyCommand denyCommand,
            @NotNull final ListCommand listCommand
    ) {
        this.allowCommand = allowCommand;
        this.denyCommand = denyCommand;
        this.listCommand = listCommand;
    }

    @Override
    public void register(@NotNull JDA api) {
        api.getPresence()
           .setActivity(Activity.watching("out for bombs on VŠE"));

        registerCommands(api,
            new PingCommand(),
            allowCommand,
            denyCommand,
            listCommand
        );
    }
}
