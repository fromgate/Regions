package ru.nukkit.regions.commands;


import cn.nukkit.permission.Permission;
import ru.nukkit.regions.util.Message;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CmdDefine {
    String command();

    String alias() default "";

    String[] subCommands();

    String permission();

    String defaultPerm() default Permission.DEFAULT_OP;

    boolean allowConsole() default false;

    Message description();
}
