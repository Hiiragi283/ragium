package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.base.HTItemResult
import hiiragi283.ragium.api.recipe.base.HTMultiItemRecipe
import hiiragi283.ragium.common.init.RagiumRecipeSerializers
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.common.crafting.SizedIngredient
import java.util.*

class HTBlastFurnaceRecipe(
    group: String,
    firstInput: SizedIngredient,
    secondInput: SizedIngredient,
    thirdInput: Optional<SizedIngredient>,
    itemResult: HTItemResult,
) : HTMultiItemRecipe(group, firstInput, secondInput, thirdInput, itemResult) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.BLAST_FURNACE.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.BLAST_FURNACE.get()
}
