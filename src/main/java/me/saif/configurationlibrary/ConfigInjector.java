package me.saif.configurationlibrary;

import me.saif.configurationlibrary.types.ItemStackConfigAccessor;
import me.saif.configurationlibrary.utils.ReflectionHelper;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigInjector {

    private static final ConfigInjector instance = new ConfigInjector();

    public static ConfigInjector getInstance() {
        return instance;
    }

    static {
        if (ConfigInjector.class.getPackage().getName().equals("me.saif.configurationlibrary")) {
            throw new IllegalStateException("ConfigurationLibrary must be relocated when shaded");
        }
    }

    private TypeRegistry registry = new TypeRegistry();

    private ConfigInjector() {
        this.registry.addType(ItemStack.class, new ItemStackConfigAccessor());
    }

    public TypeRegistry getRegistry() {
        return registry;
    }

    public boolean inject(ConfigurationSection configuration, Object instance, String basePath) {
        if (basePath == null)
            basePath = "";
        List<Field> configFields = Arrays.stream(ReflectionHelper.getAllFields(instance.getClass())).filter(field -> field.isAnnotationPresent(ConfigValue.class)).collect(Collectors.toList());

        boolean changesMade = false;
        for (Field configField : configFields) {
            if (Modifier.isFinal(configField.getModifiers())) {
                throw new IllegalStateException(configField.getName() + " is final in " + configField.getDeclaringClass().getName());
            }
            try {
                configField.setAccessible(true);
                ConfigValue value = configField.getAnnotation(ConfigValue.class);
                String path = basePath + value.path();
                Object currentVal = configField.get(instance);
                boolean copyIfAbsent = value.copyIfAbsent();

                System.out.println(path);
                System.out.println("Current Value: " + currentVal);

                Object returned = this.registry.loadFromConfig(configuration, configField.getType(), path, currentVal, copyIfAbsent);

                System.out.println("Loaded Value: " + returned);

                configField.set(instance, returned);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }

        return changesMade;
    }



    public boolean inject(ConfigurationSection configuration, Object instance) {
        return inject(configuration, instance, null);
    }

}
