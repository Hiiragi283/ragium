package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.recipe.HTInfusionRecipe
import hiiragi283.ragium.common.recipe.HTMachineRecipe
import hiiragi283.ragium.common.recipe.HTTransformRecipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object RagiumRecipeSerializers {
    @JvmField
    val MACHINE: HTMachineRecipe.Serializer = register("generic", HTMachineRecipe.Serializer)

    @JvmField
    val INFUSION: HTInfusionRecipe.Serializer = register("alchemical_infusion", HTInfusionRecipe.Serializer)

    @JvmField
    val TRANSFORM: HTTransformRecipe.Serializer = register("alchemical_transform", HTTransformRecipe.Serializer)

    @JvmStatic
    private fun <T : RecipeSerializer<*>> register(name: String, serializer: T): T = Registry.register(
        Registries.RECIPE_SERIALIZER,
        Ragium.id(name),
        serializer,
    )
}
