package dev.vrba.bomberman.discord.modules.subjects;

import dev.vrba.bomberman.discord.modules.BombermanModule;
import dev.vrba.bomberman.discord.modules.subjects.commands.ModifySubjectRolesCommand;
import net.dv8tion.jda.api.JDA;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubjectsBombermanModule extends BombermanModule {

    private final ModifySubjectRolesCommand modifySubjectRolesCommand;

    @Autowired
    public SubjectsBombermanModule(ModifySubjectRolesCommand modifySubjectRolesCommand) {
        this.modifySubjectRolesCommand = modifySubjectRolesCommand;
    }

    @Override
    public void register(@NotNull JDA api) {
        this.registerCommands(
                api,
                modifySubjectRolesCommand
        );
    }
}
