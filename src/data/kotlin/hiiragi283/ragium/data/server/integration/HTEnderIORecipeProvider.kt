package hiiragi283.ragium.data.server.integration

import com.enderio.base.common.init.EIOBlocks
import com.enderio.base.common.init.EIOItems
import hiiragi283.ragium.api.IntegrationMods
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTMultiItemRecipeBuilder
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.IntegrationMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.tag.RagiumItemTags
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object HTEnderIORecipeProvider : HTRecipeProvider.Modded(IntegrationMods.EIO_BASE) {
    override fun buildModRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Enderman Skull
        HTMultiItemRecipeBuilder
            .assembler(lookup)
            .itemInput(Items.SKELETON_SKULL)
            .itemInput(Tags.Items.ENDER_PEARLS, 4)
            .itemOutput(EIOBlocks.ENDERMAN_HEAD)
            .save(output)

        alloy(output)
    }

    private fun alloy(output: RecipeOutput) {
        // Copper Alloy
        HTMultiItemRecipeBuilder
            .blastFurnace(lookup)
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.COPPER)
            .itemInput(RagiumItemTags.SILICON)
            .itemOutput(EIOItems.COPPER_ALLOY_INGOT)
            .save(output)
        // Energetic Alloy
        HTMultiItemRecipeBuilder
            .blastFurnace(lookup)
            .itemInput(HTTagPrefix.DUST, VanillaMaterials.REDSTONE)
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.GOLD)
            .itemInput(Tags.Items.DUSTS_GLOWSTONE)
            .itemOutput(EIOItems.ENERGETIC_ALLOY_INGOT)
            .save(output)
        // Vibrant Alloy
        HTMultiItemRecipeBuilder
            .blastFurnace(lookup)
            .itemInput(HTTagPrefix.INGOT, IntegrationMaterials.ENERGETIC_ALLOY)
            .itemInput(Tags.Items.ENDER_PEARLS)
            .itemOutput(EIOItems.VIBRANT_ALLOY_INGOT)
            .save(output)
        // Redstone Alloy
        HTMultiItemRecipeBuilder
            .blastFurnace(lookup)
            .itemInput(HTTagPrefix.DUST, VanillaMaterials.REDSTONE)
            .itemInput(RagiumItemTags.SILICON)
            .itemOutput(EIOItems.REDSTONE_ALLOY_INGOT)
            .save(output)
        // Conductive Alloy
        HTMultiItemRecipeBuilder
            .blastFurnace(lookup)
            .itemInput(HTTagPrefix.INGOT, IntegrationMaterials.COPPER_ALLOY)
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.IRON)
            .itemInput(HTTagPrefix.DUST, VanillaMaterials.REDSTONE)
            .itemOutput(EIOItems.CONDUCTIVE_ALLOY_INGOT)
            .save(output)
        // Pulsating Alloy
        HTMultiItemRecipeBuilder
            .blastFurnace(lookup)
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.IRON)
            .itemInput(Tags.Items.ENDER_PEARLS)
            .itemOutput(EIOItems.PULSATING_ALLOY_INGOT)
            .save(output)
        // Dark Steel
        HTMultiItemRecipeBuilder
            .blastFurnace(lookup)
            .itemInput(HTTagPrefix.INGOT, VanillaMaterials.IRON)
            .itemInput(HTTagPrefix.GEM, VanillaMaterials.COAL)
            .itemInput(HTTagPrefix.DUST, VanillaMaterials.OBSIDIAN)
            .itemOutput(EIOItems.DARK_STEEL_INGOT)
            .save(output)
        // Soularium
    }
}
