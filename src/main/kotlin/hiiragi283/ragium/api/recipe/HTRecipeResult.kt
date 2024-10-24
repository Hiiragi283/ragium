package hiiragi283.ragium.api.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.*
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount
import net.minecraft.component.ComponentChanges
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registries
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.fluid.Fluid as MCFluid
import net.minecraft.item.Item as MCItem

sealed class HTRecipeResult<O : Any, V : Number, S : Any>(val entry: RegistryEntry<O>, val amount: V, val components: ComponentChanges) {
    companion object {
        @JvmStatic
        private fun <T : HTRecipeResult<*, *, *>> validate(result: T): DataResult<T> = when {
            result.isEmpty -> DataResult.error { "Could not encode empty ingredient!" }
            else -> DataResult.success(result)
        }

        @JvmField
        val ITEM_CODEC: Codec<Item> = RecordCodecBuilder
            .create { instance ->
                instance
                    .group(
                        Registries.ITEM.entryCodec
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
            }.validate(::validate)

        @JvmField
        val FLUID_CODEC: Codec<Fluid> = RecordCodecBuilder
            .create { instance ->
                instance
                    .group(
                        Registries.FLUID.entryCodec
                            .fieldOf("fluid")
                            .forGetter(Fluid::entry),
                        longRangeCodec(1, Long.MAX_VALUE)
                            .optionalFieldOf("amount", FluidConstants.BUCKET)
                            .forGetter(Fluid::amount),
                        ComponentChanges.CODEC
                            .optionalFieldOf("components", ComponentChanges.EMPTY)
                            .forGetter(Fluid::components),
                    ).apply(instance, ::Fluid)
            }.validate(::validate)

        @JvmField
        val ITEM_PACKET_CODEC: PacketCodec<RegistryByteBuf, Item> = PacketCodec
            .tuple(
                PacketCodecs.registryCodec(Registries.ITEM.entryCodec),
                Item::entry,
                PacketCodecs.VAR_INT,
                Item::amount,
                ComponentChanges.PACKET_CODEC,
                Item::components,
                ::Item,
            ).validate(::validate)

        @JvmField
        val FLUID_PACKET_CODEC: PacketCodec<RegistryByteBuf, Fluid> = PacketCodec
            .tuple(
                PacketCodecs.registryCodec(Registries.FLUID.entryCodec),
                Fluid::entry,
                PacketCodecs.VAR_LONG,
                Fluid::amount,
                ComponentChanges.PACKET_CODEC,
                Fluid::components,
                ::Fluid,
            ).validate(::validate)

        @JvmStatic
        fun ofItem(item: ItemConvertible, count: Int = 1, components: ComponentChanges = ComponentChanges.EMPTY): Item =
            Item(item.asItem().registryEntry, count, components)

        @JvmStatic
        fun ofItem(stack: ItemStack): Item = Item(stack.registryEntry, stack.count, stack.componentChanges)

        @JvmStatic
        fun ofFluid(fluid: MCFluid, amount: Long = FluidConstants.BUCKET, components: ComponentChanges = ComponentChanges.EMPTY): Fluid =
            Fluid(fluid.registryEntry, amount, components)
    }

    abstract val isEmpty: Boolean

    abstract fun canMerge(other: S): Boolean

    abstract fun merge(other: S): S

    //    Item    //

    class Item(entry: RegistryEntry<MCItem>, amount: Int, components: ComponentChanges) :
        HTRecipeResult<MCItem, Int, ItemStack>(
            entry,
            amount,
            components,
        ) {
        val stack: ItemStack
            get() = ItemStack(entry, amount, components)

        override val isEmpty: Boolean
            get() = entry.value() == Items.AIR || amount <= 0

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
    }

    //    Fluid    //

    class Fluid(entry: RegistryEntry<MCFluid>, amount: Long, components: ComponentChanges) :
        HTRecipeResult<MCFluid, Long, ResourceAmount<FluidVariant>>(
            entry,
            amount,
            components,
        ) {
        val resourceAmount: ResourceAmount<FluidVariant> =
            ResourceAmount(FluidVariant.of(entry.value(), components), amount)

        override val isEmpty: Boolean
            get() = entry.value() == Fluids.EMPTY || amount <= 0

        override fun canMerge(other: ResourceAmount<FluidVariant>): Boolean = when {
            other.isBlank() -> true
            else -> other.equalsResource(resourceAmount)
        }

        override fun merge(other: ResourceAmount<FluidVariant>): ResourceAmount<FluidVariant> = when {
            !canMerge(other) -> other
            other.isBlank() -> resourceAmount
            other.equalsResource(resourceAmount) -> other + this.amount
            else -> other
        }
    }
}
