package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.data.recipe.HTIngredientRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTStackRecipeBuilder
import hiiragi283.ragium.api.registry.HTItemHolderLike
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.SingleItemRecipe
import net.minecraft.world.item.crafting.StonecutterRecipe
import net.minecraft.world.level.ItemLike

class HTSingleItemRecipeBuilder<RECIPE : SingleItemRecipe>(
    prefix: String,
    private val factory: SingleItemRecipe.Factory<RECIPE>,
    item: HTItemHolderLike,
    count: Int,
    component: DataComponentPatch,
) : HTStackRecipeBuilder<HTSingleItemRecipeBuilder<RECIPE>>(prefix, item, count, component),
    HTIngredientRecipeBuilder<HTSingleItemRecipeBuilder<RECIPE>> {
    companion object {
        @JvmStatic
        fun stonecutter(item: ItemLike, count: Int = 1): HTSingleItemRecipeBuilder<StonecutterRecipe> =
            HTSingleItemRecipeBuilder("stonecutting", ::StonecutterRecipe, HTItemHolderLike.fromItem(item), count, DataComponentPatch.EMPTY)
    }

    private lateinit var ingredient: Ingredient
    private var group: String? = null

    override fun addIngredient(ingredient: Ingredient): HTSingleItemRecipeBuilder<RECIPE> = apply {
        check(!::ingredient.isInitialized) { "Ingredient has already been initialized!" }
        this.ingredient = ingredient
    }

    override fun group(groupName: String?): HTSingleItemRecipeBuilder<RECIPE> = apply {
        this.group = groupName
    }

    override fun createRecipe(output: ItemStack): RECIPE = factory.create(
        group ?: "",
        ingredient,
        output,
    )
}
