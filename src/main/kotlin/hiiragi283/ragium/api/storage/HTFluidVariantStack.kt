package hiiragi283.ragium.api.storage

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.NON_NEGATIVE_LONG_CODEC
import hiiragi283.ragium.api.extension.isIn
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.impl.transfer.VariantCodecs
import net.minecraft.fluid.Fluid
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.tag.TagKey

/**
 * [HTVariantStack] for [FluidVariant]
 */
@Suppress("UnstableApiUsage")
class HTFluidVariantStack(override val variant: FluidVariant, override val amount: Long) : HTVariantStack<Fluid, FluidVariant> {
    companion object {
        @JvmField
        val EMPTY = HTFluidVariantStack(FluidVariant.blank(), 0)

        @JvmField
        val CODEC: Codec<HTFluidVariantStack> = RecordCodecBuilder
            .create<HTFluidVariantStack> { instance ->
                instance
                    .group(
                        VariantCodecs.FLUID_CODEC.fieldOf("variant").forGetter(HTFluidVariantStack::variant),
                        NON_NEGATIVE_LONG_CODEC.fieldOf("amount").forGetter(HTFluidVariantStack::amount),
                    ).apply(instance, ::HTFluidVariantStack)
            }.validate(HTVariantStack.Companion::validate)

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTFluidVariantStack> = PacketCodec.tuple(
            VariantCodecs.FLUID_PACKET_CODEC,
            HTFluidVariantStack::variant,
            PacketCodecs.VAR_LONG,
            HTFluidVariantStack::amount,
            ::HTFluidVariantStack,
        )
    }

    constructor(fluid: Fluid, amount: Long) : this(FluidVariant.of(fluid), amount)

    val fluid: Fluid = variant.fluid

    fun isIn(tagKey: TagKey<Fluid>): Boolean = variant.isIn(tagKey)
}
