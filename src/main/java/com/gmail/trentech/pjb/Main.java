package com.gmail.trentech.pjb;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import com.gmail.trentech.pjb.commands.CommandManager;
import com.gmail.trentech.pjb.utils.ConfigManager;
import com.gmail.trentech.pjb.utils.Resource;

import me.flibio.updatifier.Updatifier;

@Updatifier(repoName = "ProjectWorlds", repoOwner = "TrenTech", version = Resource.VERSION)
@Plugin(id = Resource.ID, name = Resource.NAME, authors = Resource.AUTHOR, url = Resource.URL, dependencies = {@Dependency(id = "Updatifier", optional = true)})
public class Main {

	private static Game game;
	private static Logger log;	
	private static PluginContainer plugin;

	@Listener
    public void onPreInitialization(GamePreInitializationEvent event) {
		game = Sponge.getGame();
		plugin = getGame().getPluginManager().getPlugin(Resource.ID).get();
		log = getPlugin().getLogger();
    }
	
    @Listener
    public void onInitialization(GameInitializationEvent event) {   	
    	getGame().getCommandManager().register(this, new CommandManager().cmdBorder, "border", "b");
    	
    	new ConfigManager().init();
    }

    public static Logger getLog() {
        return log;
    }
    
	public static Game getGame() {
		return game;
	}

	public static PluginContainer getPlugin() {
		return plugin;
	}
}