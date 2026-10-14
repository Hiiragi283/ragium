package hiiragi283.lib.recipe.ingredient

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.registry.asHolderSequence
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.util.DFUEither
import hiiragi283.ragium.api.data.chemical.HTChemical
import hiiragi283.ragium.api.data.map.RagiumDataMaps
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.ExtraCodecs
import net.minecraft.util.context.ContextMap
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.display.DisplayContentsFactory
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

sealed interface HTItemIngredient :
    HTIngredient<Item, ItemInstance>,
    HTStackPreview<ItemStack> {
    companion object {
        @JvmField
        val MAP_CODEC: MapCodec<HTItemIngredient> = Codec.mapEither(Simple.CODEC, Chemical.CODEC).xmap(
            { DFUEither.unwrap(it) },
            {
                when (it) {
                    is Chemical -> DFUEither.right(it)
                    is Simple -> DFUEither.left(it)
                }
            }
        )

        @JvmField
        val CODEC: Codec<HTItemIngredient> = Codec.lazyInitialized(MAP_CODEC::codec)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTItemIngredient> = ByteBufCodecs.either(
            Simple.STREAM_CODEC,
            Chemical.STREAM_CODEC
        ).map(
            { DFUEither.unwrap(it) },
            {
                when (it) {
                    is Chemical -> DFUEither.right(it)
                    is Simple -> DFUEither.left(it)
                }
            }
        )
    }

    @JvmRecord
    data class Simple(val unsized: Ingredient, val count: Int) : HTItemIngredient {
        companion object {
            @JvmField
            val CODEC = HTCodecs.recordMap { instance ->
                instance.group(
                    Ingredient.CODEC.fieldOf(HTConstants.ITEMS).forGetter(Simple::unsized),
                    HTCodecs.POSITIVE_INT.fieldOf(HTConstants.COUNT).forGetter(Simple::count)
                ).apply(instance, ::Simple)
            }

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Simple> = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC,
                Simple::unsized,
                ByteBufCodecs.VAR_INT,
                Simple::count,
                ::Simple
            )
        }

        override fun test(instance: ItemInstance): Boolean = testOnlyType(instance) && instance.count() >= count

        override fun testOnlyType(instance: ItemInstance): Boolean =
            HTIngredientHelper.unwrap(instance).let(unsized::test)

        override fun getMatchingStack(instance: ItemInstance): ItemInstance = when (testOnlyType(instance)) {
            true -> HTIngredientHelper.copyWithCount(instance, count)
            false -> ItemStack.EMPTY
        }

        override fun getPreviewStacks(contextMap: ContextMap): List<ItemStack> = unsized
            .display()
            .resolve(contextMap, DisplayContentsFactory.ForStacks { it.copyWithCount(count) })
            .toList()
    }

    @JvmRecord
    data class Chemical(val chemicals: HolderSet<HTChemical>, val scale: Float) : HTItemIngredient {
        companion object {
            @JvmField
            val CODEC: MapCodec<Chemical> = HTCodecs.recordMap { instance ->
                instance.group(
                    HTChemical.HOLDER_SET_CODEC.fieldOf("chemicals").forGetter(Chemical::chemicals),
                    ExtraCodecs.POSITIVE_FLOAT.fieldOf("scale").forGetter(Chemical::scale)
                ).apply(instance, ::Chemical)
            }

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Chemical> = StreamCodec.composite(
                HTChemical.HOLDER_SET_STREAM_CODEC,
                Chemical::chemicals,
                ByteBufCodecs.FLOAT,
                Chemical::scale,
                ::Chemical
            )
        }

        override fun test(instance: ItemInstance): Boolean {
            val (chemical: Holder<HTChemical>, scale: Float) = instance.getData(RagiumDataMaps.CHEMICAL) ?: return false
            return chemical in chemicals && scale * instance.count() >= this.scale
        }

        override fun testOnlyType(instance: ItemInstance): Boolean {
            val (chemical: Holder<HTChemical>, _) = instance.getData(RagiumDataMaps.CHEMICAL) ?: return false
            return chemical in chemicals
        }

        override fun getMatchingStack(instance: ItemInstance): ItemInstance {
            val (chemical: Holder<HTChemical>, scale: Float) =
                instance.getData(RagiumDataMaps.CHEMICAL) ?: return ItemStack.EMPTY
            if (chemical in chemicals) {
                val multiplier: Int = (this.scale / scale).toInt()
                if (multiplier > 0) {
                    return HTIngredientHelper.copyWithCount(instance, multiplier)
                }
            }
            return ItemStack.EMPTY
        }

        override fun getPreviewStacks(contextMap: ContextMap): List<ItemStack> =
            BuiltInRegistries.ITEM.asHolderSequence().mapNotNull { holder: Holder<Item> ->
                val (chemical: Holder<HTChemical>, scale: Float) =
                    holder.getData(RagiumDataMaps.CHEMICAL) ?: return@mapNotNull null
                if (chemical in chemicals) {
                    val multiplier: Int = (this.scale / scale).toInt()
                    if (multiplier > 0) {
                        return@mapNotNull ItemStack(holder, multiplier)
                    }
                }
                null
            }.toList()
    }
}

//    Extensions    //

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.11
 */
fun HTItemIngredient(unsized: Ingredient, count: Int): HTItemIngredient = HTItemIngredient.Simple(unsized, count)

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.11
 */
fun HTItemIngredient(chemicals: HolderSet<HTChemical>, scale: Float): HTItemIngredient =
    HTItemIngredient.Chemical(chemicals, scale)

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.11
 */
fun HTItemIngredient(chemical: Holder<HTChemical>, scale: Float): HTItemIngredient =
    HTItemIngredient(HolderSet.direct(chemical), scale)

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.11
 */
fun HTItemIngredient(chemicals: HolderSet<HTChemical>, scale: Int): HTItemIngredient =
    HTItemIngredient(chemicals, scale.toFloat())

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.11
 */
fun HTItemIngredient(chemical: Holder<HTChemical>, scale: Int): HTItemIngredient =
    HTItemIngredient(chemical, scale.toFloat())

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
fun Optional<HTItemIngredient>.test(instance: ItemInstance): Boolean = this.getOrNull().testOrEmpty(instance)

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.9
 */
fun HTItemIngredient?.testOrEmpty(instance: ItemInstance): Boolean =
    this?.test(instance) ?: HTIngredientHelper.isEmpty(instance)
