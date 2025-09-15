package hiiragi283.ragium.api.recipe.ingredient

import hiiragi283.ragium.api.codec.BiCodec
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.crafting.SizedIngredient

/**
 * [ItemStack]向けの[HTIngredient]の実装
 * @see [mekanism.api.recipes.ingredients.ItemStackIngredient]
 */
@ConsistentCopyVisibility
@JvmRecord
data class HTItemIngredient private constructor(private val delegate: SizedIngredient) : HTIngredient<ItemStack> {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemIngredient> = BiCodec
            .of(
                SizedIngredient.FLAT_CODEC,
                SizedIngredient.STREAM_CODEC,
            ).xmap(::HTItemIngredient, HTItemIngredient::delegate)

        @JvmStatic
        fun of(ingredient: SizedIngredient): HTItemIngredient {
            check(!ingredient.ingredient().isEmpty)
            return HTItemIngredient(ingredient)
        }
    }

    override fun test(stack: ItemStack): Boolean = delegate.test(stack)

    override fun testOnlyType(stack: ItemStack): Boolean = delegate.ingredient().test(stack)

    override fun getMatchingStack(stack: ItemStack): ItemStack = if (test(stack)) stack.copyWithCount(delegate.count()) else ItemStack.EMPTY

    override fun getRequiredAmount(stack: ItemStack): Int = if (test(stack)) delegate.count() else 0

    override fun hasNoMatchingStacks(): Boolean = delegate.ingredient().hasNoItems()

    override fun getMatchingStacks(): List<ItemStack> = listOf(*delegate.items)
}
