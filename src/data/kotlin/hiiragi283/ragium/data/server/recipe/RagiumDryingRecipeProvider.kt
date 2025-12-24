package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.registry.HTFluidWithTag
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.setup.HCFluids
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTDryingRecipeBuilder
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumDryingRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Mud -> Clay + Water
        HTDryingRecipeBuilder
            .createItem(itemCreator.fromItem(Items.MUD))
            .setResult(resultHelper.item(Items.CLAY))
            .setResult(resultHelper.fluid(HTFluidWithTag.WATER, 250))
            .saveSuffixed(output, "_from_mud")
        // Crying Obsidian -> Obsidian + ???
        HTDryingRecipeBuilder
            .createItem(itemCreator.fromTagKey(Tags.Items.OBSIDIANS_CRYING))
            .setResult(resultHelper.item(Items.OBSIDIAN))
            .setResult(resultHelper.fluid(HCFluids.ELDRITCH_FLUX, 125))
            .setTime(20 * 20)
            .saveSuffixed(output, "_from_crying")
        // Sapling -> Dead Bush
        HTDryingRecipeBuilder
            .createItem(itemCreator.fromTagKey(ItemTags.SAPLINGS))
            .setResult(resultHelper.item(Items.DEAD_BUSH))
            .setResult(resultHelper.fluid(HTFluidWithTag.WATER, 125))
            .saveSuffixed(output, "_from_sapling")
        // Wet Sponge -> Sponge + Water
        HTDryingRecipeBuilder
            .createItem(itemCreator.fromItem(Items.WET_SPONGE))
            .setResult(resultHelper.item(Items.SPONGE))
            .setResult(resultHelper.fluid(HTFluidWithTag.WATER, 1000))
            .saveSuffixed(output, "_from_sponge")
        // Kelp -> Dried Kelp + Salt Water
        HTDryingRecipeBuilder
            .createItem(itemCreator.fromItem(Items.KELP))
            .setResult(resultHelper.item(Items.DRIED_KELP))
            .setResult(resultHelper.fluid(RagiumFluids.SALT_WATER, 250))
            .saveSuffixed(output, "_from_kelp")

        // Slime -> Raw Rubber
        HTDryingRecipeBuilder
            .createItem(itemCreator.fromItem(Items.SLIME_BALL))
            .setResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.RAW_MATERIAL, HCMaterial.Plates.RUBBER))
            .saveSuffixed(output, "_from_slime")
        // Latex -> Raw Rubber
        HTDryingRecipeBuilder
            .createFluid(fluidCreator.fromTagKey(HCFluids.LATEX, 1000))
            .setResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.RAW_MATERIAL, HCMaterial.Plates.RUBBER, 2))
            .saveSuffixed(output, "_from_latex")
        // Salt Water -> Water + Salt
        HTDryingRecipeBuilder
            .createFluid(fluidCreator.fromTagKey(RagiumFluids.SALT_WATER, 1000))
            .setResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.DUST, HCMaterial.Minerals.SALT))
            .setResult(resultHelper.fluid(HTFluidWithTag.WATER, 750))
            .saveSuffixed(output, "_from_salt_water")
    }
}
