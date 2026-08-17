package hiiragi283.lib.recipe.ingredient

import com.mojang.serialization.Codec
import hiiragi283.lib.util.Either
import net.minecraft.core.TypedInstance
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.context.ContextMap
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.display.DisplayContentsFactory
import net.neoforged.neoforge.common.crafting.SizedIngredient

typealias HTCatalystOrIngredient = Either<Ingredient, HTItemIngredient>

/**
 * [Item]向けの[HTIngredient]の実装クラスです。
 *
 * 参照 : [Mekanism - ItemStackIngredient](https://github.com/mekanism/Mekanism/blob/26.1/src/api/java/mekanism/api/recipes/ingredients/ItemStackIngredient.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmInline
value class HTItemIngredient(@PublishedApi internal val delegate: SizedIngredient) : HTIngredient<Item, ItemStack> {
    companion object {
        @JvmField
        val CODEC: Codec<HTItemIngredient> = SizedIngredient.NESTED_CODEC.xmap(::HTItemIngredient, HTItemIngredient::delegate)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTItemIngredient> = SizedIngredient.STREAM_CODEC.map(::HTItemIngredient, HTItemIngredient::delegate)
    }

    constructor(ingredient: Ingredient, count: Int) : this(SizedIngredient(ingredient, count))

    inline val unsized: Ingredient get() = delegate.ingredient()
    inline val count: Int get() = delegate.count()

    override fun test(instance: TypedInstance<Item>): Boolean = HTIngredientHelper.unwrap(instance).fold(::testOnlyType, delegate::test)

    override fun testOnlyType(instance: TypedInstance<Item>): Boolean = HTIngredientHelper.createStack(instance).let(unsized::test)

    override fun getRequiredAmount(instance: TypedInstance<Item>): Int = when (testOnlyType(instance)) {
        true -> count
        false -> 0
    }

    override fun getPreviewStacks(contextMap: ContextMap): List<ItemStack> = unsized
        .display()
        .resolve(contextMap, DisplayContentsFactory.ForStacks { it.copyWithCount(count) })
        .toList()
}
