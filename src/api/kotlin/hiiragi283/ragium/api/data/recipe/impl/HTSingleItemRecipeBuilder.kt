package hiiragi283.ragium.api.data.recipe.impl

import hiiragi283.ragium.api.data.recipe.HTIngredientRecipeBuilder
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.recipe.impl.HTSawmillRecipe
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.SingleItemRecipe
import net.minecraft.world.item.crafting.StonecutterRecipe
import net.minecraft.world.level.ItemLike

class HTSingleItemRecipeBuilder<RECIPE : SingleItemRecipe>(
    prefix: String,
    private val factory: SingleItemRecipe.Factory<RECIPE>,
    private val output: ItemStack,
) : HTIngredientRecipeBuilder.Prefixed<HTSingleItemRecipeBuilder<RECIPE>>(prefix) {
    companion object {
        @JvmStatic
        fun sawmill(item: ItemLike, count: Int = 1): HTSingleItemRecipeBuilder<HTSawmillRecipe> =
            HTSingleItemRecipeBuilder("sawmill", ::HTSawmillRecipe, ItemStack(item, count))

        @JvmStatic
        fun stonecutter(item: ItemLike, count: Int = 1): HTSingleItemRecipeBuilder<StonecutterRecipe> =
            HTSingleItemRecipeBuilder("stonecutting", ::StonecutterRecipe, ItemStack(item, count))
    }

    private lateinit var ingredient: Ingredient
    private var group: String? = null

    override fun addIngredient(ingredient: Ingredient): HTSingleItemRecipeBuilder<RECIPE> = apply {
        check(!::ingredient.isInitialized) { "Ingredient has already been initialized!" }
        this.ingredient = ingredient
    }

    override fun getPrimalId(): ResourceLocation = output.itemHolder.idOrThrow

    override fun group(groupName: String?): HTSingleItemRecipeBuilder<RECIPE> = apply {
        this.group = groupName
    }

    override fun createRecipe(): RECIPE = factory.create(
        group ?: "",
        ingredient,
        output,
    )
}
