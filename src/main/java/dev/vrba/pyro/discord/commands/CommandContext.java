package dev.vrba.pyro.discord.commands;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CommandContext {
    @NotNull TextChannel getChannel();
    @NotNull GuildMessageReceivedEvent getEvent();

    @NotNull List<User> getMentionedUsers();
    @NotNull List<Role> getMentionedRoles();
    @NotNull List<TextChannel> getMentionedTextChannels();
}
