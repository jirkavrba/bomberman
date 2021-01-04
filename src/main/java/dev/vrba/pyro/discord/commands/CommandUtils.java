package dev.vrba.pyro.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;

public class CommandUtils {
    public static void sendError(
            @NotNull final CommandContext context,
            @NotNull final String title
    ) {
        sendError(context, title, null);
    }

    public static void sendError(
            @NotNull final CommandContext context,
            @NotNull final String title,
            @Nullable final String text
    ) {
        final User author = context.getAuthor();

        EmbedBuilder builder = new EmbedBuilder()
            .setAuthor(author.getName(), null, author.getAvatarUrl())
            .setTimestamp(context.getMessage().getTimeCreated())
            .setColor(new Color(0xff4444))
            .setTitle(title);

        if (text != null) {
            builder.setDescription(text);
        }

        context.getChannel()
                .sendMessage(builder.build())
                .queue();
    }
}
