package hiiragi283.ragium.common.recipe.special

import hiiragi283.ragium.api.recipe.HTItemOrFluidRecipe
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.crafting.RecipeType

abstract class HTCustomCanningRecipe : HTItemOrFluidRecipe.Serializable {
    final override val time: Int = 20

    final override fun getType(): RecipeType<*> = RagiumRecipeTypes.CANNING.get()
}
