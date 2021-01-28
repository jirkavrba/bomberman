package dev.vrba.bomberman.discord.modules.core.commands;

import dev.vrba.bomberman.discord.commands.Command;
import dev.vrba.bomberman.discord.commands.CommandContext;
import dev.vrba.bomberman.discord.commands.CommandListener;
import dev.vrba.bomberman.discord.commands.ExcludeFromHelpListing;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
@ExcludeFromHelpListing
public class HelpCommand implements Command {

    private final Collection<Command> commands;

    public HelpCommand(@NotNull final ApplicationContext context) {
        this.commands = context.getBeansOfType(Command.class)
                .values()
                .stream()
                .filter(command -> !command.getClass().isAnnotationPresent(ExcludeFromHelpListing.class))
                .collect(Collectors.toList());
    }

    @Override
    public @NotNull String getName() {
        return "help";
    }

    @Override
    public @NotNull String getDescription() {
        return "Displays help for a command, if you read this, you probably know that tho";
    }

    @Override
    public @NotNull String getHelp() {
        return "Get yourself a dictionary and search for term 'recursion' 😉";
    }

    @Override
    public @NotNull Command.ExecutionSecurityPolicy getExecutionPolicy() {
        return ExecutionSecurityPolicy.DeterminedByACL;
    }

    @Override
    public void execute(@NotNull CommandContext context) {
        MessageEmbed embed = this.buildHelpEmbed(context);

        context.getChannel()
                .sendMessage(embed)
                .queue();
    }

    private MessageEmbed buildHelpEmbed(@NotNull CommandContext context) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("🤔 Help");
        builder.setColor(new Color(0x44C6DC));
        builder.setFooter(context.getAuthor().getName(), context.getAuthor().getAvatarUrl());

        this.commands.forEach(command -> builder.addField(
                "`" + CommandListener.COMMAND_PREFIX + command.getName() + "`",
                command.getDescription() + "\n\n" + command.getHelp(),
                false
        ));

        return builder.build();
    }
}
