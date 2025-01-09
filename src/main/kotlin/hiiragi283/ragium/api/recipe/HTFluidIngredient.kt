package hiiragi283.ragium.api.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.codec.HTRegistryEntryListCodec
import hiiragi283.ragium.api.codec.HTRegistryEntryPacketCodec
import hiiragi283.ragium.api.codec.RagiumCodecs
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.storage.HTFluidVariantStack
import hiiragi283.ragium.api.util.HTRegistryEntryList
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.fluid.Fluid
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registries
import net.minecraft.registry.tag.TagKey
import net.minecraft.text.MutableText
import java.util.*
import java.util.function.Predicate

/**
 * 液体を扱う材料のクラス
 * @param entryList 条件に一致する液体の一覧
 * @param amount 条件に一致する液体の量
 */
class HTFluidIngredient private constructor(private val entryList: HTRegistryEntryList<Fluid>, val amount: Long = FluidConstants.BUCKET) :
    Predicate<HTFluidVariantStack> {
        companion object {
            @JvmStatic
            private val FLUID_CODEC: HTRegistryEntryListCodec<Fluid> = HTRegistryEntryListCodec(Registries.FLUID)

            @JvmField
            val CODEC: Codec<HTFluidIngredient> = RagiumCodecs.simpleOrComplex(
                FLUID_CODEC.xmap(::HTFluidIngredient, HTFluidIngredient::entryList),
                RecordCodecBuilder.create { instance ->
                    instance
                        .group(
                            FLUID_CODEC
                                .fieldOf("fluids")
                                .forGetter(HTFluidIngredient::entryList),
                            POSITIVE_LONG_CODEC
                                .optionalFieldOf("amount", FluidConstants.BUCKET)
                                .forGetter(HTFluidIngredient::amount),
                        ).apply(instance, ::HTFluidIngredient)
                },
            ) { ingredient: HTFluidIngredient -> ingredient.amount == FluidConstants.BUCKET }

            @JvmField
            val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTFluidIngredient> = PacketCodec
                .tuple(
                    HTRegistryEntryPacketCodec(Registries.FLUID),
                    HTFluidIngredient::entryList,
                    PacketCodecs.VAR_LONG,
                    HTFluidIngredient::amount,
                    ::HTFluidIngredient,
                )

            /**
             * 指定した[fluid]と[amount]から[HTFluidIngredient]を返します。
             */
            @JvmStatic
            fun of(fluid: Fluid, amount: Long = FluidConstants.BUCKET): HTFluidIngredient =
                HTFluidIngredient(HTRegistryEntryList.direct(fluid), amount)

            /**
             * 指定した[fluids]と[amount]から[HTFluidIngredient]を返します。
             */
            @JvmStatic
            fun of(fluids: List<Fluid>, amount: Long = FluidConstants.BUCKET): HTFluidIngredient =
                HTFluidIngredient(HTRegistryEntryList.direct(fluids), amount)

            /**
             * 指定した[tagKey]と[amount]から[HTFluidIngredient]を返します。
             */
            @JvmStatic
            fun of(tagKey: TagKey<Fluid>, amount: Long = FluidConstants.BUCKET): HTFluidIngredient =
                HTFluidIngredient(HTRegistryEntryList.fromTag(tagKey, Registries.FLUID), amount)
        }

        val isEmpty: Boolean
            get() = entryList.storage.map({ false }, { it.isEmpty() || it.all(Fluid::isEmpty) }) || amount <= 0

        fun <T : Any> map(transform: (Fluid, Long) -> T): List<T> = entryList.map { transform(it, amount) }

        val text: MutableText
            get() = entryList.asText(Fluid::name)

        override fun test(stack: HTFluidVariantStack): Boolean = when (stack.isEmpty) {
            true -> this.isEmpty
            false -> stack.fluid in entryList && stack.amount >= this.amount
        }

        fun test(fluid: Fluid, amount: Long): Boolean = test(HTFluidVariantStack(fluid, amount))

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
                    if (extracted == amount) transaction.commit()
                }
            }
        }

        override fun equals(other: Any?): Boolean = (other as? HTFluidIngredient)
            ?.let { it.entryList == this.entryList && it.amount == this.amount } == true

        override fun hashCode(): Int = Objects.hash(entryList, amount)

        override fun toString(): String = "HTFluidIngredient[entryList=${text.string},amount=$amount]"
    }
