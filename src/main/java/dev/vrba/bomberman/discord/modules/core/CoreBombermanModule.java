package dev.vrba.bomberman.discord.modules.core;

import dev.vrba.bomberman.discord.modules.BombermanModule;
import dev.vrba.bomberman.discord.modules.core.commands.HelpCommand;
import dev.vrba.bomberman.discord.modules.core.commands.PingCommand;
import dev.vrba.bomberman.discord.modules.core.commands.acl.AllowCommand;
import dev.vrba.bomberman.discord.modules.core.commands.acl.DenyCommand;
import dev.vrba.bomberman.discord.modules.core.commands.acl.ListCommand;
import dev.vrba.bomberman.discord.modules.core.commands.acl.ResetCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CoreBombermanModule extends BombermanModule {

    private final AllowCommand allowCommand;

    private final DenyCommand denyCommand;

    private final ListCommand listCommand;

    private final ResetCommand resetCommand;

    private final HelpCommand helpCommand;

    @Autowired
    public CoreBombermanModule(
            @NotNull final AllowCommand allowCommand,
            @NotNull final DenyCommand denyCommand,
            @NotNull final ListCommand listCommand,
            @NotNull final ResetCommand resetCommand,
            @NotNull final HelpCommand helpCommand
    ) {
        this.allowCommand = allowCommand;
        this.denyCommand = denyCommand;
        this.listCommand = listCommand;
        this.resetCommand = resetCommand;
        this.helpCommand = helpCommand;
    }

    @Override
    public void register(@NotNull JDA api) {
        api.getPresence()
                .setActivity(Activity.watching("out for bombs on VŠE"));

        registerCommands(api,
                new PingCommand(),
                allowCommand,
                denyCommand,
                listCommand,
                resetCommand,
                helpCommand
        );
    }
}
