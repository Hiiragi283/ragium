package hiiragi283.ragium.integration.jei.entry

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.data.HTHammerDrop
import hiiragi283.ragium.api.extension.idOrThrow
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.RegistryFixedCodec
import net.minecraft.world.level.block.Block

data class HTHammerDropEntry(val input: Holder<Block>, val output: HTHammerDrop) : Comparable<HTHammerDropEntry> {
    companion object {
        @JvmField
        val CODEC: Codec<HTHammerDropEntry> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    RegistryFixedCodec.create(Registries.BLOCK).fieldOf("input").forGetter(HTHammerDropEntry::input),
                    HTHammerDrop.CODEC.fieldOf("output").forGetter(HTHammerDropEntry::output),
                ).apply(instance, ::HTHammerDropEntry)
        }

        @JvmField
        val COMPARATOR: Comparator<HTHammerDropEntry> = compareBy { it.input.idOrThrow }
    }

    override fun compareTo(other: HTHammerDropEntry): Int = COMPARATOR.compare(this, other)
}
