package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.recipe.HTTransmuteRecipe
import net.minecraft.core.NonNullList
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike

class HTTransmuteRecipeBuilder(private val output: ItemStack, private val category: CraftingBookCategory) :
    HTIngredientRecipeBuilder<HTTransmuteRecipeBuilder, HTTransmuteRecipe> {
    constructor(item: ItemLike, count: Int = 1, category: CraftingBookCategory = CraftingBookCategory.MISC) : this(
        ItemStack(item, count),
        category,
    )

    private val ingredients: NonNullList<Ingredient> = NonNullList.create()

    override fun addIngredient(ingredient: Ingredient): HTTransmuteRecipeBuilder = apply {
        check(ingredients.size <= 2) { "Ingredient has already been initialized!" }
        ingredients.add(ingredient)
    }

    //    RecipeBuilder    //

    override fun getPrimalId(): ResourceLocation = output.itemHolder.idOrThrow

    private var groupName: String? = null

    override fun group(groupName: String?): HTTransmuteRecipeBuilder = apply {
        this.groupName = groupName
    }

    override fun getPrefix(recipe: HTTransmuteRecipe): String = "transmute"

    override fun createRecipe(): HTTransmuteRecipe = HTTransmuteRecipe(
        groupName ?: "",
        category,
        output,
        ingredients[0],
        ingredients[1],
    )
}
