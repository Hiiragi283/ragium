package hiiragi283.ragium.api.recipe

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.*
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryCodecs
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.entry.RegistryEntryList
import net.minecraft.registry.tag.TagKey
import net.minecraft.text.Text
import net.minecraft.util.StringIdentifiable
import java.util.function.BiPredicate
import java.util.function.Predicate
import kotlin.collections.component1
import kotlin.collections.component2
import net.minecraft.fluid.Fluid as MCFluid
import net.minecraft.item.Item as MCItem

@Suppress("DEPRECATION")
sealed class HTIngredient<O : Any, V : Number>(protected val entryList: RegistryEntryList<O>, val amount: V) {
    abstract val isEmpty: Boolean

    fun <T : Any> map(transform: (RegistryEntry<O>, V) -> T): List<T> = entryList.map { transform(it, amount) }

    val storage: Either<TagKey<O>, List<RegistryEntry<O>>> = entryList.storage

    val firstEntry: RegistryEntry<O>?
        get() = entryList.firstOrNull()

    val entryMap: Map<RegistryEntry<O>, V> = entryList.associateWith { amount }

    val valueMap: Map<O, V>
        get() = entryMap.mapKeys { it.key.value() }

    abstract val entryText: Text

    override fun toString(): String = "HTIngredient[entryList=${entryText.string},amount=$amount]"

    companion object {
        @JvmStatic
        private fun <T : HTIngredient<*, *>> validate(ingredient: T): DataResult<T> = when {
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
                        ConsumeType.CODEC
                            .optionalFieldOf("consume_type", ConsumeType.DECREMENT)
                            .forGetter(Item::consumeType),
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
                ConsumeType.PACKET_CODEC,
                Item::consumeType,
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

        @JvmStatic
        fun ofItem(item: ItemConvertible, count: Int = 1, consumeType: ConsumeType = ConsumeType.DECREMENT): Item =
            Item(RegistryEntryList.of(item.asItem().registryEntry), count, consumeType)

        @JvmStatic
        fun ofItem(tagKey: TagKey<MCItem>, count: Int = 1, consumeType: ConsumeType = ConsumeType.DECREMENT): Item =
            Item(Registries.ITEM.getOrCreateEntryList(tagKey), count, consumeType)

        @JvmStatic
        fun ofFluid(fluid: MCFluid, amount: Long = FluidConstants.BUCKET): Fluid = Fluid(RegistryEntryList.of(fluid.registryEntry), amount)

        @JvmStatic
        fun ofFluid(tagKey: TagKey<MCFluid>, amount: Long = FluidConstants.BUCKET): Fluid =
            Fluid(Registries.FLUID.getOrCreateEntryList(tagKey), amount)
    }

    //    Item    //

    class Item(entryList: RegistryEntryList<MCItem>, amount: Int, val consumeType: ConsumeType) :
        HTIngredient<MCItem, Int>(entryList, amount),
        Predicate<ItemStack> {
        val matchingStacks: List<ItemStack>
            get() = entryMap.map { (entry: RegistryEntry<MCItem>, count: Int) ->
                ItemStack(
                    entry,
                    count,
                )
            }

        override val isEmpty: Boolean =
            entryList.storage.map(
                { false },
                { list: List<RegistryEntry<MCItem>> -> list.isEmpty() || list.any { it.isOf(Items.AIR) } },
            ) ||
                amount <= 0

        override val entryText: Text
            get() = entryList.asText(MCItem::getName)

        override fun test(stack: ItemStack): Boolean = when (stack.isEmpty) {
            true -> this.isEmpty
            false -> stack.isIn(entryList) && stack.count >= this.amount
        }

        fun onConsume(stack: ItemStack) {
            when (consumeType) {
                ConsumeType.DECREMENT -> stack.decrement(amount)
                ConsumeType.DAMAGE -> {
                    val damage: Int = stack.damage
                    val maxDamage: Int = stack.maxDamage
                    if (damage + 1 <= maxDamage) {
                        stack.damage += amount
                    } else {
                        stack.decrement(1)
                    }
                }
            }
        }
    }

    //    Fluid    //

    class Fluid(entryList: RegistryEntryList<MCFluid>, amount: Long) :
        HTIngredient<MCFluid, Long>(entryList, amount),
        BiPredicate<MCFluid, Long> {
        override val isEmpty: Boolean =
            entryList.storage.map(
                { false },
                { list: List<RegistryEntry<MCFluid>> -> list.isEmpty() || list.any { it.isOf(Fluids.EMPTY) } },
            ) ||
                amount <= 0

        override val entryText: Text
            get() = entryList.asText(MCFluid::name)

        fun test(resource: ResourceAmount<FluidVariant>): Boolean = test(resource.resource.fluid, resource.amount)

        override fun test(fluid: MCFluid, amount: Long): Boolean = when {
            fluid == Fluids.EMPTY || amount <= 0 -> this.isEmpty
            else -> fluid in entryList && amount >= this.amount
        }

        fun onConsume(storage: SingleSlotStorage<FluidVariant>) {
            useTransaction { transaction: Transaction ->
                val foundVariant: FluidVariant = StorageUtil.findExtractableResource(
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
    }

    //    ConsumeType    //

    enum class ConsumeType : StringIdentifiable {
        DECREMENT,
        DAMAGE,
        ;

        companion object {
            @JvmField
            val CODEC: Codec<ConsumeType> = codecOf(entries)

            @JvmField
            val PACKET_CODEC: PacketCodec<RegistryByteBuf, ConsumeType> = packetCodecOf(entries)
        }

        override fun asString(): String = name.lowercase()
    }
}
