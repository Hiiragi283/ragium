package hiiragi283.ragium.api.machine

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec

data class HTMachineDefinition(val type: HTMachineType, val tier: HTMachineTier) {
    companion object {
        @JvmField
        val CODEC: Codec<HTMachineDefinition> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    HTMachineTypeRegistry.CODEC.fieldOf("type").forGetter(HTMachineDefinition::type),
                    HTMachineTier.CODEC.fieldOf("tier").forGetter(HTMachineDefinition::tier),
                ).apply(instance, ::HTMachineDefinition)
        }

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTMachineDefinition> = PacketCodec.tuple(
            HTMachineTypeRegistry.PACKET_CODEC,
            HTMachineDefinition::type,
            HTMachineTier.PACKET_CODEC,
            HTMachineDefinition::tier,
            ::HTMachineDefinition,
        )
    }

    val iconStack: ItemStack
        get() = type.createItemStack(tier)
}
