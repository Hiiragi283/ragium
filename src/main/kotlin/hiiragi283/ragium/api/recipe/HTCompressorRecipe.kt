package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.base.HTSingleItemRecipe
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.common.crafting.SizedIngredient
import java.util.Optional

class HTCompressorRecipe(
    group: String,
    input: SizedIngredient,
    catalyst: Optional<Ingredient>,
    output: ItemStack,
) : HTSingleItemRecipe(group, input, catalyst, output) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.COMPRESSOR.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.COMPRESSOR.get()
}
