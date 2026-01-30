package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.registry.toLike
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTEnchantingRecipe
import net.minecraft.core.Holder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.enchantment.Enchantment

class HTEnchantingRecipeBuilder : HTProcessingRecipeBuilder(RagiumConst.ENCHANTING) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTEnchantingRecipeBuilder.() -> Unit) {
            HTEnchantingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    lateinit var ingredient: HTItemIngredient
    lateinit var holder: Holder<Enchantment>

    override fun getPrimalId(): ResourceLocation = holder.toLike().getId()

    override fun createRecipe(): HTEnchantingRecipe = HTEnchantingRecipe(ingredient, holder, time, exp)
}
