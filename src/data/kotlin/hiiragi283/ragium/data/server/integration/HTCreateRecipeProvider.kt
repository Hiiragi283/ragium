package hiiragi283.ragium.data.server.integration

import com.simibubi.create.content.fluids.transfer.FillingRecipe
import com.simibubi.create.content.kinetics.deployer.ManualApplicationRecipe
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder
import com.simibubi.create.foundation.fluid.FluidIngredient
import hiiragi283.ragium.api.IntegrationMods
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.extension.toStack
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.tag.RagiumFluidTags
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.Tags

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

        // Sweet Berries Cake
        ProcessingRecipeBuilder(::FillingRecipe, RagiumBlocks.SWEET_BERRIES_CAKE.id)
            .withItemIngredients(
                Ingredient.of(RagiumBlocks.SPONGE_CAKE),
            ).withFluidIngredients(FluidIngredient.fromTag(RagiumFluidTags.CHOCOLATES, 1000))
            .withSingleItemOutput(RagiumBlocks.SWEET_BERRIES_CAKE.toStack())
            .build(output)
        // Chocolate Bread
        ProcessingRecipeBuilder(::FillingRecipe, RagiumItems.CHOCOLATE_BREAD.id)
            .withItemIngredients(
                Ingredient.of(Tags.Items.FOODS_BREAD),
            ).withFluidIngredients(FluidIngredient.fromTag(RagiumFluidTags.CHOCOLATES, 250))
            .withSingleItemOutput(RagiumItems.CHOCOLATE_BREAD.toStack())
            .build(output)
    }
}
