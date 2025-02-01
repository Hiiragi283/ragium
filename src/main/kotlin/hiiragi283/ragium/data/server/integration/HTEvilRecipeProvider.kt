package hiiragi283.ragium.data.server.integration

import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.api.extension.sources
import hiiragi283.ragium.api.tag.RagiumBlockTags
import hiiragi283.ragium.common.init.RagiumRecipes
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
            .create(RagiumRecipes.EXTRACTOR)
            .fluidInput(blood)
            .sources(RagiumBlockTags.HEATING_SOURCES)
            .itemOutput(RegistryEntries.BLOCK_HARDENED_BLOOD.get())
            .save(output)
    }
}
