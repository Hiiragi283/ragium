package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTComplexRecipeBuilder
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumMixingRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Dirt + Water -> Mud
        HTComplexRecipeBuilder
            .mixing(
                itemCreator.fromItem(Items.DIRT),
                fluidCreator.water(250),
            ).setResult(itemResult.create(Items.MUD))
            .setTime(20 * 5)
            .save(output)
        // Gravel + Water -> Flint
        HTComplexRecipeBuilder
            .mixing(
                itemCreator.fromTagKey(Tags.Items.GRAVELS),
                fluidCreator.water(250),
            ).setResult(itemResult.create(Items.FLINT))
            .setTime(20 * 5)
            .save(output)

        // Diamond + Raginite -> Ragi-Crystal
        HTComplexRecipeBuilder
            .mixing(
                itemCreator.fromTagKey(HCMaterialPrefixes.GEM, VanillaMaterialKeys.DIAMOND),
                fluidCreator.fromTagKey(HCMaterialPrefixes.MOLTEN, RagiumMaterialKeys.RAGINITE, 100 * 8),
            ).setResult(RagiumMaterialResultHelper.item(HCMaterialPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL))
            .save(output)

        // Creosote + Redstone -> Lubricant
        HTComplexRecipeBuilder
            .mixing(
                itemCreator.fromTagKey(HCMaterialPrefixes.DUST, VanillaMaterialKeys.REDSTONE),
                fluidCreator.fromTagKey(RagiumFluids.CREOSOTE, 1000),
            ).setResult(fluidResult.create(RagiumFluids.LUBRICANT, 500))
            .saveSuffixed(output, "_from_creosote_with_redstone")
        // Creosote + Raginite -> Lubricant
        HTComplexRecipeBuilder
            .mixing(
                itemCreator.fromTagKey(HCMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE),
                fluidCreator.fromTagKey(RagiumFluids.CREOSOTE, 1000),
            ).setResult(fluidResult.create(RagiumFluids.LUBRICANT, 750))
            .saveSuffixed(output, "_from_creosote_with_raginite")

        // Ethanol + Seed Oil -> Biofuel
        HTComplexRecipeBuilder
            .mixing(
                itemCreator.fromTagKey(HiiragiCoreTags.Items.ORGANIC_OILS),
                fluidCreator.fromTagKey(RagiumFluids.ETHANOL, 750),
            ).setResult(fluidResult.create(RagiumFluids.BIOFUEL, 500))
            .saveSuffixed(output, "_from_ethanol")

        // Water + Wheat Flour -> Wheat Dough
        HTComplexRecipeBuilder
            .mixing(
                itemCreator.fromTagKey(HiiragiCoreTags.Items.FLOURS_WHEAT),
                fluidCreator.water(250),
            ).setResult(itemResult.create(HCItems.WHEAT_DOUGH, HiiragiCoreTags.Items.DOUGHS_WHEAT))
            .save(output)
    }
}
