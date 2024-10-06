package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.alchemy.HTInfusionRecipe
import hiiragi283.ragium.api.recipe.alchemy.HTTransformRecipe
import hiiragi283.ragium.api.recipe.machine.HTFluidDrillRecipe
import hiiragi283.ragium.api.recipe.machine.HTMachineRecipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object RagiumRecipeSerializers {
    @JvmField
    val FLUID_DRILL: HTFluidDrillRecipe.Serializer = register("fluid_drill", HTFluidDrillRecipe.Serializer)
    
    @JvmField
    val MACHINE: HTMachineRecipe.Serializer = register("machine", HTMachineRecipe.Serializer)

    @JvmField
    val INFUSION: HTInfusionRecipe.Serializer = register("alchemical_infusion", HTInfusionRecipe.Serializer)

    @JvmField
    val TRANSFORM: HTTransformRecipe.Serializer = register("alchemical_transform", HTTransformRecipe.Serializer)

    @JvmStatic
    private fun <T : RecipeSerializer<*>> register(name: String, serializer: T): T = Registry.register(
        Registries.RECIPE_SERIALIZER,
        RagiumAPI.id(name),
        serializer,
    )
}
