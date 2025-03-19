package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.recipe.HTCrushingRecipe
import hiiragi283.ragium.api.recipe.HTExtractingRecipe
import hiiragi283.ragium.api.recipe.HTSingleItemRecipe
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike

class HTSingleItemRecipeBuilder<R : HTSingleItemRecipe>(
    private val factory: HTSingleItemRecipe.Factory<R>,
    override val prefix: String,
    private val result: ItemStack,
) : HTIngredientRecipeBuilder<HTSingleItemRecipeBuilder<R>, R> {
    companion object {
        @JvmStatic
        fun crush(result: ItemLike, count: Int = 1): HTSingleItemRecipeBuilder<HTCrushingRecipe> =
            HTSingleItemRecipeBuilder(::HTCrushingRecipe, "crushing", ItemStack(result, count))

        @JvmStatic
        fun extract(result: ItemLike, count: Int = 1): HTSingleItemRecipeBuilder<HTExtractingRecipe> =
            HTSingleItemRecipeBuilder(::HTExtractingRecipe, "extracting", ItemStack(result, count))
    }

    private var group: String? = null
    private lateinit var ingredient: Ingredient

    override fun addIngredient(ingredient: Ingredient): HTSingleItemRecipeBuilder<R> = apply {
        check(!::ingredient.isInitialized) { "Ingredient has already been initialized!" }
        this.ingredient = ingredient
    }

    override fun group(groupName: String?): HTSingleItemRecipeBuilder<R> = apply {
        this.group = groupName
    }

    override fun getPrimalId(): ResourceLocation = result.itemHolder.idOrThrow

    override fun createRecipe(): R = factory.create(
        group ?: "",
        ingredient,
        result,
    )
}
