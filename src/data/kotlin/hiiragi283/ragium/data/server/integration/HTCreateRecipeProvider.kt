package hiiragi283.ragium.data.server.integration

import com.simibubi.create.content.kinetics.deployer.ManualApplicationRecipe
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder
import hiiragi283.ragium.api.IntegrationMods
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.extension.toStack
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.common.init.RagiumBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.crafting.Ingredient

object HTCreateRecipeProvider : HTRecipeProvider.Modded(IntegrationMods.CREATE) {
    fun manualProcess(path: String): ProcessingRecipeBuilder<ManualApplicationRecipe> =
        ProcessingRecipeBuilder(::ManualApplicationRecipe, RagiumAPI.id(path))

    override fun buildModRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Upgrading manual machines
        manualProcess("grinder")
            .withItemIngredients(
                Ingredient.of(RagiumBlocks.MANUAL_GRINDER),
                Ingredient.of(RagiumBlocks.MACHINE_FRAME),
            ).withSingleItemOutput(HTMachineType.GRINDER.toStack())
            .build(output)
    }
}
