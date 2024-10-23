package hiiragi283.ragium.api.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.*
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.entry.RegistryEntryList
import net.minecraft.registry.tag.TagKey
import java.util.function.BiPredicate

typealias HTItemIngredient = HTIngredient<Item, ItemVariant>
typealias HTFluidIngredient = HTIngredient<Fluid, FluidVariant>

@Suppress("DEPRECATION")
class HTIngredient<O : Any, T : TransferVariant<O>> private constructor(private val entryList: RegistryEntryList<O>, val amount: Long) :
    BiPredicate<T, Long> {
        companion object {
            @JvmField
            val EMPTY_ITEM: HTIngredient<Item, ItemVariant> = HTIngredient(RegistryEntryList.empty(), 0)

            @JvmField
            val EMPTY_FLUID: HTIngredient<Fluid, FluidVariant> = HTIngredient(RegistryEntryList.empty(), 0)

            @JvmStatic
            private fun <O : Any, T : TransferVariant<O>> createCodec(
                objCodec: MapCodec<RegistryEntryList<O>>,
                defaultAmount: Long,
                toIngredient: (RegistryEntryList<O>, Long) -> HTIngredient<O, T>,
            ): Codec<HTIngredient<O, T>> = RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        objCodec.forGetter(HTIngredient<O, T>::entryList),
                        Codec.LONG.optionalFieldOf("amount", defaultAmount).forGetter(HTIngredient<O, T>::amount),
                    ).apply(instance, toIngredient)
            }

            @JvmField
            val ITEM_CODEC: Codec<HTIngredient<Item, ItemVariant>> =
                createCodec(
                    RegistryCodecs.entryList(RegistryKeys.ITEM).fieldOf("items"),
                    1,
                    ::ofItem,
                )

            @JvmField
            val FLUID_CODEC: Codec<HTIngredient<Fluid, FluidVariant>> =
                createCodec(
                    RegistryCodecs.entryList(RegistryKeys.FLUID).fieldOf("fluids"),
                    FluidConstants.BUCKET,
                    ::ofFluid,
                )

            @JvmStatic
            private fun <O : Any, T : TransferVariant<O>> createPacketCodec(
                registryKey: RegistryKey<Registry<O>>,
                toIngredient: (RegistryEntryList<O>, Long) -> HTIngredient<O, T>,
            ): PacketCodec<RegistryByteBuf, HTIngredient<O, T>> = PacketCodec.tuple(
                PacketCodecs.registryEntryList(registryKey),
                HTIngredient<O, T>::entryList,
                PacketCodecs.VAR_LONG,
                HTIngredient<O, T>::amount,
                toIngredient,
            )

            @JvmField
            val ITEM_PACKET_CODEC: PacketCodec<RegistryByteBuf, HTIngredient<Item, ItemVariant>> =
                createPacketCodec(RegistryKeys.ITEM, ::ofItem)

            @JvmField
            val FLUID_PACKET_CODEC: PacketCodec<RegistryByteBuf, HTIngredient<Fluid, FluidVariant>> =
                createPacketCodec(RegistryKeys.FLUID, ::ofFluid)

            @JvmStatic
            private fun ofItem(entryList: RegistryEntryList<Item>, amount: Long): HTIngredient<Item, ItemVariant> =
                HTIngredient(entryList, amount)

            @JvmStatic
            fun ofItem(item: ItemConvertible, amount: Long = 1): HTIngredient<Item, ItemVariant> =
                ofItem(RegistryEntryList.of(item.asItem().registryEntry), amount)

            @JvmStatic
            fun ofItem(tagKey: TagKey<Item>, amount: Long = 1): HTIngredient<Item, ItemVariant> =
                ofItem(Registries.ITEM.getOrCreateEntryList(tagKey), amount)

            @JvmStatic
            private fun ofFluid(entryList: RegistryEntryList<Fluid>, amount: Long): HTIngredient<Fluid, FluidVariant> =
                HTIngredient(entryList, amount)

            @JvmStatic
            fun ofFluid(fluid: Fluid, amount: Long = FluidConstants.BUCKET): HTIngredient<Fluid, FluidVariant> =
                ofFluid(RegistryEntryList.of(fluid.registryEntry), amount)

            @JvmStatic
            fun ofFluid(tagKey: TagKey<Fluid>, amount: Long = FluidConstants.BUCKET): HTIngredient<Fluid, FluidVariant> =
                ofFluid(Registries.FLUID.getOrCreateEntryList(tagKey), amount)
        }

        val isEmpty: Boolean
            get() = entryList.size() == 0 || amount <= 0

        val firstEntry: RegistryEntry<O>?
            get() = entryList.firstOrNull()

        val weightedList: List<Pair<O, Long>>
            get() = entryList.map { it.value() to amount }

        override fun toString(): String = "HTIngredient[entryList=$entryList,amount=$amount]"

        //    BiPredicate    //

        override fun test(variant: T, amount: Long): Boolean = when (isEmpty) {
            true -> variant.isBlank
            false -> entryList.any { variant.isOf(it.value()) } && amount >= this.amount
        }
    }
