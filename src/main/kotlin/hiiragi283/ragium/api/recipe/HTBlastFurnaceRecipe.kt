package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.common.crafting.SizedIngredient
import java.util.Optional

class HTBlastFurnaceRecipe(
    group: String,
    firstInput: SizedIngredient,
    secondInput: SizedIngredient,
    thirdInput: Optional<SizedIngredient>,
    output: ItemStack,
) : HTMultiItemRecipe(group, firstInput, secondInput, thirdInput, output) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.BLAST_FURNACE.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.BLAST_FURNACE.get()
}
