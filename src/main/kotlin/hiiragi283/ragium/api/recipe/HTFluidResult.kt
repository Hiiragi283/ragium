package hiiragi283.ragium.api.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.entryPacketCodec
import hiiragi283.ragium.api.extension.isFilledMax
import hiiragi283.ragium.api.extension.longRangeCodec
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import net.minecraft.fluid.Fluid
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registries
import net.minecraft.registry.entry.RegistryEntry

class HTFluidResult(val entry: RegistryEntry<Fluid>, val amount: Long = FluidConstants.BUCKET) {
    companion object {
        @JvmField
        val CODEC: Codec<HTFluidResult> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    Registries.FLUID.entryCodec
                        .fieldOf("fluid")
                        .forGetter(HTFluidResult::entry),
                    longRangeCodec(1, Long.MAX_VALUE)
                        .optionalFieldOf("amount", FluidConstants.BUCKET)
                        .forGetter(HTFluidResult::amount),
                ).apply(instance, ::HTFluidResult)
        }

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTFluidResult> = PacketCodec.tuple(
            Registries.FLUID.entryPacketCodec,
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

    fun canMerge(storage: SingleSlotStorage<FluidVariant>): Boolean = when {
        storage.isFilledMax -> false
        storage.isResourceBlank -> storage.amount + this.amount <= storage.capacity
        storage.resource == variant -> storage.amount + this.amount <= storage.capacity
        else -> false
    }

    fun merge(storage: SingleSlotStorage<FluidVariant>, transaction: TransactionContext): Long = when {
        canMerge(storage) -> storage.insert(variant, amount, transaction)
        else -> 0
    }

    override fun toString(): String = "HTFluidResult[fluid=${entry.idAsString},amount=$amount]"
}
