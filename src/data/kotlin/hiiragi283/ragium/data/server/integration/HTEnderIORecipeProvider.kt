package hiiragi283.ragium.data.server.integration

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTMultiItemRecipeBuilder
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.IntegrationMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumItemTags
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.ItemTags
import net.neoforged.neoforge.common.Tags

object HTEnderIORecipeProvider : HTRecipeProvider.Modded("enderio") {
    override fun buildModRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // alloy(output)
    }

    private fun alloy(output: RecipeOutput) {
        // Copper Alloy
        HTMultiItemRecipeBuilder
            .blastFurnace(lookup)
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.COPPER)
            .itemInput(RagiumItemTags.SILICON)
            .itemOutput(HTTagPrefix.INGOT, IntegrationMaterials.COPPER_ALLOY)
            .save(output)
        // Energetic Alloy
        HTMultiItemRecipeBuilder
            .blastFurnace(lookup)
            .itemInput(HTTagPrefix.DUST, VanillaMaterials.REDSTONE)
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.GOLD)
            .itemInput(Tags.Items.DUSTS_GLOWSTONE)
            .itemOutput(HTTagPrefix.INGOT, IntegrationMaterials.ENERGETIC_ALLOY)
            .save(output)
        // Vibrant Alloy
        HTMultiItemRecipeBuilder
            .blastFurnace(lookup)
            .itemInput(HTTagPrefix.INGOT, IntegrationMaterials.ENERGETIC_ALLOY)
            .itemInput(Tags.Items.ENDER_PEARLS)
            .itemOutput(HTTagPrefix.INGOT, IntegrationMaterials.VIBRANT_ALLOY)
            .save(output)
        // Redstone Alloy
        HTMultiItemRecipeBuilder
            .blastFurnace(lookup)
            .itemInput(HTTagPrefix.DUST, VanillaMaterials.REDSTONE)
            .itemInput(RagiumItemTags.SILICON)
            .itemOutput(HTTagPrefix.INGOT, IntegrationMaterials.REDSTONE_ALLOY)
            .save(output)
        // Conductive Alloy
        HTMultiItemRecipeBuilder
            .blastFurnace(lookup)
            .itemInput(HTTagPrefix.DUST, IntegrationMaterials.COPPER_ALLOY)
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.IRON)
            .itemInput(HTTagPrefix.DUST, VanillaMaterials.REDSTONE)
            .itemOutput(HTTagPrefix.INGOT, IntegrationMaterials.CONDUCTIVE_ALLOY)
            .save(output)
        // Pulsating Alloy
        HTMultiItemRecipeBuilder
            .blastFurnace(lookup)
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.IRON)
            .itemInput(Tags.Items.ENDER_PEARLS)
            .itemOutput(HTTagPrefix.INGOT, IntegrationMaterials.PULSATING_ALLOY)
            .save(output)
        // Dark Steel
        HTMultiItemRecipeBuilder
            .blastFurnace(lookup)
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.IRON)
            .itemInput(ItemTags.COALS)
            .itemInput(Tags.Items.OBSIDIANS)
            .itemOutput(HTTagPrefix.INGOT, IntegrationMaterials.DARK_STEEL)
            .save(output)
        // Soularium
    }
}
