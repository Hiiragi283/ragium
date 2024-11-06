package hiiragi283.ragium.api.recipe

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.entryComparator
import hiiragi283.ragium.api.extension.isOf
import hiiragi283.ragium.api.extension.longRangeCodec
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.minecraft.component.ComponentChanges
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.TagKey
import net.minecraft.fluid.Fluid as MCFluid
import net.minecraft.item.Item as MCItem

@Suppress("DEPRECATION")
sealed class HTRecipeResult<O : Any, V : Number, S : Any>(
    val entry: Either<RegistryEntry<O>, TagKey<O>>,
    val amount: V,
    val components: ComponentChanges,
) {
    companion object {
        @JvmStatic
        private fun <T : HTRecipeResult<*, *, *>> validate(result: T): DataResult<T> = when {
            result.isEmpty -> DataResult.error { "Could not encode empty ingredient!" }
            else -> DataResult.success(result)
        }

        @JvmStatic
        fun <T : Any> entryCodec(registry: Registry<T>): Codec<Either<RegistryEntry<T>, TagKey<T>>> =
            Codec.xor(registry.entryCodec, TagKey.codec(registry.key))

        @JvmField
        val ITEM_ENTRY_CODEC: Codec<Either<RegistryEntry<MCItem>, TagKey<MCItem>>> = entryCodec(Registries.ITEM)

        @JvmField
        val FLUID_ENTRY_CODEC: Codec<Either<RegistryEntry<MCFluid>, TagKey<MCFluid>>> = entryCodec(Registries.FLUID)

        @JvmField
        val ITEM_CODEC: Codec<Item> = RecordCodecBuilder
            .create { instance ->
                instance
                    .group(
                        ITEM_ENTRY_CODEC
                            .fieldOf("item")
                            .forGetter(Item::entry),
                        Codec
                            .intRange(1, Int.MAX_VALUE)
                            .optionalFieldOf("count", 1)
                            .forGetter(Item::amount),
                        ComponentChanges.CODEC
                            .optionalFieldOf("components", ComponentChanges.EMPTY)
                            .forGetter(Item::components),
                    ).apply(instance, ::Item)
            }

        @JvmField
        val FLUID_CODEC: Codec<Fluid> = RecordCodecBuilder
            .create { instance ->
                instance
                    .group(
                        FLUID_ENTRY_CODEC
                            .fieldOf("fluid")
                            .forGetter(Fluid::entry),
                        longRangeCodec(1, Long.MAX_VALUE)
                            .optionalFieldOf("amount", FluidConstants.BUCKET)
                            .forGetter(Fluid::amount),
                        ComponentChanges.CODEC
                            .optionalFieldOf("components", ComponentChanges.EMPTY)
                            .forGetter(Fluid::components),
                    ).apply(instance, ::Fluid)
            }

        @JvmField
        val ITEM_PACKET_CODEC: PacketCodec<RegistryByteBuf, Item> = PacketCodec
            .tuple(
                PacketCodecs.registryCodec(ITEM_ENTRY_CODEC),
                Item::entry,
                PacketCodecs.VAR_INT,
                Item::amount,
                ComponentChanges.PACKET_CODEC,
                Item::components,
                ::Item,
            )

        @JvmField
        val FLUID_PACKET_CODEC: PacketCodec<RegistryByteBuf, Fluid> = PacketCodec
            .tuple(
                PacketCodecs.registryCodec(FLUID_ENTRY_CODEC),
                Fluid::entry,
                PacketCodecs.VAR_LONG,
                Fluid::amount,
                ComponentChanges.PACKET_CODEC,
                Fluid::components,
                ::Fluid,
            )

        @JvmStatic
        fun ofItem(item: ItemConvertible, count: Int = 1, components: ComponentChanges = ComponentChanges.EMPTY): Item =
            Item(Either.left(item.asItem().registryEntry), count, components)

        @JvmStatic
        fun ofItem(stack: ItemStack): Item = Item(Either.left(stack.registryEntry), stack.count, stack.componentChanges)

        @Deprecated(
            "Experimental Feature",
            ReplaceWith(
                "Item(Either.right(tagKey), count, components)",
                "hiiragi283.ragium.api.recipe.HTRecipeResult.Item",
                "com.mojang.datafixers.util.Either",
            ),
        )
        @JvmStatic
        fun ofDynamic(tagKey: TagKey<MCItem>, count: Int = 1, components: ComponentChanges = ComponentChanges.EMPTY): Item =
            Item(Either.right(tagKey), count, components)

        @JvmStatic
        fun ofFluid(fluid: MCFluid, amount: Long = FluidConstants.BUCKET, components: ComponentChanges = ComponentChanges.EMPTY): Fluid =
            Fluid(Either.left(fluid.registryEntry), amount, components)
    }

    abstract val isEmpty: Boolean

    abstract fun canMerge(other: S): Boolean

    abstract fun merge(other: S): S

    abstract val registry: Registry<O>

    fun findFirstEntry(): RegistryEntry<O> = entry.left().orElseGet {
        entry
            .right()
            .flatMap(registry::getEntryList)
            .orElseThrow { NoSuchElementException("TagKey; $entry has no entry!") }
            .sortedWith(entryComparator(registry))
            .first()
    }

    fun findFirst(): O = findFirstEntry().value()

    //    Item    //

    class Item(entry: Either<RegistryEntry<MCItem>, TagKey<MCItem>>, amount: Int, components: ComponentChanges) :
        HTRecipeResult<MCItem, Int, ItemStack>(
            entry,
            amount,
            components,
        ) {
        val stack: ItemStack
            get() = ItemStack(findFirstEntry(), amount, components)

        override val isEmpty: Boolean
            get() = entry.map(
                { it.isOf(Items.AIR) },
                { Registries.ITEM.iterateEntries(it).firstOrNull() == null },
            ) ||
                amount <= 0

        override fun canMerge(other: ItemStack): Boolean = when {
            other.isEmpty -> true
            ItemStack.areItemsAndComponentsEqual(stack, other) -> true
            other.count + this.amount > other.maxCount -> false
            else -> false
        }

        override fun merge(other: ItemStack): ItemStack = when {
            !canMerge(other) -> other
            other.isEmpty -> stack
            other.count + this.amount > other.maxCount -> other.apply { count = other.maxCount }
            ItemStack.areItemsAndComponentsEqual(stack, other) -> other.apply { count += stack.count }
            else -> other
        }

        override val registry: Registry<MCItem>
            get() = Registries.ITEM
    }

    //    Fluid    //

    class Fluid(entry: Either<RegistryEntry<MCFluid>, TagKey<MCFluid>>, amount: Long, components: ComponentChanges) :
        HTRecipeResult<MCFluid, Long, Pair<MCFluid, Long>>(
            entry,
            amount,
            components,
        ) {
        val variant: FluidVariant
            get() = FluidVariant.of(findFirst())

        val amounted: Pair<MCFluid, Long>
            get() = findFirst() to amount

        override val isEmpty: Boolean
            get() = entry.map(
                { it.isOf(Fluids.EMPTY) },
                { Registries.FLUID.iterateEntries(it).firstOrNull() == null },
            ) ||
                amount <= 0

        override fun canMerge(other: Pair<MCFluid, Long>): Boolean = when {
            other.first == Fluids.EMPTY || other.second <= 0 -> true
            else -> other.first == this.findFirst()
        }

        override fun merge(other: Pair<MCFluid, Long>): Pair<MCFluid, Long> = when {
            !canMerge(other) -> other
            other.first == Fluids.EMPTY || other.second <= 0 -> amounted
            other.first == this.findFirst() -> other.first to (other.second + this.amount)
            else -> other
        }

        override val registry: Registry<MCFluid>
            get() = Registries.FLUID
    }
}
