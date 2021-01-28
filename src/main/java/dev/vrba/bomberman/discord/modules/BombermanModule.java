package dev.vrba.bomberman.discord.modules;

import dev.vrba.bomberman.discord.commands.Command;
import dev.vrba.bomberman.discord.commands.CommandListener;
import net.dv8tion.jda.api.JDA;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public abstract class BombermanModule {

    protected Logger logger = Logger.getAnonymousLogger();

    public abstract void register(@NotNull JDA api);

    protected void registerCommands(@NotNull JDA api, @NotNull Command... commands) {
        ArrayList<Command> list = new ArrayList<>(Arrays.asList(commands));
        registerCommands(api, list);
    }

    protected void registerCommands(@NotNull JDA api, @NotNull List<Command> commands) {
        api.addEventListener(new CommandListener(commands));

        logger.info("Registering commands: " +
                commands.stream()
                .map(command -> CommandListener.COMMAND_PREFIX + command.getName())
                .collect(Collectors.joining(", ")));
    }
}
