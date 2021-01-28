package dev.vrba.bomberman.discord.modules.subjects;

import dev.vrba.bomberman.discord.modules.BombermanModule;
import dev.vrba.bomberman.discord.modules.subjects.commands.AddSubjectCommand;
import dev.vrba.bomberman.discord.modules.subjects.commands.ModifySubjectRolesCommand;
import net.dv8tion.jda.api.JDA;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubjectsBombermanModule extends BombermanModule {

    private final AddSubjectCommand addSubjectCommand;

    private final ModifySubjectRolesCommand modifySubjectRolesCommand;

    @Autowired
    public SubjectsBombermanModule(
           @NotNull final AddSubjectCommand addSubjectCommand,
           @NotNull final ModifySubjectRolesCommand modifySubjectRolesCommand
    ) {
        this.addSubjectCommand = addSubjectCommand;
        this.modifySubjectRolesCommand = modifySubjectRolesCommand;
    }

    @Override
    public void register(@NotNull JDA api) {
        this.registerCommands(
                api,
                addSubjectCommand,
                modifySubjectRolesCommand
        );
    }
}
