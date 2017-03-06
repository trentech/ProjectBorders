package com.gmail.trentech.pjb.commands;

import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.storage.WorldProperties;

import com.gmail.trentech.pjc.help.Help;

public class CMDDiameter implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (!args.hasAny("world")) {
			Help help = Help.get("border diameter").get();
			throw new CommandException(Text.builder().onClick(TextActions.executeCallback(help.execute())).append(help.getUsageText()).build(), false);
		}
		WorldProperties properties = args.<WorldProperties> getOne("world").get();

		Optional<World> optionalWorld = Sponge.getServer().getWorld(properties.getUniqueId());

		if (!optionalWorld.isPresent()) {
			throw new CommandException(Text.of(TextColors.RED, properties.getWorldName(), " must be loaded"), false);
		}
		World world = optionalWorld.get();

		WorldBorder border = world.getWorldBorder();

		if (!args.hasAny("startDiameter")) {
			Help help = Help.get("border diameter").get();
			throw new CommandException(Text.builder().onClick(TextActions.executeCallback(help.execute())).append(help.getUsageText()).build(), false);
		}
		double startDiameter = args.<Double> getOne("startDiameter").get();

		long time = 0;
		double endDiameter = 0;
		if (args.hasAny("time")) {
			time = args.<Long> getOne("time").get();

			if (args.hasAny("endDiameter")) {
				endDiameter = args.<Double> getOne("endDiameter").get();
			}
		}

		if (time != 0) {
			if (endDiameter != 0) {
				border.setDiameter(startDiameter, endDiameter, time);
				src.sendMessage(Text.of(TextColors.DARK_GREEN, "Set diameter of ", world.getName(), " to start: ", startDiameter, "end: ", endDiameter, " time: ", time));
			} else {
				border.setDiameter(startDiameter, time);
				src.sendMessage(Text.of(TextColors.DARK_GREEN, "Set diameter of ", world.getName(), " to start: ", startDiameter, " time: ", time));
			}
		} else {
			border.setDiameter(startDiameter);
			src.sendMessage(Text.of(TextColors.DARK_GREEN, "Set diameter of ", world.getName(), " to ", startDiameter));
		}

		return CommandResult.success();
	}
}
