package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.data.recipe.HTIngredientRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTStackRecipeBuilder
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.NonNullList
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe

/**
 * @see vectorwing.farmersdelight.data.builder.CookingPotRecipeBuilder
 */
class HTCookingPotRecipeBuilder(stack: ImmutableItemStack, private val container: ImmutableItemStack?) :
    HTStackRecipeBuilder<HTCookingPotRecipeBuilder>(
        "cooking/",
        stack,
    ),
    HTIngredientRecipeBuilder<HTCookingPotRecipeBuilder> {
    companion object {
        @JvmStatic
        fun create(item: ItemLike, count: Int = 1, container: ItemLike? = null): HTCookingPotRecipeBuilder =
            HTCookingPotRecipeBuilder(ImmutableItemStack.of(item, count), container?.let(ImmutableItemStack::of))
    }

    private var groupName: String? = null
    private var tab: CookingPotRecipeBookTab? = null
    private var ingredients: MutableList<Ingredient> = mutableListOf()
    private var time: Int = 20 * 10
    private var exp: Float = 0f

    override fun addIngredient(ingredient: Ingredient): HTCookingPotRecipeBuilder = apply {
        this.ingredients.add(ingredient)
    }

    override fun group(groupName: String?): HTCookingPotRecipeBuilder = apply {
        this.groupName = groupName
    }

    fun setTab(tab: CookingPotRecipeBookTab): HTCookingPotRecipeBuilder = apply {
        this.tab = tab
    }

    fun setTime(time: Int): HTCookingPotRecipeBuilder = apply {
        this.time = time
    }

    fun setExp(exp: Float): HTCookingPotRecipeBuilder = apply {
        this.exp = exp
    }

    override fun createRecipe(output: ItemStack): CookingPotRecipe = CookingPotRecipe(
        groupName ?: "",
        tab,
        ingredients.let(NonNullList<Ingredient>::copyOf),
        output,
        container?.unwrap() ?: ItemStack.EMPTY,
        exp,
        time,
    )
}
