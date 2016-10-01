package me.theeninja.bungeeteleport.yaml;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.theeninja.bungeeteleport.yaml.ConfigurationValues.ConfigurationValue;

/**
 * Configuration class for managing individual 
 * write/read for individual configuration files.
 * 
 * @author TheeNinja
 *
 */
public class Configuration {
	
	private int comments;
	private ConfigurationManager manager;

	private File file;
	private FileConfiguration config;

	/**
	 * 
	 * @param configStream
	 * @param configFile
	 * @param comments
	 */
	public Configuration(InputStream configStream, File configFile, int comments) {
		
		Reader reader = null;
		
		try {
			reader = new InputStreamReader(configStream, "UTF-8");
		} 
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		this.comments = comments;
		this.manager = new ConfigurationManager();

		this.file = configFile;
		this.config = YamlConfiguration.loadConfiguration(reader);
	}

	/**
	 * 
	 * @param path
	 * @return value associated with path specified
	 */
	public Object get(String path) {
		
		return this.config.get(path);
	}
	
	/**
	 * 
	 * @param path
	 * @param def
	 * @return value associated with path specified
	 */
	public Object get(String path, Object def) {
		
		return this.config.get(path, def);
	}

	/**
	 * 
	 * @param path
	 * @return value (type String) associated with path specified
	 */
	public String getString(String path) {
		
		return this.config.getString(path);
	}

	/**
	 * 
	 * @param path
	 * @param def
	 * @return value (type String) associated with path specified
	 */
	public String getString(String path, String def) {
		return this.config.getString(path, def);
	}

	/**
	 * 
	 * @param path
	 * @return value (type int) associated with path specified
	 */
	public int getInt(String path) {
		return this.config.getInt(path);
	}
	
	/**
	 * 
	 * @param path
	 * @param def
	 * @return value (type int) associated with path specified
	 */
	public int getInt(String path, int def) {
		return this.config.getInt(path, def);
	}

	/**
	 * 
	 * @param path
	 * @return value (type boolean) associated with path specified
	 */
	public boolean getBoolean(String path) {
		return this.config.getBoolean(path);
	}

	/**
	 * 
	 * @param path
	 * @param def
	 * @return value (type boolean) associated with path specified
	 */
	public boolean getBoolean(String path, boolean def) {
		return this.config.getBoolean(path, def);
	}

	/**
	 * Creates a section within the specified path
	 * 
	 * @param path
	 */
	public void createSection(String path) {
		this.config.createSection(path);
	}
	
	/**
	 * Returns a section within the specified path
	 * 
	 * @param path
	 * @return configuration section specified within path
	 */
	public ConfigurationSection getConfigurationSection(String path) {
		return this.config.getConfigurationSection(path);
	}
	
	/**
	 * 
	 * @param path
	 * @return value (type double) associated with path specified
	 */
	public double getDouble(String path) {
		return this.config.getDouble(path);
	}

	/**
	 * 
	 * @param path
	 * @param def
	 * @return value (type double) associated with path specified
	 */
	public double getDouble(String path, double def) {
		return this.config.getDouble(path, def);
	}

	/**
	 * 
	 * @param path
	 * @return list of values associated with path specified
	 */
	public List<?> getList(String path) {
		return this.config.getList(path);
	}

	/**
	 * 
	 * @param path
	 * @param def
	 * @return list of values associated with path specified
	 */
	public List<?> getList(String path, List<?> def) {
		return this.config.getList(path, def);
	}

	
	/**
	 * 
	 * @param path
	 * @return list of values (type String) associated with path specified
	 */
	public List<String> getStringList(String path) {

		List<?> list = getList(path);

		if (list == null) {

			return new ArrayList<String>(0);
		}

		List<String> result = new ArrayList<String>();

		for (Object object : list) {

			if ((object instanceof String) || (isPrimitiveWrapper(object))) {

				result.add(String.valueOf(object));
			}
		}

		return result;
	}

	/**
	 * 
	 * @param path
	 * @return list of values (type int) associated with path specified
	 */
	public List<Integer> getIntegerList(String path) {

		List<?> list = getList(path);

		if (list == null) {

			return new ArrayList<Integer>(0);
		}

		List<Integer> result = new ArrayList<Integer>();

		for (Object object : list) {

			if (object instanceof Integer) {

				result.add((Integer) object);
			} 
			
			if (object instanceof String) {

				result.add(Integer.valueOf((String) object));
			}
			
			if (object instanceof Character) {

				result.add((int) ((Character) object).charValue());
			}
			
			if (object instanceof Number) {

				result.add(((Number) object).intValue());
			}
		}

		return result;
	}

