package com.gmail.trentech.pjb.commands;

import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.storage.WorldProperties;

import com.gmail.trentech.pjb.utils.Help;

public class CMDCenter implements CommandExecutor {

	public CMDCenter() {
		Help help = new Help("center", "center", " Set the center coordinates of border");
		help.setSyntax(" /border center <world> <x> <z>\n /b d <world> <x> <z>");
		help.setExample(" /border center MyWorld 100 -250");
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

		Optional<WorldProperties> optionalProperties = Sponge.getServer().getWorldProperties(worldName);

		if (!optionalProperties.isPresent()) {
			src.sendMessage(Text.of(TextColors.DARK_RED, worldName, " does not exist"));
			return CommandResult.empty();
		}
		WorldProperties properties = optionalProperties.get();

		Optional<World> optionalWorld = Sponge.getServer().getWorld(properties.getUniqueId());

		if (!optionalWorld.isPresent()) {
			src.sendMessage(Text.of(TextColors.DARK_RED, worldName, " must be loaded"));
			return CommandResult.empty();
		}
		World world = optionalWorld.get();

		WorldBorder border = world.getWorldBorder();

		if (!args.hasAny("x")) {
			src.sendMessage(invalidArg());
			return CommandResult.empty();
		}
		String xArg = args.<String> getOne("x").get();

		double x;
		double z;

		if (xArg.equalsIgnoreCase("@p") && src instanceof Player) {
			Location<World> location = ((Player) src).getLocation();

			x = location.getX();
			z = location.getZ();
		} else {
			try {
				x = Double.parseDouble(args.<String> getOne("x").get());
			} catch (Exception e) {
				src.sendMessage(Text.of(TextColors.DARK_RED, "Not a valid number"));
				src.sendMessage(invalidArg());
				return CommandResult.empty();
			}

			if (!args.hasAny("z")) {
				src.sendMessage(invalidArg());
				return CommandResult.empty();
			}

			try {
				z = Double.parseDouble(args.<String> getOne("z").get());
			} catch (Exception e) {
				src.sendMessage(Text.of(TextColors.DARK_RED, "Not a valid number"));
				src.sendMessage(invalidArg());
				return CommandResult.empty();
			}
		}

		border.setCenter(x, z);

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Set center of ", worldName, " to x: ", x, " z: ", z));

		return CommandResult.success();
	}

	private Text invalidArg() {
		Text t1 = Text.of(TextColors.YELLOW, "/border center ");
		Text t2 = Text.builder().color(TextColors.YELLOW).onHover(TextActions.showText(Text.of("Enter world name"))).append(Text.of("<world> ")).build();
		Text t3 = Text.builder().color(TextColors.YELLOW).onHover(TextActions.showText(Text.of("Enter x coordinate"))).append(Text.of("<x> ")).build();
		Text t4 = Text.builder().color(TextColors.YELLOW).onHover(TextActions.showText(Text.of("Enter z coordinate"))).append(Text.of("<z>")).build();

		return Text.of(t1, t2, t3, t4);
	}
}
