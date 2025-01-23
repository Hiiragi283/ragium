package hiiragi283.ragium.data.server.integration

import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.api.extension.fluidHolder
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.level.material.Fluid
import org.cyclops.evilcraft.RegistryEntries

object HTEvilRecipeProvider : RagiumRecipeProvider.ModChild("evilcraft") {
    @JvmField
    val BLOOD: Fluid = fluidHolder<Fluid>(id("blood")).get()

    override fun buildModRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Hardened Blood
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .fluidInput(BLOOD)
            .catalyst(RagiumItems.DEHYDRATION_CATALYST)
            .itemOutput(RegistryEntries.BLOCK_HARDENED_BLOOD.get())
            .save(output)
    }
}
