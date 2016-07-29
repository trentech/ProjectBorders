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
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
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
		if (!args.hasAny("world")) {
			src.sendMessage(invalidArg());
			return CommandResult.empty();
		}
		AtomicReference<String> worldName = new AtomicReference<>(args.<String> getOne("world").get());

		if (worldName.get().equalsIgnoreCase("@w") && src instanceof Player) {
			worldName.set(((Player) src).getWorld().getName());
		}

		if (args.hasAny("s")) {
			if (!list.containsKey(worldName.get())) {
				src.sendMessage(Text.of(TextColors.YELLOW, "Pre-Generator not running for this world"));
				return CommandResult.empty();
			}
			list.get(worldName.get()).cancel();
			list.remove(worldName.get());

			src.sendMessage(Text.of(TextColors.DARK_GREEN, "Pre-Generator stopped for ", worldName.get()));
			return CommandResult.success();
		}

		if (list.containsKey(worldName.get())) {
			if (Sponge.getScheduler().getScheduledTasks(Main.getPlugin()).contains(list.get(worldName.get()))) {
				src.sendMessage(Text.of(TextColors.YELLOW, "Pre-Generator already running for this world"));
				return CommandResult.empty();
			}
			list.remove(worldName.get());
		}

		Optional<WorldProperties> optionalProperties = Sponge.getServer().getWorldProperties(worldName.get());

		if (!optionalProperties.isPresent()) {
			src.sendMessage(Text.of(TextColors.DARK_RED, worldName.get(), " does not exist"));
			return CommandResult.empty();
		}
		WorldProperties properties = optionalProperties.get();

		Optional<World> optionalWorld = Sponge.getServer().getWorld(properties.getUniqueId());

		if (!optionalWorld.isPresent()) {
			src.sendMessage(Text.of(TextColors.DARK_RED, worldName.get(), " must be loaded"));
			return CommandResult.empty();
		}
		World world = optionalWorld.get();

		WorldBorder border = world.getWorldBorder();

		ChunkPreGenerate generator = border.newChunkPreGenerate(world).owner(Main.getPlugin());
		generator.logger(Main.getLog());

		if (args.hasAny("tickInterval")) {
			int tickInterval;
			try {
				tickInterval = Integer.parseInt(args.<String> getOne("tickInterval").get());
			} catch (Exception e) {
				src.sendMessage(Text.of(TextColors.DARK_RED, "Not a valid number"));
				src.sendMessage(invalidArg());
				return CommandResult.empty();
			}
			generator.tickInterval(tickInterval);
		}

		if (args.hasAny("chunkCount")) {
			int chunkCount;
			try {
				chunkCount = Integer.parseInt(args.<String> getOne("chunkCount").get());
			} catch (Exception e) {
				src.sendMessage(Text.of(TextColors.DARK_RED, "Not a valid number"));
				src.sendMessage(invalidArg());
				return CommandResult.empty();
			}
			generator.chunksPerTick(chunkCount);
		}

		if (args.hasAny("tickPercent")) {
			float tickPercent;
			try {
				tickPercent = Float.parseFloat(args.<String> getOne("tickPercent").get());
			} catch (Exception e) {
				src.sendMessage(Text.of(TextColors.DARK_RED, "Not a valid number"));
				src.sendMessage(invalidArg());
				return CommandResult.empty();
			}
			generator.tickPercentLimit(tickPercent);
		}

		Task task = generator.start();

		list.put(worldName.get(), task);

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Pre-Generator starting for ", worldName.get()));
		src.sendMessage(Text.of(TextColors.GOLD, "This can cause significant lag while running"));

		status(src, task);

		return CommandResult.success();
	}

	private Text invalidArg() {
		Text t1 = Text.of(TextColors.YELLOW, "/border generate ");
		Text t2 = Text.builder().color(TextColors.YELLOW).onHover(TextActions.showText(Text.of("Enter world name"))).append(Text.of("<world> ")).build();
		Text t3 = Text.builder().color(TextColors.YELLOW).onHover(TextActions.showText(Text.of("To cancel generator task"))).append(Text.of("[-s] ")).build();
		Text t4 = Text.builder().color(TextColors.YELLOW).onHover(TextActions.showText(Text.of("Sets the interval between generation runs. Default is 10"))).append(Text.of("[-i <tickInterval>] ")).build();
		Text t5 = Text.builder().color(TextColors.YELLOW).onHover(TextActions.showText(Text.of("Sets the limit of tick time that can be used to generate chunks as a percentage of tickInterval. The percentage should be in the range 0-1"))).append(Text.of("[-p <tickPercent>] ")).build();
		Text t6 = Text.builder().color(TextColors.YELLOW).onHover(TextActions.showText(Text.of("Sets maximum number of chunks per tick to generate"))).append(Text.of("[-c <chunkCount>]")).build();
		return Text.of(t1, t2, t3, t4, t5, t6);
	}

	private void status(CommandSource src, Task task) {
		Sponge.getScheduler().createTaskBuilder().delayTicks(100).execute(c -> {
			if (!Sponge.getScheduler().getScheduledTasks(Main.getPlugin()).contains(task)) {
				src.sendMessage(Text.of(TextColors.DARK_GREEN, "Pre-Generator finished"));
			} else {
				status(src, task);
			}
		}).submit(Main.getPlugin());
	}
}