	/**
	 * 
	 * @param path
	 * @return list of values (type boolean) associated with path specified
	 */
	public List<Boolean> getBooleanList(String path) {

		List<?> list = getList(path);

		if (list == null) {

			return new ArrayList<Boolean>(0);
		}

		List<Boolean> result = new ArrayList<Boolean>();

		for (Object object : list) {

			if (object instanceof Boolean) {

				result.add((Boolean) object);
			}
			
			if (object instanceof String) {

				if (Boolean.TRUE.toString().equals(object)) {

					result.add(true);
				}
				
				if (Boolean.FALSE.toString().equals(object)) {

					result.add(false);
				}
			}
		}

		return result;
	}

	/**
	 * 
	 * @param path
	 * @return list of values (double) associated with path specified
	 */
	public List<Double> getDoubleList(String path) {

		List<?> list = getList(path);


		if (list == null) {

			return new ArrayList<Double>(0);

		}

		List<Double> result = new ArrayList<Double>();

		for (Object object : list) {

			if (object instanceof Double) {

				result.add((Double) object);

			} else if (object instanceof String) {

				try {

					result.add(Double.valueOf((String) object));

				} catch (Exception ex) {

				}

			} else if (object instanceof Character) {

				result.add((double) ((Character) object).charValue());

			} else if (object instanceof Number) {

				result.add(((Number) object).doubleValue());

			}

		}

		return result;

	}

	/**
	 * 
	 * @param path
	 * @return list of values (type float) associated with path specified
	 */
	public List<Float> getFloatList(String path) {

		List<?> list = getList(path);

		if (list == null) {

			return new ArrayList<Float>(0);
		}

		List<Float> result = new ArrayList<Float>();

		for (Object object : list) {

			if (object instanceof Float) {

				result.add((Float) object);
			} 
			
			if (object instanceof String) {
			
				result.add(Float.valueOf((String) object));

			} 
			
			if (object instanceof Character) {

				result.add((float) ((Character) object).charValue());

			}
			
			if (object instanceof Number) {

				result.add(((Number) object).floatValue());
			}
		}

		return result;
	}

	/**
	 * 
	 * @param path
	 * @return list of values (type long) associated with path specified
	 */
	public List<Long> getLongList(String path) {

		List<?> list = getList(path);

		if (list == null) {

			return new ArrayList<Long>(0);
		}

		List<Long> result = new ArrayList<Long>();

		for (Object object : list) {

			if (object instanceof Long) {

				result.add((Long) object);
			} 
			
			if (object instanceof String) {
					result.add(Long.valueOf((String) object));

			} 
			
			if (object instanceof Character) {

				result.add((long) ((Character) object).charValue());
			}
			
			if (object instanceof Number) {

				result.add(((Number) object).longValue());
			}
		}

		return result;
	}

	/**
	 * 
	 * @param path
	 * @return list of values (type byte) associated with path specified
	 */
	public List<Byte> getByteList(String path) {

		List<?> list = getList(path);

		if (list == null) {

			return new ArrayList<Byte>(0);
		}

		List<Byte> result = new ArrayList<Byte>();

		for (Object object : list) {

			if (object instanceof Byte) {

				result.add((Byte) object);
			} 
			
			if (object instanceof String) {
					
				result.add(Byte.valueOf((String) object));
			} 
			
			if (object instanceof Character) {

				result.add((byte) ((Character) object).charValue());
			}
			
			if (object instanceof Number) {

				result.add(((Number) object).byteValue());
			}
		}

		return result;
	}

	/**
	 * 
	 * @param path
	 * @return list of values (type character) associated with path specified
	 */
	public List<Character> getCharacterList(String path) {

		List<?> list = getList(path);

		if (list == null) {

			return new ArrayList<Character>(0);
		}

		List<Character> result = new ArrayList<Character>();

		for (Object object : list) {

			if (object instanceof Character) {

				result.add((Character) object);
			}
			
			if (object instanceof String) {

				String str = (String) object;

				if (str.length() == 1) {

					result.add(str.charAt(0));
				}

			}
			
			if (object instanceof Number) {

				result.add((char) ((Number) object).intValue());
			}
		}

		return result;
	}

