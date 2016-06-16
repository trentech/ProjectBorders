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

public class CMDWarning implements CommandExecutor {

	public CMDWarning() {
		Help help = new Help("warning", "warning", " Set the center coordinates of border");
		help.setSyntax(" /border warning <world> <distance> [time]\n /b w <world> <distance> [time]");
		help.setExample(" /border warning MyWorld 4900\n /border warning MyWorld 4900 10");
		help.save();
	}
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(!args.hasAny("world")) {
			src.sendMessage(invalidArg());
			return CommandResult.empty();
		}
		String worldName = args.<String>getOne("world").get();
		
		if(worldName.equalsIgnoreCase("@w") && src instanceof Player) {
			worldName = ((Player) src).getWorld().getName();
		}
		
		Optional<WorldProperties> optionalProperties = Main.getGame().getServer().getWorldProperties(worldName);
		
		if(!optionalProperties.isPresent()) {
			src.sendMessage(Text.of(TextColors.DARK_RED, worldName, " does not exist"));
			return CommandResult.empty();
		}
		WorldProperties properties = optionalProperties.get();
		
		Optional<World> optionalWorld = Main.getGame().getServer().getWorld(properties.getUniqueId());
		
		if(!optionalWorld.isPresent()) {
			src.sendMessage(Text.of(TextColors.DARK_RED, worldName, " must be loaded"));
			return CommandResult.empty();
		}
		World world = optionalWorld.get();
		
		WorldBorder border = world.getWorldBorder();
		
		if(!args.hasAny("distance")) {
			src.sendMessage(invalidArg());
			return CommandResult.empty();
		}
		
		int distance;
		try{
			distance = Integer.parseInt(args.<String>getOne("distance").get());
		}catch(Exception e) {
			src.sendMessage(Text.of(TextColors.DARK_RED, "Not a valid number"));
			src.sendMessage(invalidArg());
			return CommandResult.empty();
		}
		
		int time = 0;
		if(args.hasAny("time")) {
			try{
				time = Integer.parseInt(args.<String>getOne("time").get());
			}catch(Exception e) {
				src.sendMessage(Text.of(TextColors.DARK_RED, "Not a valid number"));
				src.sendMessage(invalidArg());
				return CommandResult.empty();
			}
		}

		border.setWarningDistance(distance);
		
		if(time != 0) {
			border.setWarningTime(time);
			src.sendMessage(Text.of(TextColors.DARK_GREEN, "Set warning distance of ", worldName, " to distance: ", distance, " time: ", time));
		} else {
			src.sendMessage(Text.of(TextColors.DARK_GREEN, "Set warning distance of ", worldName, " to ", distance));
		}

		return CommandResult.success();
	}
	
	private Text invalidArg() {
		Text t1 = Text.of(TextColors.YELLOW, "/border border ");
		Text t2 = Text.builder().color(TextColors.YELLOW).onHover(TextActions.showText(Text.of("Enter world name"))).append(Text.of("<world> ")).build();
		Text t3 = Text.builder().color(TextColors.YELLOW).onHover(TextActions.showText(Text.of("Enter warning distance"))).append(Text.of("<distance> ")).build();
		Text t4 = Text.builder().color(TextColors.YELLOW).onHover(TextActions.showText(Text.of("Enter warning time contracting border will reach player"))).append(Text.of("[time]")).build();
		return Text.of(t1,t2,t3,t4);
	}
}
