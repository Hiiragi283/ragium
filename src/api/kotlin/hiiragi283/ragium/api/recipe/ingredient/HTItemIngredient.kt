package hiiragi283.ragium.api.recipe.ingredient

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.BiCodecs
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.util.unwrapEither
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import net.neoforged.neoforge.common.crafting.IngredientType
import net.neoforged.neoforge.registries.NeoForgeRegistries
import net.neoforged.neoforge.registries.holdersets.OrHolderSet
import java.util.function.IntUnaryOperator
import kotlin.jvm.optionals.getOrNull

/**
 * [ImmutableItemStack]向けの[HTIngredient]の実装クラス
 */
sealed class HTItemIngredient(protected val count: Int) : HTIngredient<Item, ImmutableItemStack> {
    fun interface CountGetter {
        fun getRequiredCount(stack: ImmutableItemStack): Int
    }

    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemIngredient> = BiCodecs
            .xor(HolderBased.CODEC, IngredientBased.CODEC)
            .xmap(::unwrapEither) { ingredient: HTItemIngredient ->
                when (ingredient) {
                    is HolderBased -> Either.left(ingredient)
                    is IngredientBased -> Either.right(ingredient)
                }
            }

        @JvmStatic
        fun of(holderSet: HolderSet<Item>, count: Int = 1): HTItemIngredient = HolderBased(holderSet, count)

        @JvmStatic
        fun of(ingredient: ICustomIngredient, count: Int = 1): HTItemIngredient = IngredientBased(ingredient, count)

        @JvmStatic
        fun convert(ingredient: Ingredient, count: Int = 1): HTItemIngredient {
            val custom: ICustomIngredient? = ingredient.customIngredient
            if (custom != null) {
                return of(custom, count)
            } else {
                val values: Array<out Ingredient.Value> = ingredient.values
                val holderSet: HolderSet<Item> = when (values.size) {
                    0 -> null
                    1 -> resolveValue(values[0])
                    else -> {
                        val holderSets: List<HolderSet<Item>> = values.mapNotNull(::resolveValue)
                        when {
                            holderSets.isEmpty() -> null
                            else -> OrHolderSet(holderSets)
                        }
                    }
                } ?: HolderSet.empty()
                return of(holderSet, count)
            }
        }

        @JvmStatic
        private fun resolveValue(value: Ingredient.Value): HolderSet<Item>? = when (value) {
            is Ingredient.TagValue -> BuiltInRegistries.ITEM.getTag(value.tag()).getOrNull()
            else -> HolderSet.direct(ItemStack::getItemHolder, value.items)
        }
    }

    fun test(stack: ItemStack): Boolean = stack.toImmutable()?.let(this::test) ?: false

    fun testOnlyType(stack: ItemStack): Boolean = stack.toImmutable()?.let(this::testOnlyType) ?: false

    final override fun test(stack: ImmutableItemStack): Boolean = testOnlyType(stack) && stack.amount() >= this.count

    final override fun getRequiredAmount(stack: ImmutableItemStack): Int = if (test(stack)) this.count else 0

    abstract fun copyWithCount(operator: IntUnaryOperator): HTItemIngredient

    //    HolderBased    //

    private class HolderBased(private val holderSet: HolderSet<Item>, count: Int) : HTItemIngredient(count) {
        companion object {
            @JvmStatic
            private val ENTRY_CODEC: BiCodec<RegistryFriendlyByteBuf, HolderSet<Item>> = VanillaBiCodecs.holderSet(Registries.ITEM)

            @JvmStatic
            private val FLAT_CODEC: BiCodec<RegistryFriendlyByteBuf, HolderBased> =
                ENTRY_CODEC.xmap(::HolderBased, HolderBased::holderSet)

            @JvmStatic
            private val NESTED_CODEC: BiCodec<RegistryFriendlyByteBuf, HolderBased> = BiCodec.composite(
                ENTRY_CODEC.fieldOf(RagiumConst.ITEMS).forGetter(HolderBased::holderSet),
                BiCodecs.POSITIVE_INT.optionalFieldOf(RagiumConst.AMOUNT, 1).forGetter(HolderBased::count),
                ::HolderBased,
            )

            @JvmField
            val CODEC: BiCodec<RegistryFriendlyByteBuf, HolderBased> = BiCodecs
                .xor(FLAT_CODEC, NESTED_CODEC)
                .xmap(::unwrapEither) { ingredient: HolderBased ->
                    when (ingredient.count) {
                        1 -> Either.left(ingredient)
                        else -> Either.right(ingredient)
                    }
                }
        }
        private constructor(holderSet: HolderSet<Item>) : this(holderSet, 1)

        override fun testOnlyType(stack: ImmutableItemStack): Boolean = stack.isOf(holderSet)

        override fun hasNoMatchingStacks(): Boolean = holderSet.none()

        override fun unwrap(): Either<Pair<HolderSet<Item>, Int>, List<ImmutableItemStack>> = Either.left(holderSet to count)

        override fun copyWithCount(operator: IntUnaryOperator): HTItemIngredient = HolderBased(holderSet, operator.applyAsInt(count))
    }

    private class IngredientBased(private val ingredient: ICustomIngredient, count: Int) : HTItemIngredient(count) {
        companion object {
            @JvmStatic
            private val CUSTOM_CODEC: BiCodec<RegistryFriendlyByteBuf, ICustomIngredient> =
                VanillaBiCodecs
                    .registryBased(NeoForgeRegistries.INGREDIENT_TYPES)
                    .dispatch(ICustomIngredient::getType, IngredientType<*>::codec, IngredientType<*>::streamCodec)

            @JvmField
            val CODEC: BiCodec<RegistryFriendlyByteBuf, IngredientBased> = BiCodec.composite(
                CUSTOM_CODEC.fieldOf(RagiumConst.INGREDIENT).forGetter(IngredientBased::ingredient),
                BiCodecs.POSITIVE_INT.optionalFieldOf(RagiumConst.AMOUNT, 1).forGetter(IngredientBased::count),
                ::IngredientBased,
            )
        }

        override fun testOnlyType(stack: ImmutableItemStack): Boolean = ingredient.test(stack.unwrap())

        override fun hasNoMatchingStacks(): Boolean = ingredient.items.findAny().isEmpty

        override fun unwrap(): Either<Pair<HolderSet<Item>, Int>, List<ImmutableItemStack>> =
            Either.right(ingredient.items.toList().mapNotNull(ItemStack::toImmutable))

        override fun copyWithCount(operator: IntUnaryOperator): HTItemIngredient = IngredientBased(ingredient, operator.applyAsInt(count))
    }
}
