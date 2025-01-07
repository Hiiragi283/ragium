package hiiragi283.ragium.api.storage

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.content.HTFluidContent
import hiiragi283.ragium.api.data.RagiumCodecs
import hiiragi283.ragium.api.extension.NON_NEGATIVE_LONG_CODEC
import hiiragi283.ragium.api.extension.isIn
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.minecraft.fluid.Fluid
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.tag.TagKey

/**
 * [FluidVariant]向けの[HTVariantStack]の実装
 */
class HTFluidVariantStack(variant: FluidVariant, amount: Long) : HTVariantStack<Fluid, FluidVariant>(variant, amount) {
    companion object {
        /**
         * [HTVariantStack.isEmpty]が常にtrueとなるインスタンス
         */
        @JvmField
        val EMPTY = HTFluidVariantStack(FluidVariant.blank(), 0)

        @JvmField
        val CODEC: Codec<HTFluidVariantStack> = RecordCodecBuilder
            .create<HTFluidVariantStack> { instance ->
                instance
                    .group(
                        RagiumCodecs.FLUID_VARIANT.fieldOf("variant").forGetter(HTFluidVariantStack::variant),
                        NON_NEGATIVE_LONG_CODEC.fieldOf("amount").forGetter(HTFluidVariantStack::amount),
                    ).apply(instance, ::HTFluidVariantStack)
            }.validate(HTVariantStack.Companion::validate)

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTFluidVariantStack> = PacketCodec.tuple(
            FluidVariant.PACKET_CODEC,
            HTFluidVariantStack::variant,
            PacketCodecs.VAR_LONG,
            HTFluidVariantStack::amount,
            ::HTFluidVariantStack,
        )
    }

    constructor(fluid: Fluid, amount: Long) : this(FluidVariant.of(fluid), amount)

    constructor(content: HTFluidContent, amount: Long) : this(content.get(), amount)

    /**
     * [FluidVariant]のオブジェクト
     */
    val fluid: Fluid = variant.fluid

    /**
     * 指定した[tagKey]に[variant]が含まれているか判定します。
     * @see [FluidVariant.isIn]
     */
    fun isIn(tagKey: TagKey<Fluid>): Boolean = variant.isIn(tagKey)
}
