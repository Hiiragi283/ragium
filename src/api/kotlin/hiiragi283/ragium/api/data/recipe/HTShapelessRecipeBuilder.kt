package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.extension.idOrThrow
import net.minecraft.core.NonNullList
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.ShapelessRecipe
import net.minecraft.world.level.ItemLike

class HTShapelessRecipeBuilder(private val output: ItemStack, private val category: CraftingBookCategory) :
    HTIngredientRecipeBuilder<HTShapelessRecipeBuilder, ShapelessRecipe> {
    constructor(item: ItemLike, count: Int = 1, category: CraftingBookCategory = CraftingBookCategory.MISC) : this(
        ItemStack(item, count),
        category,
    )

    private val ingredients: NonNullList<Ingredient> = NonNullList.create()

    override fun addIngredient(ingredient: Ingredient): HTShapelessRecipeBuilder = apply {
        ingredients.add(ingredient)
    }

    //    RecipeBuilder    //

    override fun getPrimalId(): ResourceLocation = output.itemHolder.idOrThrow

    private var groupName: String? = null

    override fun group(groupName: String?): HTShapelessRecipeBuilder = apply {
        this.groupName = groupName
    }

    override fun getPrefix(recipe: ShapelessRecipe): String = "shapeless"

    override fun createRecipe(): ShapelessRecipe = ShapelessRecipe(
        groupName ?: "",
        category,
        output,
        ingredients,
    )
}
