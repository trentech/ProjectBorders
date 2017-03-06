package com.gmail.trentech.pjb.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.storage.WorldProperties;

import com.flowpowered.math.vector.Vector3d;
import com.gmail.trentech.pjc.help.Help;

public class CMDInfo implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (!args.hasAny("world")) {
			Help help = Help.get("border info").get();
			throw new CommandException(Text.builder().onClick(TextActions.executeCallback(help.execute())).append(help.getUsageText()).build(), false);
		}
		WorldProperties properties = args.<WorldProperties> getOne("world").get();

		Optional<World> optionalWorld = Sponge.getServer().getWorld(properties.getUniqueId());

		if (!optionalWorld.isPresent()) {
			throw new CommandException(Text.of(TextColors.RED, properties.getWorldName(), " must be loaded"), false);
		}
		World world = optionalWorld.get();

		WorldBorder border = world.getWorldBorder();
		
		List<Text> list = new ArrayList<>();
		
		list.add(Text.of(TextColors.GREEN, "World: ", TextColors.WHITE, world.getName()));
		
		Vector3d center = border.getCenter();
		
		list.add(Text.of(TextColors.GREEN, "Center:"));
		list.add(Text.of(TextColors.GREEN, "  X: ", TextColors.WHITE, center.getFloorX()));
		list.add(Text.of(TextColors.GREEN, "  Y: ", TextColors.WHITE, center.getFloorY()));
		list.add(Text.of(TextColors.GREEN, "  Z: ", TextColors.WHITE, center.getFloorZ()));
		list.add(Text.of(TextColors.GREEN, "Diameter: ", TextColors.WHITE, border.getDiameter()));
		
		if(border.getDiameter() != border.getNewDiameter()) {
			list.add(Text.of(TextColors.GREEN, "New Diameter: ", TextColors.WHITE, border.getNewDiameter()));
		}
		if(border.getTimeRemaining() != 0) {
			list.add(Text.of(TextColors.GREEN, "Time Remaining: ", TextColors.WHITE, border.getTimeRemaining()));
		}
		
		list.add(Text.of(TextColors.GREEN, "Warning Distance: ", TextColors.WHITE, border.getWarningDistance()));
		list.add(Text.of(TextColors.GREEN, "Warning Time: ", TextColors.WHITE, border.getWarningTime()));
		list.add(Text.of(TextColors.GREEN, "Damage Amount: ", TextColors.WHITE, border.getDamageAmount()));
		list.add(Text.of(TextColors.GREEN, "Damage Threshold: ", TextColors.WHITE, border.getDamageThreshold()));
		
		if (src instanceof Player) {
			PaginationList.Builder pages = PaginationList.builder();

			pages.title(Text.builder().color(TextColors.DARK_GREEN).append(Text.of(TextColors.GREEN, "Border")).build());

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
