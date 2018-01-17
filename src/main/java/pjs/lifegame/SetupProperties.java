package pjs.lifegame;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


/**
 * <h1>SetupProperties</h1>
 * Loads the properties file and acts as reference for any class that wants to get
 * these properties. Can get the properties as various objects: String, Integer, List<Integer>.
 * 
 * @author Paul Seebald
 * @version 1.0
 * @since 2018-01-12
 */
public class SetupProperties {
	
	private static Properties properties = null;
	private static String propertyLocation = null;
	
	/**
	 * Used to set the location of the properties file.
	 * @param location
	 */
	public static void setPropertiesLocation(String location) {
		propertyLocation = location;
	}
	
	/**
	 * Initializes the properties by loading the file.
	 */
	public static void initProperties() {
		properties = new Properties();
		
		if (propertyLocation == null) {
			// If propertyLocation is not defined (i.e. not input to GameApp as part of args), use the default.
			if (GameApp.propertyLocation == null) {
				setPropertiesLocation(GameApp.defaultPropertyLocation);
			} else {
				setPropertiesLocation(GameApp.propertyLocation);
			}
		}
		
		try (BufferedInputStream propsStream = new BufferedInputStream(new FileInputStream(propertyLocation)) ) {
			properties.load(propsStream);
		} catch (FileNotFoundException e) {
			System.out.println("File " + propertyLocation + " was not found.");
			System.out.println();
			e.printStackTrace();
			throw new RuntimeException("Please enter a valid file location.");
		} catch (IOException e) {
			System.out.println("File " + propertyLocation + " could not be read.");
			System.out.println();
			e.printStackTrace();
			throw new RuntimeException("Please check file for correct syntax.");
		}
	}

	/**
	 * Gets the property value associated with the key.
	 * @param key					Key for desired property.
	 * @return String property		Property value as a String.
	 */
	public static String getProperty(String key) {
		if (properties == null) {
			initProperties();
		}

		if (!properties.containsKey(key)) {
			throw new RuntimeException("Properties file " + propertyLocation + " does not contain key: " + key);
		}
		
		return properties.getProperty(key);
	}
	
	/**
	 * Gets the property value associated with the key.
	 * @param key					Key for desired property.
	 * @return Integer property		Property value as a Integer.
	 */
	public static Integer getIntProperty(String key) {
		try {
			return Integer.valueOf(getProperty(key));
		} catch (NumberFormatException e) {
			System.out.println(e.getClass() + ": " + e.getMessage());
			throw new RuntimeException("Value for key " + key + " in properties is not a valid integer.");
		}
	}
	
	/**
	 * Gets the property value associated with the key.
	 * @param key					Key for desired property.
	 * @return Boolean property		Property value as a Boolean.
	 */
	public static boolean getBooleanProperty(String key) {
		String value = getProperty(key);
		if (value.equalsIgnoreCase("F")) {
			return false;
		} else if (value.equalsIgnoreCase("T")) {
			return true;
		}
		
		throw new RuntimeException("Boolean property value of key " + key + " was not T or F, as expected, it was: " + value);
	}
	
	/**
	 * Gets the property associated with the key.
	 * @param key							Key for desired property.
	 * @return List<Integer> property		Property value as a List<Integer>.
	 */
	public static List<Integer> getIntListProperty(String key) {
		List<String> strProp =  new ArrayList<String>(Arrays.asList(getProperty(key).split(",")));
		return ParseUtil.convertStrListToIntList(strProp);
	}
	
	/**
	 * Sets a value within the properties. Used in tests.
	 * @param key		Property key to change.
	 * @param value		Value to set for property key.
	 */
	public static void setProperty(String key, String value) {
		if (properties == null) {
			initProperties();
		}
		
		properties.setProperty(key, value);
	}

}
