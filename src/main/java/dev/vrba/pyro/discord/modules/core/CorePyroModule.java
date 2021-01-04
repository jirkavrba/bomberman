package dev.vrba.pyro.discord.modules.core;

import dev.vrba.pyro.discord.modules.PyroModule;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import org.springframework.stereotype.Component;

@Component
public class CorePyroModule implements PyroModule {

    @Override
    public void register(JDA api) {
        api.getPresence()
           .setActivity(Activity.watching("out for bombs on VŠE"));
    }
}
