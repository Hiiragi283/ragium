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
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import java.util.function.IntUnaryOperator
import kotlin.jvm.optionals.getOrNull

/**
 * [ImmutableItemStack]向けの[HTIngredient]の実装クラス
 */
class HTItemIngredient(private val ingredient: Ingredient, private val count: Int) : HTIngredient<Item, ImmutableItemStack> {
    fun interface CountGetter {
        fun getRequiredCount(stack: ImmutableItemStack): Int
    }

    companion object {
        @JvmStatic
        private val FLAT_CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemIngredient> =
            VanillaBiCodecs.INGREDIENT.xmap(::HTItemIngredient, HTItemIngredient::ingredient)

        @JvmStatic
        private val NESTED_CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemIngredient> = BiCodec.composite(
            VanillaBiCodecs.INGREDIENT.fieldOf(RagiumConst.ITEMS).forGetter(HTItemIngredient::ingredient),
            BiCodecs.POSITIVE_INT.fieldOf(RagiumConst.AMOUNT).forGetter(HTItemIngredient::count),
            ::HTItemIngredient,
        )

        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemIngredient> = BiCodecs
            .xor(FLAT_CODEC, NESTED_CODEC)
            .xmap(::unwrapEither) { ingredient: HTItemIngredient ->
                when (ingredient.count) {
                    1 -> Either.left(ingredient)
                    else -> Either.right(ingredient)
                }
            }

        @JvmStatic
        private fun resolveValue(value: Ingredient.Value): HolderSet<Item> = when (value) {
            is Ingredient.TagValue -> BuiltInRegistries.ITEM.getTag(value.tag()).getOrNull() ?: HolderSet.empty()
            else -> HolderSet.direct(ItemStack::getItemHolder, value.items)
        }
    }

    private constructor(ingredient: Ingredient) : this(ingredient, 1)

    fun test(stack: ItemStack): Boolean = stack.toImmutable()?.let(this::test) ?: false

    fun testOnlyType(stack: ItemStack): Boolean = stack.toImmutable()?.let(this::testOnlyType) ?: false

    fun copyWithCount(operator: IntUnaryOperator): HTItemIngredient = HTItemIngredient(this.ingredient, operator.applyAsInt(this.count))

    override fun test(stack: ImmutableItemStack): Boolean = testOnlyType(stack) && stack.amount() >= this.count

    override fun testOnlyType(stack: ImmutableItemStack): Boolean = ingredient.test(stack.unwrap())

    override fun getRequiredAmount(stack: ImmutableItemStack): Int = if (testOnlyType(stack)) this.count else 0

    override fun hasNoMatchingStacks(): Boolean = ingredient.items.isEmpty()

    override fun unwrap(): Either<Pair<HolderSet<Item>, Int>, List<ImmutableItemStack>> {
        val custom: ICustomIngredient? = ingredient.customIngredient
        if (custom != null) {
            return Either.right(
                custom.items
                    .map { it.copyWithCount(count) }
                    .toList()
                    .mapNotNull(ItemStack::toImmutable),
            )
        } else {
            val values: Array<Ingredient.Value> = ingredient.values
            return when (values.size) {
                0 -> Either.right(listOf())
                1 -> Either.left(resolveValue(values[0]) to count)
                else -> Either.right(
                    values
                        .flatMap(Ingredient.Value::getItems)
                        .map { it.copyWithCount(count) }
                        .mapNotNull(ItemStack::toImmutable),
                )
            }
        }
    }
}
