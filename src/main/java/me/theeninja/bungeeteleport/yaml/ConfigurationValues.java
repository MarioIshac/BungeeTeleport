package me.theeninja.bungeeteleport.yaml;

/**
 * A class used to construct and hold default 
 * configuration values along with their comments.
 * 
 * @author TheeNinja
 *
 */
public class ConfigurationValues {
	
	/**
	 * An inner class representing the structure of a configuration value.
	 * 
	 * @author TheeNinja
	 *
	 */
	public static class ConfigurationValue {
		
		private String[] comments;
		private String defaultValue;
		private String identifier;
		
		/**
		 * 
		 * @param comments - comments for configuration value
		 * @param defaultValue - the value to be used in the default configuration
		 */
		ConfigurationValue(String[] comments, String defaultValue, String identifier) {
			this.comments = comments;
			this.defaultValue = defaultValue;
			this.identifier = identifier;
		}
		
		/**
		 * Returns the comments for configuration value
		 * 
		 * @return a string array consisting of the comments
		 */
		public String[] getComments() {
			return comments;
		}
		
		/**
		 * Returns the default value for configuration value
		 * 
		 * @return default value of configuration value
		 */
		public String getDefaultValue() {
			return defaultValue;
		}
		
		public String getIdentifier() {
			return identifier;
		}
	}
	
	
	public static ConfigurationValue noPermissionUseMessage = new ConfigurationValue(new String[] {
			"This message is sent when a user does not have", 
			"permission to use a BungeeTeleport sign"
	}, "You do not have permission to use this sign!", "NoPermissionUseMessage");
	public static ConfigurationValue noPermissionBuildMessage = new ConfigurationValue(new String[] {
			"This message is sent when a user does not have", 
			"permission to build a BungeeTeleport sign"
	}, "You do not have permission to build this sign!", "NoPermissionBuildMessage");
			
}
