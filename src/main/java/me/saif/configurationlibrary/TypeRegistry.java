package me.saif.configurationlibrary;

import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class TypeRegistry {

    private Map<Class<?>, ConfigAccessor<?>> registryMap = new HashMap<>();

    protected Object loadFromConfig(ConfigurationSection section, Class<?> type, String path, Object def, boolean copyIfAbsent) {
        if (type == int.class || type == Integer.class) {
            if (section.isInt(path)) {
                return section.getInt(path);
            } else {
                if (copyIfAbsent)
                    section.set(path, def);
                return def;
            }
        }

        if (type == long.class || type == Long.class) {
            if (section.isLong(path)) {
                return section.getLong(path);
            } else {
                if (copyIfAbsent)
                    section.set(path, def);
                return def;
            }
        }

        if (type == double.class || type == Double.class) {
            if (section.isDouble(path)) {
                return section.getDouble(path);
            } else {
                if (copyIfAbsent)
                    section.set(path, def);
                return def;
            }
        }

        if (type == boolean.class || type == Boolean.class) {
            if (section.isBoolean(path)) {
                return section.getBoolean(path);
            } else {
                if (copyIfAbsent)
                    section.set(path, def);
                return def;
            }
        }

        if (type == String.class) {
            if (section.isString(path)) {
                return section.getString(path);
            } else {
                if (copyIfAbsent)
                    section.set(path, def);
                return def;
            }
        }

        if (type.isEnum()) {
            String defaultVal = def == null ? "" : ((Enum<?>) def).name();
            String val = section.getString(path, defaultVal);

            for (Enum<?> enumConstant : (Enum<?>[]) type.getEnumConstants()) {
                if (enumConstant.name().equalsIgnoreCase(val))
                    return enumConstant;
            }


            if (copyIfAbsent)
                section.set(path, ((Enum<?>) def).name());
            return def;
        }

        ConfigAccessor<?> configAccessor = registryMap.get(type);

        if (configAccessor == null) {
            //TODO do something if the type not registered
            return def;
        }

        Object loaded = configAccessor.get(section, path);

        if (loaded != null)
            return loaded;

        if (copyIfAbsent) {
            ((ConfigAccessor<Object>) configAccessor).set(section, path, def);
        }

        return def;
    }

    public <T> void addType(Class<T> clazz, ConfigAccessor<T> configAccessor) {
        this.addType(clazz, configAccessor, false);
    }

    public <T> void addType(Class<T> clazz, ConfigAccessor<T> configAccessor, boolean overwrite) {
        if (this.registryMap.get(clazz) != null && !overwrite)
            throw new RuntimeException(clazz.getName() + " already has a registered Config Accessor.");

        this.registryMap.put(clazz, configAccessor);
    }

}