	/**
	 * 
	 * @param path
	 * @return list of values (type short) associated with path specified
	 */
	public List<Short> getShortList(String path) {

		List<?> list = getList(path);

		if (list == null) {

			return new ArrayList<Short>(0);
		}

		List<Short> result = new ArrayList<Short>();

		for (Object object : list) {

			if (object instanceof Short) {

				result.add((Short) object);

			} 
			
			if (object instanceof String) {

					result.add(Short.valueOf((String) object));
			} 
			
			if (object instanceof Character) {

				result.add((short) ((Character) object).charValue());
			} 
			
			if (object instanceof Number) {
				
				result.add(((Number) object).shortValue());
			}
		}

		return result;
	}

	/**
	 * 
	 * @param path
	 * @return list of values (type map<?, ?>) associated with path specified
	 */
	public List<Map<?, ?>> getMapList(String path) {

		List<?> list = getList(path);

		List<Map<?, ?>> result = new ArrayList<Map<?, ?>>();

		if (list == null) {

			return result;
		}

		for (Object object : list) {

			if (object instanceof Map) {

				result.add((Map<?, ?>) object);
			}
		}

		return result;
	}



	/**
	 * 
	 * @param path
	 * @return 
	 */
	public boolean contains(String path) {
		
		return this.config.contains(path);
	}

	/**
	 * Removes an entry from the configuration.
	 * 
	 * @param path
	 */
	public void removeKey(String path) {
		
		this.config.set(path, null);
	}

	/**
	 * Sets specified identifier to specified value of type object.
	 * 
	 * @param path
	 * @param value
	 */
	public void set(String path, Object value) {
		
		this.config.set(path, value);
	}

	/**
	 * Sets specified identifier to specified value of type object,
	 * along with a single comment.
	 * 
	 * @param path
	 * @param value
	 * @param comment
	 */
	public void set(String path, Object value, String comment) {
		
		if(!(this.config.contains(path))) {
			
			this.config.set(manager.getPluginName() + "_COMMENT_" + comments, " " + comment);
			comments++;
		}

		this.config.set(path, value);
	}

	/**
	 * Sets specified identifier to specified value of type object,
	 * along with multiple comments.
	 * 
	 * @param path
	 * @param value
	 * @param comment
	 */
	public void set(String path, Object value, String[] comment) {

		for(String comm : comment) {

			if(!this.config.contains(path)) {
				this.config.set(manager.getPluginName() + "_COMMENT_" + comments, " " + comm);
				comments++;
			}
		}

		this.config.set(path, value);
	}
	
	/**
	 * Sets the specified path/identifier to the default configuration value.
	 * Intended to be used in order to produce default configuration.
	 * 
	 * @param identifier
	 * @param configurationValue - configuration value to use
	 */
	public void defaultSet(String identifier, ConfigurationValue configurationValue) {
		
		set(identifier, configurationValue.getDefaultValue(), configurationValue.getComments()); 
	}

	/**
	 * Sets header comprised of comments.
	 * 
	 * @param header
	 */
	public void setHeader(String[] header) {
		
		manager.setHeader(this.file, header);
		this.comments = header.length + 2;
		this.reloadConfig();
	}

	/**
	 * Loads configuration from reader.
	 */
	public void reloadConfig() {
		
		Reader reader = null;
		try {
			reader = new InputStreamReader(manager.getConfigContent(file), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		this.config = YamlConfiguration.loadConfiguration(reader);
	}

	/**
	 * 
	 * Saves configuration.
	 */
	public void saveConfig() {
		
		String config = this.config.saveToString();
		manager.saveConfig(config, this.file);
	}

	/**
	 * 
	 * @return identifier entries
	 */
	public Set<String> getKeys() {
		
		return this.config.getKeys(false);
	}

	/**
	 * 
	 * @param input
	 * @return whether input can represent a primitive
	 */
	protected boolean isPrimitiveWrapper(Object input) {

		return input instanceof Integer || input instanceof Boolean ||
				input instanceof Character || input instanceof Byte ||
				input instanceof Short || input instanceof Double ||
				input instanceof Long || input instanceof Float;
	}

}