package org.bsdevelopment.mobfarming.container;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.bsdevelopment.mobfarming.ModConstants;

public class ModContainers {
    private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(Registries.MENU, ModConstants.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<MobBlenderContainer>> MOB_BLENDER = CONTAINERS.register("mob_blender",
            () -> IMenuTypeExtension.create(MobBlenderContainer::new));

    public static void register(IEventBus evt) {
        CONTAINERS.register(evt);
    }
}
