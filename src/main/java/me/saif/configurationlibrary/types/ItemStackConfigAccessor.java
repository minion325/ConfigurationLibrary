package me.saif.configurationlibrary.types;

import me.saif.configurationlibrary.ConfigAccessor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class ItemStackConfigAccessor implements ConfigAccessor<ItemStack> {

    @Override
    public ItemStack get(ConfigurationSection section, String path) {
        return section.getItemStack(path);
    }

    @Override
    public void set(ConfigurationSection section, String path, ItemStack toSet) {
        section.set(path, toSet);
    }
}
