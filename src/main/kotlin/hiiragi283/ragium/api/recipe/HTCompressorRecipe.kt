package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.common.crafting.SizedIngredient

class HTCompressorRecipe(group: String, input: SizedIngredient, output: ItemStack) : HTSingleItemRecipe(group, input, output) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.COMPRESSOR.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.COMPRESSOR.get()
}
