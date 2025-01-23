package hiiragi283.ragium.data.server.integration

import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.material.Fluid
import org.cyclops.evilcraft.RegistryEntries

object HTEvilRecipeProvider : RagiumRecipeProvider.ModChild("evilcraft") {
    @JvmField
    val BLOOD: ResourceKey<Fluid> = ResourceKey.create(Registries.FLUID, id("blood"))

    override fun buildModRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        val blood: Fluid = holderLookup.lookupOrThrow(Registries.FLUID).getOrThrow(BLOOD).value()
        // Hardened Blood
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.EXTRACTOR)
            .fluidInput(blood)
            .catalyst(RagiumItems.DEHYDRATION_CATALYST)
            .itemOutput(RegistryEntries.BLOCK_HARDENED_BLOOD.get())
            .save(output)
    }
}
