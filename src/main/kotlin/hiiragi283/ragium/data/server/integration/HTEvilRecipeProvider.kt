package hiiragi283.ragium.data.server.integration

import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.api.extension.fluidHolder
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.conditions.ModLoadedCondition
import org.cyclops.evilcraft.RegistryEntries

object HTEvilRecipeProvider : RagiumRecipeProvider.Child {
    override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        buildRecipesInternal(output.withConditions(ModLoadedCondition("evil_craft")), holderLookup)
    }

    @JvmField
    val BLOOD: Fluid = fluidHolder<Fluid>("evilcraft:blood").get()

    private fun buildRecipesInternal(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Hardened Blood
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .fluidInput(BLOOD)
            .catalyst(RagiumItems.DEHYDRATION_CATALYST)
            .itemOutput(RegistryEntries.BLOCK_HARDENED_BLOOD.get())
            .save(output)
    }
}
