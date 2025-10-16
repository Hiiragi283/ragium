package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.data.recipe.HTIngredientRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTStackRecipeBuilder
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.NonNullList
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.ShapelessRecipe
import net.minecraft.world.level.ItemLike

class HTShapelessRecipeBuilder(private val category: CraftingBookCategory, stack: ImmutableItemStack) :
    HTStackRecipeBuilder<HTShapelessRecipeBuilder>("shapeless", stack),
    HTIngredientRecipeBuilder<HTShapelessRecipeBuilder> {
    companion object {
        @JvmStatic
        fun building(item: ItemLike, count: Int = 1): HTShapelessRecipeBuilder =
            HTShapelessRecipeBuilder(CraftingBookCategory.BUILDING, ImmutableItemStack.of(item, count))

        @JvmStatic
        fun redstone(item: ItemLike, count: Int = 1): HTShapelessRecipeBuilder =
            HTShapelessRecipeBuilder(CraftingBookCategory.REDSTONE, ImmutableItemStack.of(item, count))

        @JvmStatic
        fun equipment(item: ItemLike, count: Int = 1): HTShapelessRecipeBuilder =
            HTShapelessRecipeBuilder(CraftingBookCategory.EQUIPMENT, ImmutableItemStack.of(item, count))

        @JvmStatic
        fun misc(item: ItemLike, count: Int = 1): HTShapelessRecipeBuilder =
            HTShapelessRecipeBuilder(CraftingBookCategory.MISC, ImmutableItemStack.of(item, count))
    }

    private val ingredients: NonNullList<Ingredient> = NonNullList.create()

    override fun addIngredient(ingredient: Ingredient): HTShapelessRecipeBuilder = apply {
        ingredients.add(ingredient)
    }

    //    RecipeBuilder    //

    private var groupName: String? = null

    override fun group(groupName: String?): HTShapelessRecipeBuilder = apply {
        this.groupName = groupName
    }

    override fun createRecipe(output: ItemStack): ShapelessRecipe = ShapelessRecipe(
        groupName ?: "",
        category,
        output,
        ingredients,
    )
}
