package hiiragi283.ragium.api.recipe

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.*
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryCodecs
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.entry.RegistryEntryList
import net.minecraft.registry.tag.TagKey
import java.util.function.Predicate
import net.minecraft.fluid.Fluid as MCFluid
import net.minecraft.item.Item as MCItem

sealed class HTIngredient<O : Any, V : Number, S : Any>(protected val entryList: RegistryEntryList<O>, val amount: V) : Predicate<S> {
    val isEmpty: Boolean
        get() = (entryList !is RegistryEntryList.Named<O> && entryList.size() == 0) || amount.toInt() <= 0

    val firstEntry: RegistryEntry<O>?
        get() = entryList.firstOrNull()

    val entryMap: Map<RegistryEntry<O>, V>
        get() = entryList.associateWith { amount }

    override fun toString(): String = "HTIngredient[entryList=$entryList,amount=$amount]"

    companion object {
        @JvmStatic
        private fun <T : HTIngredient<*, *, *>> validate(ingredient: T): DataResult<T> = when {
            ingredient.isEmpty -> DataResult.error { "Could not encode empty ingredient!" }
            else -> DataResult.success(ingredient)
        }

        @JvmField
        val ITEM_CODEC: Codec<Item> = RecordCodecBuilder
            .create { instance ->
                instance
                    .group(
                        RegistryCodecs
                            .entryList(RegistryKeys.ITEM)
                            .fieldOf("items")
                            .forGetter(Item::entryList),
                        Codec
                            .intRange(1, Int.MAX_VALUE)
                            .optionalFieldOf("count", 1)
                            .forGetter(Item::amount),
                    ).apply(instance, ::Item)
            }.validate(::validate)

        @JvmField
        val FLUID_CODEC: Codec<Fluid> = RecordCodecBuilder
            .create { instance ->
                instance
                    .group(
                        RegistryCodecs
                            .entryList(RegistryKeys.FLUID)
                            .fieldOf("fluids")
                            .forGetter(Fluid::entryList),
                        longRangeCodec(1, Long.MAX_VALUE)
                            .optionalFieldOf("amount", FluidConstants.BUCKET)
                            .forGetter(Fluid::amount),
                    ).apply(instance, ::Fluid)
            }.validate(::validate)

        @JvmField
        val ITEM_PACKET_CODEC: PacketCodec<RegistryByteBuf, Item> = PacketCodec
            .tuple(
                PacketCodecs.registryEntryList(RegistryKeys.ITEM),
                Item::entryList,
                PacketCodecs.VAR_INT,
                Item::amount,
                ::Item,
            ).validate(::validate)

        @JvmField
        val FLUID_PACKET_CODEC: PacketCodec<RegistryByteBuf, Fluid> = PacketCodec
            .tuple(
                PacketCodecs.registryEntryList(RegistryKeys.FLUID),
                Fluid::entryList,
                PacketCodecs.VAR_LONG,
                Fluid::amount,
                ::Fluid,
            ).validate(::validate)

        // val EMPTY_ITEM = Item(RegistryEntryList.empty(), 0)

        // val EMPTY_FLUID = Fluid(RegistryEntryList.empty(), 0)

        @JvmStatic
        fun ofItem(item: ItemConvertible, count: Int = 1): Item = Item(RegistryEntryList.of(item.asItem().registryEntry), count)

        @JvmStatic
        fun ofItem(tagKey: TagKey<MCItem>, count: Int = 1): Item = Item(Registries.ITEM.getOrCreateEntryList(tagKey), count)

        @JvmStatic
        fun ofItem(either: Either<TagKey<MCItem>, ItemConvertible>, count: Int = 1): Item = Item(
            either.map(Registries.ITEM::getOrCreateEntryList) { RegistryEntryList.of(it.asItem().registryEntry) },
            count,
        )

        @JvmStatic
        fun ofFluid(fluid: MCFluid, amount: Long = FluidConstants.BUCKET): Fluid = Fluid(RegistryEntryList.of(fluid.registryEntry), amount)

        @JvmStatic
        fun ofFluid(tagKey: TagKey<MCFluid>, amount: Long = FluidConstants.BUCKET): Fluid =
            Fluid(Registries.FLUID.getOrCreateEntryList(tagKey), amount)
    }

    //    Item    //

    class Item(entryList: RegistryEntryList<MCItem>, amount: Int) : HTIngredient<MCItem, Int, ItemStack>(entryList, amount) {
        val matchingStacks: List<ItemStack>
            get() = entryMap.map { (entry: RegistryEntry<net.minecraft.item.Item>, count: Int) ->
                ItemStack(
                    entry,
                    count,
                )
            }

        override fun test(stack: ItemStack): Boolean = when (stack.isEmpty) {
            true -> this.isEmpty
            false -> stack.isIn(entryList) && stack.count >= this.amount
        }
    }

    //    Fluid    //

    class Fluid(entryList: RegistryEntryList<MCFluid>, amount: Long) :
        HTIngredient<MCFluid, Long, ResourceAmount<FluidVariant>>(entryList, amount) {
        override fun test(resourceAmount: ResourceAmount<FluidVariant>): Boolean {
            val (variant: FluidVariant, amount: Long) = resourceAmount
            return when {
                resourceAmount.isBlank() -> this.isEmpty
                else -> entryList.any(variant::isOf) && amount >= this.amount
            }
        }
    }
}
