package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.data.recipe.HTIngredientRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTStackRecipeBuilder
import hiiragi283.ragium.api.registry.HTItemHolderLike
import net.minecraft.core.NonNullList
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.ShapelessRecipe
import net.minecraft.world.level.ItemLike

class HTShapelessRecipeBuilder(
    private val category: CraftingBookCategory,
    item: HTItemHolderLike,
    count: Int,
    component: DataComponentPatch,
) : HTStackRecipeBuilder<HTShapelessRecipeBuilder>("shapeless", item, count, component),
    HTIngredientRecipeBuilder<HTShapelessRecipeBuilder> {
    companion object {
        @JvmStatic
        fun building(item: ItemLike, count: Int = 1, component: DataComponentPatch = DataComponentPatch.EMPTY): HTShapelessRecipeBuilder =
            HTShapelessRecipeBuilder(CraftingBookCategory.BUILDING, HTItemHolderLike.fromItem(item), count, component)

        @JvmStatic
        fun redstone(item: ItemLike, count: Int = 1, component: DataComponentPatch = DataComponentPatch.EMPTY): HTShapelessRecipeBuilder =
            HTShapelessRecipeBuilder(CraftingBookCategory.REDSTONE, HTItemHolderLike.fromItem(item), count, component)

        @JvmStatic
        fun equipment(item: ItemLike, count: Int = 1, component: DataComponentPatch = DataComponentPatch.EMPTY): HTShapelessRecipeBuilder =
            HTShapelessRecipeBuilder(CraftingBookCategory.EQUIPMENT, HTItemHolderLike.fromItem(item), count, component)

        @JvmStatic
        fun misc(item: ItemLike, count: Int = 1, component: DataComponentPatch = DataComponentPatch.EMPTY): HTShapelessRecipeBuilder =
            HTShapelessRecipeBuilder(CraftingBookCategory.MISC, HTItemHolderLike.fromItem(item), count, component)
    }

    constructor(stack: ItemStack, category: CraftingBookCategory) : this(
        category,
        HTItemHolderLike.fromStack(stack),
        stack.count,
        stack.componentsPatch,
    )

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
