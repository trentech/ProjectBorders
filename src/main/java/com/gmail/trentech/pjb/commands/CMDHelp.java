package com.gmail.trentech.pjb.commands;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.pjb.utils.Help;

public class CMDHelp implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		String command = args.<String> getOne("command").get();

		for (Help help : Help.getAll()) {
			if (help.getCommand().equalsIgnoreCase(command)) {
				List<Text> list = new ArrayList<>();

				list.add(Text.of(TextColors.GREEN, "Description:"));
				list.add(Text.of(TextColors.WHITE, help.getDescription()));

				if (help.getSyntax().isPresent()) {
					list.add(Text.of(TextColors.GREEN, "Syntax:"));
					list.add(Text.of(TextColors.WHITE, help.getSyntax().get()));
				}
				if (help.getExample().isPresent()) {
					list.add(Text.of(TextColors.GREEN, "Example:"));
					list.add(Text.of(TextColors.WHITE, help.getExample().get(), TextColors.DARK_GREEN));
				}

				if (src instanceof Player) {
					PaginationList.Builder pages = PaginationList.builder();

					pages.title(Text.builder().color(TextColors.DARK_GREEN).append(Text.of(TextColors.GREEN, command.toLowerCase())).build());

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

		src.sendMessage(Text.of(TextColors.DARK_RED, command, " is not a valid command"));
		return CommandResult.empty();
	}
}
