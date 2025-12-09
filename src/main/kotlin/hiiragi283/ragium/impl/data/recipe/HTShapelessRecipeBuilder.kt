package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.data.recipe.HTIngredientRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTStackRecipeBuilder
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.NonNullList
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.ShapelessRecipe
import net.minecraft.world.level.ItemLike

class HTShapelessRecipeBuilder(stack: ImmutableItemStack) :
    HTStackRecipeBuilder<HTShapelessRecipeBuilder>("shapeless", stack),
    HTIngredientRecipeBuilder<HTShapelessRecipeBuilder> {
    companion object {
        @JvmStatic
        fun create(item: ItemLike, count: Int = 1): HTShapelessRecipeBuilder = HTShapelessRecipeBuilder(ImmutableItemStack.of(item, count))
    }

    private val ingredients: NonNullList<Ingredient> = NonNullList.create()

    override fun addIngredient(ingredient: Ingredient): HTShapelessRecipeBuilder = apply {
        ingredients.add(ingredient)
    }

    fun addIngredients(prefix: HTPrefixLike, key: HTMaterialLike, count: Int): HTShapelessRecipeBuilder =
        addIngredients(prefix.itemTagKey(key), count)

    fun addIngredients(tagKey: TagKey<Item>, count: Int): HTShapelessRecipeBuilder = addIngredients(Ingredient.of(tagKey), count)

    fun addIngredients(vararg items: ItemLike, count: Int): HTShapelessRecipeBuilder = addIngredients(Ingredient.of(*items), count)

    fun addIngredients(ingredient: Ingredient, count: Int): HTShapelessRecipeBuilder = apply {
        repeat(count) {
            addIngredient(ingredient)
        }
    }

    //    RecipeBuilder    //

    private var group: String? = null
    private var category: CraftingBookCategory = CraftingBookCategory.MISC

    fun setGroup(group: String?): HTShapelessRecipeBuilder = apply {
        this.group = group
    }

    fun setCategory(category: CraftingBookCategory): HTShapelessRecipeBuilder = apply {
        this.category = category
    }

    override fun createRecipe(output: ItemStack): ShapelessRecipe = ShapelessRecipe(
        group ?: "",
        category,
        output,
        ingredients,
    )
}
