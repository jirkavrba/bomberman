package dev.vrba.pyro.discord.modules.core;

import dev.vrba.pyro.discord.modules.PyroModule;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class CorePyroModule extends PyroModule {

    @Override
    public void register(@NotNull JDA api) {
        api.getPresence()
           .setActivity(Activity.watching("out for bombs on VŠE"));
    }
}
