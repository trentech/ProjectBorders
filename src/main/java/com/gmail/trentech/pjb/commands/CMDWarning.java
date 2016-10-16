package com.gmail.trentech.pjb.commands;

import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.storage.WorldProperties;

import com.gmail.trentech.pjb.utils.Help;

public class CMDWarning implements CommandExecutor {

	public CMDWarning() {
		new Help("border warning", "warning", "Set the center coordinates of border", false)
			.setPermission("pjb.cmd.border.warning")
			.setUsage("/border warning <world> <distance> [time]\n /b w <world> <distance> [time]")
			.setExample("/border warning MyWorld 4900\n /border warning MyWorld 4900 10")
			.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		WorldProperties properties = args.<WorldProperties> getOne("world").get();

		Optional<World> optionalWorld = Sponge.getServer().getWorld(properties.getUniqueId());

		if (!optionalWorld.isPresent()) {
			throw new CommandException(Text.of(TextColors.RED, properties.getWorldName(), " must be loaded"), false);
		}
		World world = optionalWorld.get();

		WorldBorder border = world.getWorldBorder();

		int distance = args.<Integer> getOne("distance").get();

		int time = 0;
		if (args.hasAny("time")) {
			time = args.<Integer> getOne("time").get();
		}

		border.setWarningDistance(distance);

		if (time != 0) {
			border.setWarningTime(time);
			src.sendMessage(Text.of(TextColors.DARK_GREEN, "Set warning distance of ", world.getName(), " to distance: ", distance, " time: ", time));
		} else {
			src.sendMessage(Text.of(TextColors.DARK_GREEN, "Set warning distance of ", world.getName(), " to ", distance));
		}

		return CommandResult.success();
	}
}
