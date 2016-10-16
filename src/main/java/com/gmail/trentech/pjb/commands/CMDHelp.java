package com.gmail.trentech.pjb.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

import com.gmail.trentech.pjb.utils.Help;

public class CMDHelp implements CommandExecutor {

	public CMDHelp() {
		new Help("border help", "help", "Get help with all commands in Project Borders", false)
			.setPermission("pjw.pjb.border")
			.setUsage("/border help <rawCommand>")
			.setExample("/border help world create")
			.save();
	}
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Help help = args.<Help> getOne("command").get();
		help.execute(src);
		
		return CommandResult.success();
	}
}
