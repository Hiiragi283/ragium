package hiiragi283.ragium.impl.recipe

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.SingleItemRecipe
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level

class HTSawmillRecipe(group: String, ingredient: Ingredient, result: ItemStack) :
    SingleItemRecipe(RagiumRecipeTypes.SAWMILL.get(), RagiumRecipeSerializers.SAWMILL, group, ingredient, result) {
    fun getIngredient(): Ingredient = this.ingredient

    fun getResult(): ItemStack = this.result

    override fun matches(input: SingleRecipeInput, level: Level): Boolean = ingredient.test(input.item())
}
