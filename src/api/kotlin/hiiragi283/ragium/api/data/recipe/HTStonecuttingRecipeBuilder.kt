package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.extension.idOrThrow
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.StonecutterRecipe
import net.minecraft.world.level.ItemLike

class HTStonecuttingRecipeBuilder(private val output: ItemStack) : HTIngredientRecipeBuilder<HTStonecuttingRecipeBuilder> {
    constructor(item: ItemLike, count: Int = 1) : this(ItemStack(item, count))

    private lateinit var ingredient: Ingredient

    override fun addIngredient(ingredient: Ingredient): HTStonecuttingRecipeBuilder = apply {
        check(!::ingredient.isInitialized) { "Ingredient has already been initialized!" }
        this.ingredient = ingredient
    }

    override fun getPrimalId(): ResourceLocation = output.itemHolder.idOrThrow

    override fun group(groupName: String?): HTStonecuttingRecipeBuilder = this

    override fun save(recipeOutput: RecipeOutput, id: ResourceLocation) {
        recipeOutput.accept(
            id.withPrefix("stonecutting/"),
            StonecutterRecipe(
                "",
                ingredient,
                output,
            ),
            null,
        )
    }
}
