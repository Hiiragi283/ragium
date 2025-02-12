package hiiragi283.ragium.integration.jei.entry

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.data.HTSoap
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.RegistryFixedCodec
import net.minecraft.world.level.block.Block

data class HTSoapEntry(val input: Holder<Block>, val output: HTSoap) {
    companion object {
        @JvmField
        val CODEC: Codec<HTSoapEntry> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    RegistryFixedCodec.create(Registries.BLOCK).fieldOf("input").forGetter(HTSoapEntry::input),
                    HTSoap.CODEC.fieldOf("output").forGetter(HTSoapEntry::output),
                ).apply(instance, ::HTSoapEntry)
        }
    }
}
