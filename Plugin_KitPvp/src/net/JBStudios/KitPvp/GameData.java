package net.JBStudios.KitPvp;

import java.util.Properties;

public class GameData {
	
	public enum PropKeys {
		Lobby("lobby"),
		Name("name");
		
		String value;
		
		private PropKeys(String value) {
			this.value = value;
		}
		
		public String toString() {
			return value;
		}
	}
	
	private Properties properties;
	
	public GameData(String name) {
		properties = new Properties();
		setProperty(PropKeys.Name, name);
	}
	
	public GameData(Properties properties) {
		this.properties = properties;
	}
	
	public Properties getProperties() {
		return properties;
	}
	
	public String getProperty(PropKeys key) {
		return properties.getProperty(key+"");
	}
	
	public void setProperty(PropKeys key, String value) {
		properties.setProperty(key+"", value);
	}
	
}
