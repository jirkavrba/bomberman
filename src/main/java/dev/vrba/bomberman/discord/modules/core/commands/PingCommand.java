package dev.vrba.bomberman.discord.modules.core.commands;

import dev.vrba.bomberman.discord.commands.Command;
import dev.vrba.bomberman.discord.commands.CommandContext;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class PingCommand implements Command {
    @Override
    public @NotNull String getName() {
        return "ping";
    }

    @Override
    public @NotNull ExecutionSecurityPolicy getExecutionPolicy() {
        return ExecutionSecurityPolicy.DeterminedByACL;
    }

    @Override
    public @NotNull String getDescription() {
        return "A basic command to quickly find out, if the bot is up and running";
    }

    @Override
    public void execute(@NotNull CommandContext context) {
        context.getChannel()
                .sendMessage("\uD83C\uDFD3 Pong!")
                .queue();
    }
}
