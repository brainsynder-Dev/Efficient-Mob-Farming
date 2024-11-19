package org.bsdevelopment.mobfarming;

import com.mojang.serialization.Codec;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.bsdevelopment.mobfarming.component.LinkingRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModComponents {
    public static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, ModConstants.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<GlobalPos>> DESTINATION_POS = register("destination_pos", GlobalPos.CODEC);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<LinkingRecord>> LINKAGE_DATA = register("linkage_data", LinkingRecord.CODEC);

    private static @NotNull <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, final Codec<T> codec) {
        return register(name, codec, null);
    }

    private static @NotNull <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, final Codec<T> codec, @Nullable final StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        if (streamCodec == null) {
            return COMPONENTS.register(name, () -> DataComponentType.<T>builder().persistent(codec).build());
        } else {
            return COMPONENTS.register(name, () -> DataComponentType.<T>builder().persistent(codec).networkSynchronized(streamCodec).build());
        }
    }

    public static void register(IEventBus evt) {
        COMPONENTS.register(evt);
    }
}
