package com.gmail.trentech.pjb.commands;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.WorldBorder.ChunkPreGenerate;
import org.spongepowered.api.world.storage.WorldProperties;

import com.gmail.trentech.pjb.Main;
import com.gmail.trentech.pjb.utils.Help;

public class CMDGenerate implements CommandExecutor {

	private static HashMap<String, Task> list = new HashMap<>();

	public CMDGenerate() {
		Help help = new Help("generate", "generate", " Pre generate chunks inside border");
		help.setSyntax(" /border generate <world> [-s] [-i <tickInverval>] [-p <tickPercent>] [-c <chunkCount>]\n /b f <world> [-s] [-i <tickInverval>] [-p <tickPercent>] [-c <chunkCount>]");
		help.setExample(" /border generate MyWorld\n /border generate MyWorld -s\n /border generate MyWorld -i 60 -p 0.4 -c 5");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		WorldProperties properties = args.<WorldProperties> getOne("world").get();

		Optional<World> optionalWorld = Sponge.getServer().getWorld(properties.getUniqueId());

		if (!optionalWorld.isPresent()) {
			throw new CommandException(Text.of(TextColors.RED, properties.getWorldName(), " must be loaded"));
		}
		World world = optionalWorld.get();
		String worldName = world.getName();

		if (args.hasAny("s")) {
			if (!list.containsKey(worldName)) {
				throw new CommandException(Text.of(TextColors.YELLOW, "Pre-Generator not running for this world"));
			}
			list.get(worldName).cancel();
			list.remove(worldName);

			src.sendMessage(Text.of(TextColors.DARK_GREEN, "Pre-Generator stopped for ", worldName));
			return CommandResult.success();
		}

		if (list.containsKey(worldName)) {
			if (Sponge.getScheduler().getScheduledTasks(Main.getPlugin()).contains(list.get(worldName))) {
				throw new CommandException(Text.of(TextColors.YELLOW, "Pre-Generator already running for this world"));
			}
			list.remove(worldName);
		}

		WorldBorder border = world.getWorldBorder();

		ChunkPreGenerate generator = border.newChunkPreGenerate(world).owner(Main.getPlugin());
		generator.logger(Main.instance().getLog());

		if (args.hasAny("tickInterval")) {
			generator.tickInterval(args.<Integer> getOne("tickInterval").get());
		}

		if (args.hasAny("chunkCount")) {
			generator.chunksPerTick(args.<Integer> getOne("chunkCount").get());
		}

		if (args.hasAny("tickPercent")) {
			generator.tickPercentLimit(args.<Float> getOne("tickPercent").get());
		}

		Task task = generator.start();

		list.put(worldName, task);

		Sponge.getServer().getBroadcastChannel().send(Text.of(TextColors.DARK_GREEN, "Pre-Generator starting for ", worldName));
		Sponge.getServer().getBroadcastChannel().send(Text.of(TextColors.GOLD, "This can cause significant lag while running"));
		
		status(task, worldName);

		return CommandResult.success();
	}
	
	private AtomicReference<Integer> time = new AtomicReference<Integer>(0);
	
	private void status(Task task, String worldName) {
		Sponge.getScheduler().createTaskBuilder().delayTicks(100).execute(c -> {
			if (!Sponge.getScheduler().getScheduledTasks(Main.getPlugin()).contains(task)) {
				Sponge.getServer().getBroadcastChannel().send(Text.of(TextColors.DARK_GREEN, "Pre-Generator finished for ", worldName));
			} else {
				if(time.get() == 60) {
					Sponge.getServer().getBroadcastChannel().send(Text.of(TextColors.DARK_GREEN, "Pre-Generator is running for ", worldName));
					Sponge.getServer().getBroadcastChannel().send(Text.of(TextColors.GOLD, "This can cause significant lag while running"));
					time.set(0);
				} else {
					time.set(time.get() + 5);
				}
				status(task, worldName);
			}			
		}).submit(Main.getPlugin());
	}
}
