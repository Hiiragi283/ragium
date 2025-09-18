package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.data.recipe.HTIngredientRecipeBuilder
import hiiragi283.ragium.api.extension.idOrThrow
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.SmithingTransformRecipe
import net.minecraft.world.level.ItemLike

class HTSmithingRecipeBuilder(private val output: ItemStack) : HTIngredientRecipeBuilder.Prefixed<HTSmithingRecipeBuilder>("smithing") {
    constructor(item: ItemLike, count: Int = 1) : this(ItemStack(item, count))

    private val ingredients: MutableList<Ingredient> = mutableListOf()

    override fun addIngredient(ingredient: Ingredient): HTSmithingRecipeBuilder = apply {
        check(ingredients.size <= 2) { "Ingredient has already been initialized!" }
        ingredients.add(ingredient)
    }

    override fun getPrimalId(): ResourceLocation = output.itemHolder.idOrThrow

    override fun group(groupName: String?): HTSmithingRecipeBuilder = this

    override fun createRecipe(): SmithingTransformRecipe = SmithingTransformRecipe(
        ingredients[0],
        ingredients[1],
        ingredients.getOrNull(2) ?: Ingredient.of(),
        output,
    )
}
