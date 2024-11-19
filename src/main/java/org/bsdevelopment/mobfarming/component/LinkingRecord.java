package org.bsdevelopment.mobfarming.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.GlobalPos;

import java.util.Objects;
import java.util.Optional;

public final class LinkingRecord {
    public static final Codec<LinkingRecord> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            GlobalPos.CODEC.optionalFieldOf("stored-pos").forGetter(comp -> Optional.ofNullable(comp.storedPos())),
            LinkingMode.CODEC.fieldOf("mode").forGetter(LinkingRecord::mode)
    ).apply(instance, (globalPos, mode) -> new LinkingRecord(globalPos.orElse(null), mode)));

    private GlobalPos storedPos;
    private LinkingMode mode;

    public LinkingRecord(GlobalPos storedPos, LinkingMode mode) {
        this.storedPos = storedPos;
        this.mode = mode;
    }

    public void setStoredPos(GlobalPos storedPos) {
        this.storedPos = storedPos;
    }

    public void setMode(LinkingMode mode) {
        this.mode = mode;
    }

    public GlobalPos storedPos() {
        return storedPos;
    }

    public LinkingMode mode() {
        return mode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (LinkingRecord) obj;
        return Objects.equals(this.storedPos, that.storedPos) &&
                Objects.equals(this.mode, that.mode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storedPos, mode);
    }

    @Override
    public String toString() {
        return "LinkingRecord[" +
                "storedPos=" + storedPos + ", " +
                "mode=" + mode + ']';
    }

}
