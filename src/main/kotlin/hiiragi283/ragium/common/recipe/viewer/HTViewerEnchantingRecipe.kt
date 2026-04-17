package hiiragi283.ragium.common.recipe.viewer

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.MapBiCodec
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.ragium.impl.recipe.HTBasicEnchantingRecipe
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.ItemStack

class HTViewerEnchantingRecipe(
    val supportedItems: List<ItemStack>,
    ingredient: HTItemIngredient,
    override val requiredExpAmount: Int,
    val result: ItemStack,
) : HTBasicEnchantingRecipe(ingredient) {
    companion object {
        @JvmField
        val CODEC: MapBiCodec<RegistryFriendlyByteBuf, HTViewerEnchantingRecipe> = MapBiCodec.composite(
            VanillaBiCodecs.ITEM_STACK
                .listOf()
                .fieldOf("supported_items")
                .forGetter(HTViewerEnchantingRecipe::supportedItems),
            HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTViewerEnchantingRecipe::ingredient),
            BiCodecs.NON_NEGATIVE_INT.fieldOf("required_exp_cost").forGetter(HTViewerEnchantingRecipe::requiredExpAmount),
            VanillaBiCodecs.ITEM_STACK.fieldOf(HTConst.RESULT).forGetter(HTViewerEnchantingRecipe::result),
            ::HTViewerEnchantingRecipe,
        )
    }

    override fun applyEnchantment(stack: ItemStack): ItemStack = stack.copy()

    override fun testBase(stack: ItemStack): Boolean = false
}
