package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.registry.toLike
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTEnchantingRecipe
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.enchantment.Enchantment

class HTEnchantingRecipeBuilder(private val ingredient: HTItemIngredient, private val holder: Holder<Enchantment>) :
    HTProcessingRecipeBuilder<HTEnchantingRecipeBuilder>(RagiumConst.ENCHANTING) {
    companion object {
        @JvmStatic
        fun create(ingredient: HTItemIngredient, holder: Holder<Enchantment>): HTEnchantingRecipeBuilder =
            HTEnchantingRecipeBuilder(ingredient, holder)
    }

    override fun getPrimalId(): ResourceLocation = holder.toLike().getId()

    override fun createRecipe(): HTEnchantingRecipe = HTEnchantingRecipe(ingredient, holder, time, exp)
}
