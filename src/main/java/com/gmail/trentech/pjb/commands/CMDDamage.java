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

public class CMDDamage implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		WorldProperties properties = args.<WorldProperties> getOne("world").get();

		Optional<World> optionalWorld = Sponge.getServer().getWorld(properties.getUniqueId());

		if (!optionalWorld.isPresent()) {
			throw new CommandException(Text.of(TextColors.DARK_RED, properties.getWorldName(), " must be loaded"), false);
		}
		World world = optionalWorld.get();

		WorldBorder border = world.getWorldBorder();

		double distance = args.<Double> getOne("distance").get();
		double damage = args.<Double> getOne("damage").get();

		border.setDamageThreshold(distance);

		if (damage != 0) {
			border.setDamageAmount(damage);
			src.sendMessage(Text.of(TextColors.DARK_GREEN, "Set border damage of ", world.getName(), " to distance: ", distance, " amount: ", damage));
		} else {
			src.sendMessage(Text.of(TextColors.DARK_GREEN, "Set border damage amount of ", world.getName(), " to ", distance));
		}

		return CommandResult.success();
	}
}
