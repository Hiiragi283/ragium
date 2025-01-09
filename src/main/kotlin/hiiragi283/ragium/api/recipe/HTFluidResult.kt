package hiiragi283.ragium.api.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.codec.RagiumCodecs
import hiiragi283.ragium.api.extension.POSITIVE_LONG_CODEC
import hiiragi283.ragium.api.extension.isEmpty
import hiiragi283.ragium.api.extension.isFilledMax
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import net.minecraft.fluid.Fluid
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.entry.RegistryFixedCodec

/**
 * 液体の完成品を扱うクラス
 * @param entry 液体の[RegistryEntry]
 * @param amount 液体の量
 */
class HTFluidResult(val entry: RegistryEntry<Fluid>, val amount: Long = FluidConstants.BUCKET) {
    companion object {
        @JvmField
        val CODEC: Codec<HTFluidResult> = RagiumCodecs.simpleOrComplex(
            RegistryFixedCodec.of(RegistryKeys.FLUID).xmap(::HTFluidResult, HTFluidResult::entry),
            RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        RegistryFixedCodec
                            .of(RegistryKeys.FLUID)
                            .fieldOf("fluid")
                            .forGetter(HTFluidResult::entry),
                        POSITIVE_LONG_CODEC
                            .optionalFieldOf("amount", FluidConstants.BUCKET)
                            .forGetter(HTFluidResult::amount),
                    ).apply(instance, ::HTFluidResult)
            },
        ) { result: HTFluidResult -> result.amount == FluidConstants.BUCKET }

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTFluidResult> = PacketCodec.tuple(
            PacketCodecs.registryEntry(RegistryKeys.FLUID),
            HTFluidResult::entry,
            PacketCodecs.VAR_LONG,
            HTFluidResult::amount,
            ::HTFluidResult,
        )
    }

    init {
        StoragePreconditions.notNegative(amount)
    }

    @Suppress("DEPRECATION")
    constructor(fluid: Fluid, amount: Long = FluidConstants.BUCKET) : this(fluid.registryEntry, amount)

    val fluid: Fluid
        get() = entry.value()
    val variant: FluidVariant
        get() = FluidVariant.of(fluid)
    val isEmpty: Boolean
        get() = fluid.isEmpty

    /**
     * 指定した[storage]にマージできるか判定します。
     */
    fun canMerge(storage: SingleSlotStorage<FluidVariant>): Boolean = when {
        storage.isFilledMax -> false
        storage.isEmpty -> storage.amount + this.amount <= storage.capacity
        storage.resource == variant -> storage.amount + this.amount <= storage.capacity
        else -> false
    }

    /**
     * 指定した[storage]にマージします。
     * @return [storage]に搬入できた量
     */
    fun merge(storage: SingleSlotStorage<FluidVariant>, transaction: TransactionContext): Long = when {
        canMerge(storage) -> storage.insert(variant, amount, transaction)
        else -> 0
    }

    override fun toString(): String = "HTFluidResult[fluid=${entry.idAsString},amount=$amount]"
}
