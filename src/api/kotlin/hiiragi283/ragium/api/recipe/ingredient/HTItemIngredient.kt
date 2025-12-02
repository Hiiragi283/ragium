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
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import java.util.function.IntUnaryOperator

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
        fun of(ingredient: Ingredient, count: Int = 1): HTItemIngredient = IngredientBased(ingredient, count)
    }

    fun test(stack: ItemStack): Boolean = stack.toImmutable()?.let(this::test) ?: false

    fun testOnlyType(stack: ItemStack): Boolean = stack.toImmutable()?.let(this::testOnlyType) ?: false

    final override fun test(stack: ImmutableItemStack): Boolean = testOnlyType(stack) && stack.amount() >= this.count

    final override fun getRequiredAmount(stack: ImmutableItemStack): Int = if (test(stack)) this.count else 0

    fun copyWithCount(count: Int): HTItemIngredient = copyWithCount { count }

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

    private class IngredientBased(private val ingredient: Ingredient, count: Int) : HTItemIngredient(count) {
        companion object {
            @JvmField
            val CODEC: BiCodec<RegistryFriendlyByteBuf, IngredientBased> = BiCodec.composite(
                VanillaBiCodecs.INGREDIENT.fieldOf(RagiumConst.INGREDIENT).forGetter(IngredientBased::ingredient),
                BiCodecs.POSITIVE_INT.optionalFieldOf(RagiumConst.AMOUNT, 1).forGetter(IngredientBased::count),
                ::IngredientBased,
            )
        }

        override fun testOnlyType(stack: ImmutableItemStack): Boolean = ingredient.test(stack.unwrap())

        override fun hasNoMatchingStacks(): Boolean = ingredient.isEmpty

        override fun unwrap(): Either<Pair<HolderSet<Item>, Int>, List<ImmutableItemStack>> =
            Either.right(ingredient.items.mapNotNull(ItemStack::toImmutable))

        override fun copyWithCount(operator: IntUnaryOperator): HTItemIngredient = IngredientBased(ingredient, operator.applyAsInt(count))
    }
}
