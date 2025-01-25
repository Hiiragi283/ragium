package hiiragi283.ragium.api.machine

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec

data class HTMachineDefinition(val key: HTMachineKey, val tier: HTMachineTier) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTMachineDefinition> =
            RecordCodecBuilder.mapCodec { instance ->
                instance
                    .group(
                        HTMachineKey.FIELD_CODEC.forGetter(HTMachineDefinition::key),
                        HTMachineTier.FIELD_CODEC.forGetter(HTMachineDefinition::tier),
                    ).apply(instance, ::HTMachineDefinition)
            }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTMachineDefinition> =
            StreamCodec.composite(
                HTMachineKey.STREAM_CODEC,
                HTMachineDefinition::key,
                HTMachineTier.STREAM_CODEC,
                HTMachineDefinition::tier,
                ::HTMachineDefinition,
            )
    }
}
