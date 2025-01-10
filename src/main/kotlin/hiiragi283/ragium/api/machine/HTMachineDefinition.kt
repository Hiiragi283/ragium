package hiiragi283.ragium.api.machine

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec

data class HTMachineDefinition(val key: HTMachineKey, val tier: HTMachineTier) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTMachineDefinition> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTMachineKey.CODEC.fieldOf("machine_type").forGetter(HTMachineDefinition::key),
                    HTMachineTier.FIELD_CODEC.forGetter(HTMachineDefinition::tier),
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
