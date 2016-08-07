package me.theeninja.bungeeteleport.yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.theeninja.bungeeteleport.BungeeTeleport;

public class ConfigurationHandler {
	private FileConfiguration fileConfiguration = null;
	private static File file = null;
	public void reloadConfiguration() {
		if (file == null) {
			file = new File(BungeeTeleport.getInstance().getDataFolder(), "config.yml");
		}
		fileConfiguration = YamlConfiguration.loadConfiguration(file);

		Reader defaultStream = null;
		try {
			defaultStream = new InputStreamReader(BungeeTeleport.getInstance().getResource("config.yml"), "UTF8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (defaultStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defaultStream);
			fileConfiguration.setDefaults(defConfig);
		}
	}
	public FileConfiguration getConfig() {
		if (fileConfiguration == null) {
			reloadConfiguration();
		}
		return fileConfiguration;
	}
	public void saveConfig() {
		if (fileConfiguration == null || file == null) {
			return;
		}
		try {
			getConfig().save(file);
		} catch (IOException exception) {
			BungeeTeleport.getInstance().getLogger().log(Level.SEVERE, "Configuration could not be saved to " + file, exception);
		}
	}
	public static void saveDefaultConfig() {
		if (file == null) {
			file = new File(BungeeTeleport.getInstance().getDataFolder(), "config.yml");
		}
		if (!file.exists()) {            
			BungeeTeleport.getInstance().saveResource("config.yml", false);
		}
	}
}
