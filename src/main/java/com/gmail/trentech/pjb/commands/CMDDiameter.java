package com.gmail.trentech.pjb.commands;

import java.util.Optional;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.storage.WorldProperties;

import com.gmail.trentech.pjb.Main;
import com.gmail.trentech.pjb.utils.Help;

public class CMDDiameter implements CommandExecutor {

	public CMDDiameter() {
		Help help = new Help("diameter", "diameter", " Set the diameter of border");
		help.setSyntax(" /border diameter <world> <startDiameter> [<time> [endDiameter]]\n /b d <world> <startDiameter> [<time> [endDiameter]]");
		help.setExample(" /border diameter MyWorld 5000\n /border diameter MyWorld 5000 60\n /border diameter MyWorld 5000 120 1000");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (!args.hasAny("world")) {
			src.sendMessage(invalidArg());
			return CommandResult.empty();
		}
		String worldName = args.<String> getOne("world").get();

		if (worldName.equalsIgnoreCase("@w") && src instanceof Player) {
			worldName = ((Player) src).getWorld().getName();
		}

		Optional<WorldProperties> optionalProperties = Main.getGame().getServer().getWorldProperties(worldName);

		if (!optionalProperties.isPresent()) {
			src.sendMessage(Text.of(TextColors.DARK_RED, worldName, " does not exist"));
			return CommandResult.empty();
		}
		WorldProperties properties = optionalProperties.get();

		Optional<World> optionalWorld = Main.getGame().getServer().getWorld(properties.getUniqueId());

		if (!optionalWorld.isPresent()) {
			src.sendMessage(Text.of(TextColors.DARK_RED, worldName, " must be loaded"));
			return CommandResult.empty();
		}
		World world = optionalWorld.get();

		WorldBorder border = world.getWorldBorder();

		if (!args.hasAny("startDiameter")) {
			src.sendMessage(invalidArg());
			return CommandResult.empty();
		}

		double startDiameter;
		try {
			startDiameter = Double.parseDouble(args.<String> getOne("startDiameter").get());
		} catch (Exception e) {
			src.sendMessage(Text.of(TextColors.DARK_RED, "Not a valid number"));
			src.sendMessage(invalidArg());
			return CommandResult.empty();
		}

		long time = 0;
		double endDiameter = 0;
		if (args.hasAny("time")) {
			try {
				time = Long.parseLong(args.<String> getOne("time").get()) * 1000;
			} catch (Exception e) {
				src.sendMessage(Text.of(TextColors.DARK_RED, "Not a valid number"));
				src.sendMessage(invalidArg());
				return CommandResult.empty();
			}

			if (args.hasAny("endDiameter")) {
				try {
					endDiameter = Double.parseDouble(args.<String> getOne("endDiameter").get());
				} catch (Exception e) {
					src.sendMessage(Text.of(TextColors.DARK_RED, "Not a valid number"));
					src.sendMessage(invalidArg());
					return CommandResult.empty();
				}
			}
		}

		if (time != 0) {
			if (endDiameter != 0) {
				border.setDiameter(startDiameter, endDiameter, time);
				src.sendMessage(Text.of(TextColors.DARK_GREEN, "Set diameter of ", worldName, " to start: ", startDiameter, "end: ", endDiameter, " time: ", time));
			} else {
				border.setDiameter(startDiameter, time);
				src.sendMessage(Text.of(TextColors.DARK_GREEN, "Set diameter of ", worldName, " to start: ", startDiameter, " time: ", time));
			}
		} else {
			border.setDiameter(startDiameter);
			src.sendMessage(Text.of(TextColors.DARK_GREEN, "Set diameter of ", worldName, " to ", startDiameter));
		}

		return CommandResult.success();
	}

	private Text invalidArg() {
		Text t1 = Text.of(TextColors.YELLOW, "/border diameter ");
		Text t2 = Text.builder().color(TextColors.YELLOW).onHover(TextActions.showText(Text.of("Enter world name"))).append(Text.of("<world> ")).build();
		Text t3 = Text.builder().color(TextColors.YELLOW).onHover(TextActions.showText(Text.of("Enter start diameter"))).append(Text.of("<startDiameter> ")).build();
		Text t4 = Text.builder().color(TextColors.YELLOW).onHover(TextActions.showText(Text.of("Enter time in seconds it takes, to expand/contract border"))).append(Text.of("[<time> ")).build();
		Text t5 = Text.builder().color(TextColors.YELLOW).onHover(TextActions.showText(Text.of("Enter end diameter border will expand/contract to"))).append(Text.of("[endDiameter]]")).build();

		return Text.of(t1, t2, t3, t4, t5);
	}
}
