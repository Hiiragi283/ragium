package hiiragi283.ragium.integration.jei

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.machine.HTMachineTier
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.RegistryFixedCodec
import net.minecraft.world.level.block.Block

data class JEITemperatureInfo(val machineTier: HTMachineTier, val block: Holder<Block>) {
    companion object {
        @JvmField
        val CODEC: Codec<JEITemperatureInfo> = RecordCodecBuilder.create<JEITemperatureInfo> { instance ->
            instance
                .group(
                    HTMachineTier.FIELD_CODEC.forGetter(JEITemperatureInfo::machineTier),
                    RegistryFixedCodec.create(Registries.BLOCK).fieldOf("block").forGetter(JEITemperatureInfo::block),
                ).apply(instance, ::JEITemperatureInfo)
        }
    }
}
