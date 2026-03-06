package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTSimpleDuplicatingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

class HTDuplicatingRecipeBuilder : HTProcessingRecipeBuilder(RagiumConst.DUPLICATING) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTDuplicatingRecipeBuilder.() -> Unit) {
            HTDuplicatingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    lateinit var ingredient: HTItemIngredient
    var requiredMatter: Int = -1

    override fun getPrimalId(): ResourceLocation =
        ingredient.unwrap().map(TagKey<Item>::location) { resources: List<HTItemResourceType> -> resources.firstOrNull()?.getId() }
            ?: error("Could not generate recipe id from empty ingredient")

    override fun createRecipe(): HTSimpleDuplicatingRecipe = HTSimpleDuplicatingRecipe(ingredient, requiredMatter, time)
}
