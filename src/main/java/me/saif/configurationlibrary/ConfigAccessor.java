package me.saif.configurationlibrary;

import org.bukkit.configuration.ConfigurationSection;

public interface ConfigAccessor<T> {

    T get(ConfigurationSection section, String path);

    void set(ConfigurationSection section, String path, T toSet);

}
