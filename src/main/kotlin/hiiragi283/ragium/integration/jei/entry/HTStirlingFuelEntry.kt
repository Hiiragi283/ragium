package hiiragi283.ragium.integration.jei.entry

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.RegistryFixedCodec
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.item.Item

data class HTStirlingFuelEntry(val input: Holder<Item>, val burnTime: Int) : Comparable<HTStirlingFuelEntry> {
    companion object {
        @JvmField
        val CODEC: Codec<HTStirlingFuelEntry> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    RegistryFixedCodec.create(Registries.ITEM).fieldOf("input").forGetter(HTStirlingFuelEntry::input),
                    ExtraCodecs.POSITIVE_INT.fieldOf("burn_time").forGetter(HTStirlingFuelEntry::burnTime),
                ).apply(instance, ::HTStirlingFuelEntry)
        }

        @JvmField
        val COMPARATOR: Comparator<HTStirlingFuelEntry> = compareBy(HTStirlingFuelEntry::burnTime).reversed()
    }

    override fun compareTo(other: HTStirlingFuelEntry): Int = COMPARATOR.compare(this, other)
}
