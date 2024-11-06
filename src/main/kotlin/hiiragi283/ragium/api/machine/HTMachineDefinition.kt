package hiiragi283.ragium.api.machine

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec

data class HTMachineDefinition(val key: HTMachineKey, val tier: HTMachineTier) {
    companion object {
        @JvmField
        val CODEC: Codec<HTMachineDefinition> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    HTMachineKey.CODEC.fieldOf("type").forGetter(HTMachineDefinition::key),
                    HTMachineTier.CODEC.fieldOf("tier").forGetter(HTMachineDefinition::tier),
                ).apply(instance, ::HTMachineDefinition)
        }

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTMachineDefinition> = PacketCodec.tuple(
            HTMachineKey.PACKET_CODEC,
            HTMachineDefinition::key,
            HTMachineTier.PACKET_CODEC,
            HTMachineDefinition::tier,
            ::HTMachineDefinition,
        )
    }

    val iconStack: ItemStack
        get() = key.createItemStack(tier)
}
