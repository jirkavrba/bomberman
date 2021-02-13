package dev.vrba.bomberman.discord.commands;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CommandContext {
    @NotNull Guild getGuild();
    @NotNull TextChannel getChannel();
    @NotNull Message getMessage();
    @NotNull User getAuthor();
    @NotNull GuildMessageReceivedEvent getEvent();

    @NotNull List<String> getArguments();

    @NotNull List<User> getMentionedUsers();
    @NotNull List<Role> getMentionedRoles();
    @NotNull List<TextChannel> getMentionedTextChannels();
}
