package dev.vrba.pyro.discord.modules;

import dev.vrba.pyro.discord.commands.Command;
import dev.vrba.pyro.discord.commands.CommandListener;
import net.dv8tion.jda.api.JDA;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public abstract class PyroModule {

    public abstract void register(@NotNull JDA api);

    protected void registerCommands(@NotNull JDA api, @NotNull Command... commands) {
        ArrayList<Command> list = new ArrayList<>(Arrays.asList(commands));
        registerCommands(api, list);
    }

    protected void registerCommands(@NotNull JDA api, @NotNull List<Command> commands) {
        api.addEventListener(new CommandListener(commands));
    }
}
