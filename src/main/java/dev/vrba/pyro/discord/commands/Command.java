package dev.vrba.pyro.discord.commands;

import org.jetbrains.annotations.NotNull;

public interface Command {
    @NotNull String getName();

    default @NotNull String getDescription() {
        return "No description provided";
    }

    default @NotNull String getHelp() {
        return "No help provided";
    }

    void execute(@NotNull CommandContext context);
}
