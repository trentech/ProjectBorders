package com.gmail.trentech.pjb.commands;

import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class CommandManager {

	private CommandSpec cmdCenter = CommandSpec.builder()
		    .description(Text.of(" Set the center coordinates of border"))
		    .permission("pjb.cmd.border.center")
		    .arguments(GenericArguments.optional(GenericArguments.world(Text.of("world"))), GenericArguments.optional(GenericArguments.seq(GenericArguments.doubleNum(Text.of("x")), GenericArguments.doubleNum(Text.of("z")))))
		    .executor(new CMDCenter())
		    .build();
	
	private CommandSpec cmdDamage = CommandSpec.builder()
		    .description(Text.of(" Set damage threhold and amount"))
		    .permission("pjb.cmd.border.damage")
		    .arguments(GenericArguments.optional(GenericArguments.world(Text.of("world"))), GenericArguments.optional(GenericArguments.doubleNum(Text.of("distance"))), GenericArguments.optional(GenericArguments.doubleNum(Text.of("damage"))))
		    .executor(new CMDDamage())
		    .build();
	
	private CommandSpec cmdDiameter = CommandSpec.builder()
		    .description(Text.of(" Set the diameter of the border"))
		    .permission("pjb.cmd.border.diameter")
		    .arguments(GenericArguments.optional(GenericArguments.world(Text.of("world"))), GenericArguments.optional(GenericArguments.doubleNum(Text.of("startDiameter"))),
		    		GenericArguments.optional(GenericArguments.seq(GenericArguments.longNum(Text.of("time")), GenericArguments.optional(GenericArguments.doubleNum(Text.of("endDiameter"))))))
		    .executor(new CMDDiameter())
		    .build();
	
	private CommandSpec cmdGenerate = CommandSpec.builder()
		    .description(Text.of(" Pre-generate chunks within border"))
		    .permission("pjb.cmd.border.generate")
		    .arguments(GenericArguments.optional(GenericArguments.world(Text.of("world"))), GenericArguments.flags().flag("stop", "verbose").setAcceptsArbitraryLongFlags(true)
		    		.valueFlag(GenericArguments.integer(Text.of("tickInterval")), "i")
		    		.valueFlag(GenericArguments.integer(Text.of("tickPercent")), "p")
		    		.valueFlag(GenericArguments.integer(Text.of("chunkCount")), "c").buildWith(GenericArguments.none()))
		    .executor(new CMDGenerate())
		    .build();
	
	private CommandSpec cmdWarning = CommandSpec.builder()
		    .description(Text.of(" Set warning values of border"))
		    .permission("pjb.cmd.border.warning")
		    .arguments(GenericArguments.optional(GenericArguments.world(Text.of("world"))), GenericArguments.optional(GenericArguments.integer(Text.of("distance"))),
		    		GenericArguments.optional(GenericArguments.integer(Text.of("time"))))
		    .executor(new CMDWarning())
		    .build();
	
	private CommandSpec cmdInfo = CommandSpec.builder()
		    .description(Text.of(" Shows border information"))
		    .permission("pjb.cmd.border.info")
		    .arguments(GenericArguments.optional(GenericArguments.world(Text.of("world"))))
		    .executor(new CMDInfo())
		    .build();

	public CommandSpec cmdBorder = CommandSpec.builder()
		    .description(Text.of(" Simple world border management"))
		    .permission("pjb.cmd.border")
		    .child(cmdCenter, "center", "c")
		    .child(cmdDamage, "damage", "dg")
		    .child(cmdDiameter, "diameter", "d")
		    .child(cmdGenerate, "generate", "g")
		    .child(cmdWarning, "warning", "w")
		    .child(cmdInfo, "info", "i")
		    .executor(new CMDBorder())
		    .build();
}
