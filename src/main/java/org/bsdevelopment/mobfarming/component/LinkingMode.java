package org.bsdevelopment.mobfarming.component;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;
import java.util.function.IntFunction;

public enum LinkingMode implements StringRepresentable {
    CURRENT_POS,
    INITIAL_POS;

    private static final IntFunction<LinkingMode> BY_ID = ByIdMap.continuous(LinkingMode::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StringRepresentable.EnumCodec<LinkingMode> CODEC = StringRepresentable.fromEnum(LinkingMode::values);
    public static final StreamCodec<ByteBuf, LinkingMode> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, LinkingMode::ordinal);

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
