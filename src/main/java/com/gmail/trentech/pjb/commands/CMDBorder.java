package com.gmail.trentech.pjb.commands;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.helpme.Help;

public class CMDBorder implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (Sponge.getPluginManager().isLoaded("helpme")) {
			Help.executeList(src, Help.get("border").get().getChildren());
			
			return CommandResult.success();
		}
		
		List<Text> list = new ArrayList<>();

		if (src.hasPermission("pjb.cmd.border.center")) {
			list.add(Text.builder().color(TextColors.GREEN).onClick(TextActions.runCommand("/pjb:border center")).append(Text.of(" /border center")).build());
		}
		if (src.hasPermission("pjb.cmd.border.damage")) {
			list.add(Text.builder().color(TextColors.GREEN).onClick(TextActions.runCommand("/pjb:border damage")).append(Text.of(" /border damage")).build());
		}
		if (src.hasPermission("pjb.cmd.border.diameter")) {
			list.add(Text.builder().color(TextColors.GREEN).onClick(TextActions.runCommand("/pjb:border diameter")).append(Text.of(" /border diameter")).build());
		}
		if (src.hasPermission("pjb.cmd.border.generate")) {
			list.add(Text.builder().color(TextColors.GREEN).onClick(TextActions.runCommand("/pjb:border generate")).append(Text.of(" /border generate")).build());
		}
		if (src.hasPermission("pjb.cmd.border.info")) {
			list.add(Text.builder().color(TextColors.GREEN).onClick(TextActions.runCommand("/pjb:border info")).append(Text.of(" /border info")).build());
		}
		if (src.hasPermission("pjb.cmd.border.warning")) {
			list.add(Text.builder().color(TextColors.GREEN).onClick(TextActions.runCommand("/pjb:border warning")).append(Text.of(" /border warning")).build());
		}

		if (src instanceof Player) {
			PaginationList.Builder pages = PaginationList.builder();

			pages.title(Text.builder().color(TextColors.DARK_GREEN).append(Text.of(TextColors.GREEN, "Command List")).build());

			pages.contents(list);

			pages.sendTo(src);
		} else {
			for (Text text : list) {
				src.sendMessage(text);
			}
		}

		return CommandResult.success();
	}

}
