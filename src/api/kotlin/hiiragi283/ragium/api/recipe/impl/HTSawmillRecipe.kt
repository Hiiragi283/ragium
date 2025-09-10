package hiiragi283.ragium.api.recipe.impl

import hiiragi283.ragium.api.recipe.RagiumRecipeSerializers
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.SingleItemRecipe
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level

class HTSawmillRecipe(group: String, ingredient: Ingredient, result: ItemStack) :
    SingleItemRecipe(RagiumRecipeTypes.SAWMILL.get(), RagiumRecipeSerializers.SAWMILL.get(), group, ingredient, result) {
    fun getIngredient(): Ingredient = this.ingredient

    fun getResult(): ItemStack = this.result

    override fun matches(input: SingleRecipeInput, level: Level): Boolean = ingredient.test(input.item())
}
