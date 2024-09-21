package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.recipe.HTAlchemyRecipe
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object RagiumRecipeTypes {
    @JvmField
    val ALCHEMY_INFUSE: HTAlchemyRecipe.Type = register("alchemy", HTAlchemyRecipe.Type)

    private fun <T : RecipeType<*>> register(name: String, type: T): T = Registry.register(
        Registries.RECIPE_TYPE,
        Ragium.id(name),
        type,
    )
}
