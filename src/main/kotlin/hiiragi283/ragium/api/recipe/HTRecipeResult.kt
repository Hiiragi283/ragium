package hiiragi283.ragium.api.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.entryComparator
import hiiragi283.ragium.api.extension.entryPacketCodec
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
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.TagKey
import com.mojang.datafixers.util.Pair as MojPair
import net.minecraft.fluid.Fluid as MCFluid
import net.minecraft.item.Item as MCItem

sealed class HTRecipeResult<O : Any, V : Number, S : Any>(val registry: Registry<O>, val amount: V, val components: ComponentChanges) {
    abstract val isEmpty: Boolean

    abstract val stack: S

    abstract fun findFirstEntry(): RegistryEntry<O>

    fun findFirst(): O = findFirstEntry().value()

    abstract fun canMerge(other: S): Boolean

    abstract fun merge(other: S): S

    companion object {
        private val SIMPLE_ITEM_CODEC: Codec<ItemImpl> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    Registries.ITEM.entryCodec
                        .fieldOf("item")
                        .forGetter(ItemImpl::entry),
                    Codec
                        .intRange(1, Int.MAX_VALUE)
                        .optionalFieldOf("count", 1)
                        .forGetter(ItemImpl::amount),
                    ComponentChanges.CODEC
                        .optionalFieldOf("components", ComponentChanges.EMPTY)
                        .forGetter(ItemImpl::components),
                ).apply(instance, ::ItemImpl)
        }

        @JvmField
        val ITEM_CODEC: Codec<Item> = object : Codec<Item> {
            override fun <T : Any> encode(input: Item, ops: DynamicOps<T>, prefix: T): DataResult<T> = when (input) {
                is ItemImpl -> SIMPLE_ITEM_CODEC.encode(input, ops, prefix)
                is ItemTag -> DataResult.error { "Cannot encode ItemTag!" }
            }

            override fun <T : Any> decode(ops: DynamicOps<T>, input: T): DataResult<MojPair<Item, T>> = SIMPLE_ITEM_CODEC
                .decode(ops, input)
                .map { pair: MojPair<ItemImpl, T> -> pair.mapFirst { it } }
        }

        @JvmField
        val FLUID_CODEC: Codec<Fluid> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    Registries.FLUID.entryCodec
                        .fieldOf("fluid")
                        .forGetter(Fluid::entry),
                    longRangeCodec(1, Long.MAX_VALUE)
                        .optionalFieldOf("amount", FluidConstants.BUCKET)
                        .forGetter(Fluid::amount),
                ).apply(instance, ::Fluid)
        }

        @JvmField
        val ITEM_PACKET_CODEC: PacketCodec<RegistryByteBuf, Item> = PacketCodec.of(
            { entry: Item, buf: RegistryByteBuf ->
                when (entry) {
                    is ItemTag -> {
                        buf.writeBoolean(true)
                        buf.writeIdentifier(entry.tagKey.id)
                        buf.writeVarInt(entry.amount)
                    }

                    is ItemImpl -> {
                        buf.writeBoolean(false)
                        Registries.ITEM.entryPacketCodec.encode(buf, entry.entry)
                        buf.writeVarInt(entry.amount)
                        ComponentChanges.PACKET_CODEC.encode(buf, entry.components)
                    }
                }
            },
            { buf: RegistryByteBuf ->
                when (buf.readBoolean()) {
                    true -> {
                        val tagKey: TagKey<MCItem> = TagKey.of(RegistryKeys.ITEM, buf.readIdentifier())
                        val amount: Int = buf.readVarInt()
                        ItemTag(tagKey, amount)
                    }

                    false -> {
                        val entry: RegistryEntry<MCItem> = Registries.ITEM.entryPacketCodec.decode(buf)
                        val amount: Int = buf.readVarInt()
                        val components: ComponentChanges = ComponentChanges.PACKET_CODEC.decode(buf)
                        ItemImpl(entry, amount, components)
                    }
                }
            },
        )

        @JvmField
        val FLUID_PACKET_CODEC: PacketCodec<RegistryByteBuf, Fluid> = PacketCodec.tuple(
            PacketCodecs.registryCodec(Registries.FLUID.entryCodec),
            Fluid::entry,
            PacketCodecs.VAR_LONG,
            Fluid::amount,
            ::Fluid,
        )

        @JvmStatic
        fun ofItem(item: ItemConvertible, amount: Int = 1, components: ComponentChanges = ComponentChanges.EMPTY): Item =
            ItemImpl(item.asItem().registryEntry, amount, components)

        @JvmStatic
        fun ofItem(stack: ItemStack): Item = ItemImpl(stack.registryEntry, stack.count, stack.componentChanges)

        @JvmStatic
        fun ofItemTag(tagKey: TagKey<MCItem>, amount: Int = 1): Item = ItemTag(tagKey, amount)

        @JvmStatic
        fun ofFluid(fluid: MCFluid, amount: Long = FluidConstants.BUCKET): Fluid = Fluid(fluid.registryEntry, amount)
    }

    //    Item    //

    sealed class Item(amount: Int, components: ComponentChanges) :
        HTRecipeResult<MCItem, Int, ItemStack>(Registries.ITEM, amount, components) {
        final override val stack: ItemStack
            get() = ItemStack(findFirstEntry(), amount, components)
    }

    private class ItemImpl(val entry: RegistryEntry<MCItem>, amount: Int, components: ComponentChanges) : Item(amount, components) {
        override val isEmpty: Boolean = entry.isOf(Items.AIR) || amount <= 0

        override fun findFirstEntry(): RegistryEntry<MCItem> = entry

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

    //    ItemTag    //

    private class ItemTag(val tagKey: TagKey<MCItem>, amount: Int) : Item(amount, ComponentChanges.EMPTY) {
        override val isEmpty: Boolean = registry.getEntryList(tagKey).isEmpty || amount <= 0

        override fun findFirstEntry(): RegistryEntry<MCItem> = registry
            .getEntryList(tagKey)
            .orElseThrow { NoSuchElementException("TagKey; ${tagKey.id} has no entry!") }
            .sortedWith(entryComparator(registry))
            .first()

        override fun canMerge(other: ItemStack): Boolean = when {
            other.isEmpty -> true
            other.isIn(tagKey) -> true
            other.count + this.amount > other.maxCount -> false
            else -> false
        }

        override fun merge(other: ItemStack): ItemStack = when {
            !canMerge(other) -> other
            other.isEmpty -> stack
            other.count + this.amount > other.maxCount -> other.apply { count = other.maxCount }
            else -> other.apply { count += stack.count }
        }
    }

    //    Fluid    //

    class Fluid(val entry: RegistryEntry<MCFluid>, amount: Long) :
        HTRecipeResult<MCFluid, Long, Pair<MCFluid, Long>>(Registries.FLUID, amount, ComponentChanges.EMPTY) {
        val variant: FluidVariant = FluidVariant.of(findFirst())
            
        override val isEmpty: Boolean = entry.isOf(Fluids.EMPTY) || amount <= 0
        override val stack: Pair<MCFluid, Long>
            get() = entry.value() to amount

        override fun findFirstEntry(): RegistryEntry<MCFluid> = entry

        override fun canMerge(other: Pair<MCFluid, Long>): Boolean = when {
            other.first == Fluids.EMPTY || other.second <= 0 -> true
            else -> other.first == this.findFirst()
        }

        override fun merge(other: Pair<MCFluid, Long>): Pair<MCFluid, Long> = when {
            !canMerge(other) -> other
            other.first == Fluids.EMPTY || other.second <= 0 -> stack
            other.first == this.findFirst() -> other.first to (other.second + this.amount)
            else -> other
        }
    }
}
