package com.gmail.trentech.pjb;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import com.gmail.trentech.helpme.Help;
import com.gmail.trentech.pjb.commands.CommandManager;
import com.gmail.trentech.pjb.utils.Resource;
import com.google.inject.Inject;

import me.flibio.updatifier.Updatifier;

@Updatifier(repoName = Resource.NAME, repoOwner = Resource.AUTHOR, version = Resource.VERSION)
@Plugin(id = Resource.ID, name = Resource.NAME, version = Resource.VERSION, description = Resource.DESCRIPTION, authors = Resource.AUTHOR, url = Resource.URL, dependencies = { @Dependency(id = "Updatifier", optional = true), @Dependency(id = "helpme", optional = true) })
public class Main {

	@Inject
	private Logger log;

	private static  PluginContainer plugin;
	private static Main instance;
	
	@Listener
	public void onPreInitializationEvent(GamePreInitializationEvent event) {
		plugin = Sponge.getPluginManager().getPlugin(Resource.ID).get();
		instance = this;
	}
	
	@Listener
	public void onInitializationEvent(GameInitializationEvent event) {
		Sponge.getCommandManager().register(this, new CommandManager().cmdBorder, "border", "b");
		
		if (Sponge.getPluginManager().isLoaded("helpme")) {
			Help borderCenter = new Help("border center", "center", "Set the center coordinates of border")
					.setPermission("pjb.cmd.border.center")
					.addUsage("/border center <world> <x> <z>")
					.addUsage("/b d <world> <x> <z>")
					.addExample("/border center MyWorld 100 -250");
			
			Help borderDamage = new Help("border damage", "damage", "Set the damage threshold and amount damage player takes")
					.setPermission("pjb.cmd.border.damage")
					.addUsage("/border damage <world> <distance> [damage]")
					.addUsage("/b dmg <world> <distance> [damage]")
					.addExample("/border diameter MyWorld 50 2")
					.addExample("/border damage MyWorld 50");
			
			Help borderDiameter = new Help("border diameter", "diameter", "Set the diameter of border")
					.setPermission("pjb.cmd.border.diameter")
					.addUsage("/border diameter <world> <startDiameter> [<time> [endDiameter]]")
					.addUsage("/b d <world> <startDiameter> [<time> [endDiameter]]")
					.addExample("/border diameter MyWorld 5000 120 1000")
					.addExample("/border diameter MyWorld 5000 60")
					.addExample("/border diameter MyWorld 5000");
			
			Help borderGenerate = new Help("border generate", "generate", "Pre generate chunks inside border")
					.setPermission("pjb.cmd.border.generate")
					.addUsage("/border generate <world> [--stop] [--verbose] [-i <tickInverval>] [-p <tickPercent>] [-c <chunkCount>]")
					.addUsage("/b f <world> [--stop] [--verbose] [-i <tickInverval>] [-p <tickPercent>] [-c <chunkCount>]")
					.addExample("/border generate MyWorld --verbose -i 60 -p 0.4 -c 5")
					.addExample("/border generate MyWorld --stop")
					.addExample("/border generate MyWorld");
			
			Help borderInfo = new Help("border info", "info", "Show information of world border")
					.setPermission("pjb.cmd.border.info")
					.addUsage("/border info <world>")
					.addUsage("/b i <world>")
					.addExample("/border info MyWorld");
			
			Help borderWarning = new Help("border warning", "warning", "Set the center coordinates of border")
					.setPermission("pjb.cmd.border.warning")
					.addUsage("/border warning <world> <distance> [time]")
					.addUsage("/b w <world> <distance> [time]")
					.addExample("/border warning MyWorld 4900 10")
					.addExample("/border warning MyWorld 4900");
			
			Help border = new Help("border", "border", "Base Project Borders command")
					.setPermission("pji.cmd.border")
					.addChild(borderWarning)
					.addChild(borderInfo)
					.addChild(borderGenerate)
					.addChild(borderDiameter)
					.addChild(borderDamage)
					.addChild(borderCenter);
			
			Help.register(border);
		}
	}

	public Logger getLog() {
		return log;
	}

	public static PluginContainer getPlugin() {
		return plugin;
	}
	
	public static Main instance() {
		return instance;
	}
}