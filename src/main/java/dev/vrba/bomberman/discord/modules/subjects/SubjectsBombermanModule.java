package dev.vrba.bomberman.discord.modules.subjects;

import dev.vrba.bomberman.discord.modules.BombermanModule;
import dev.vrba.bomberman.discord.modules.subjects.commands.AddSubjectCommand;
import dev.vrba.bomberman.discord.modules.subjects.commands.ModifySubjectRolesCommand;
import dev.vrba.bomberman.discord.modules.subjects.commands.RemoveSubjectCommand;
import dev.vrba.bomberman.discord.modules.subjects.commands.ScaffoldSubjectCommand;
import net.dv8tion.jda.api.JDA;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubjectsBombermanModule extends BombermanModule {

    private final AddSubjectCommand addSubjectCommand;
    private final RemoveSubjectCommand removeSubjectCommand;
    private final ModifySubjectRolesCommand modifySubjectRolesCommand;
    private final ScaffoldSubjectCommand scaffoldSubjectCommand;

    @Autowired
    public SubjectsBombermanModule(
           AddSubjectCommand addSubjectCommand,
           RemoveSubjectCommand removeSubjectCommand,
           ModifySubjectRolesCommand modifySubjectRolesCommand,
           ScaffoldSubjectCommand scaffoldSubjectCommand
    ) {
        this.addSubjectCommand = addSubjectCommand;
        this.removeSubjectCommand = removeSubjectCommand;
        this.modifySubjectRolesCommand = modifySubjectRolesCommand;
        this.scaffoldSubjectCommand = scaffoldSubjectCommand;
    }

    @Override
    public void register(@NotNull JDA api) {
        this.registerCommands(
                api,
                addSubjectCommand,
                removeSubjectCommand,
                modifySubjectRolesCommand,
                scaffoldSubjectCommand
        );
    }
}
