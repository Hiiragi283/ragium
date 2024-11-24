package hiiragi283.ragium.api.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.*
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryCodecs
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.entry.RegistryEntryList
import net.minecraft.registry.tag.TagKey
import net.minecraft.text.MutableText
import java.util.function.BiPredicate

class HTFluidIngredient private constructor(private val entryList: RegistryEntryList<Fluid>, val amount: Long) : BiPredicate<Fluid, Long> {
    companion object {
        @JvmField
        val CODEC: Codec<HTFluidIngredient> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    RegistryCodecs
                        .entryList(RegistryKeys.FLUID)
                        .fieldOf("fluids")
                        .forGetter(HTFluidIngredient::entryList),
                    longRangeCodec(1, Long.MAX_VALUE)
                        .optionalFieldOf("amount", FluidConstants.BUCKET)
                        .forGetter(HTFluidIngredient::amount),
                ).apply(instance, ::HTFluidIngredient)
        }

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTFluidIngredient> = PacketCodec
            .tuple(
                PacketCodecs.registryEntryList(RegistryKeys.FLUID),
                HTFluidIngredient::entryList,
                PacketCodecs.VAR_LONG,
                HTFluidIngredient::amount,
                ::HTFluidIngredient,
            )

        @JvmStatic
        fun of(fluid: Fluid, amount: Long = FluidConstants.BUCKET): HTFluidIngredient =
            HTFluidIngredient(RegistryEntryList.of(fluid.registryEntry), amount)

        @JvmStatic
        fun of(tagKey: TagKey<Fluid>, amount: Long = FluidConstants.BUCKET): HTFluidIngredient =
            HTFluidIngredient(Registries.FLUID.getOrCreateEntryList(tagKey), amount)
    }

    val isEmpty: Boolean
        get() =
            entryList.storage.map(
                { false },
                { it.isEmpty() || it.any { entry: RegistryEntry<Fluid> -> entry.isOf(Fluids.EMPTY) } },
            ) ||
                amount <= 0

    fun <T : Any> map(transform: (RegistryEntry<Fluid>, Long) -> T): List<T> = entryList.map { transform(it, amount) }

    val text: MutableText
        get() = entryList.asText(Fluid::name)

    val firstEntry: RegistryEntry<Fluid>?
        get() = entryList.firstOrNull()

    val entryMap: Map<RegistryEntry<Fluid>, Long> = entryList.associateWith { amount }

    val valueMap: Map<Fluid, Long>
        get() = entryMap.mapKeys { it.key.value() }

    fun test(resource: ResourceAmount<FluidVariant>): Boolean = test(resource.resource.fluid, resource.amount)

    override fun test(fluid: Fluid, amount: Long): Boolean = when {
        fluid == Fluids.EMPTY || amount <= 0 -> this.isEmpty
        else -> fluid in entryList && amount >= this.amount
    }

    fun onConsume(storage: SingleSlotStorage<FluidVariant>) {
        useTransaction { transaction: Transaction ->
            val foundVariant: FluidVariant =
                StorageUtil.findExtractableResource(
                    storage,
                    { test(it.fluid, storage.amount) },
                    transaction,
                ) ?: return
            if (test(foundVariant.fluid, amount)) {
                val extracted: Long = storage.extract(foundVariant, amount, transaction)
                if (extracted == amount) {
                    transaction.commit()
                } else {
                    transaction.abort()
                }
            }
        }
    }

    override fun toString(): String = "HTFluidIngredient[entryList=${text.string},amount=$amount]"
}
