package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.data.recipe.HTMaterialResultHelper
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTMixingRecipeBuilder
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumMixingRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Dirt + Water -> Mud
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromItem(Items.DIRT))
            .addIngredient(fluidCreator.water(250))
            .setResult(itemResult.create(Items.MUD))
            .setTime(20 * 5)
            .save(output)
        // Gravel + Water -> Flint
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromTagKey(Tags.Items.GRAVELS))
            .addIngredient(fluidCreator.water(250))
            .setResult(itemResult.create(Items.FLINT))
            .setTime(20 * 5)
            .save(output)
        
        // Water + Lava -> Obsidian
        HTMixingRecipeBuilder
            .create()
            .addIngredient(fluidCreator.water(1000))
            .addIngredient(fluidCreator.lava(1000))
            .setResult(itemResult.create(Items.OBSIDIAN))
            .save(output)

        // Diamond + Raginite -> Ragi-Crystal
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromTagKey(CommonTagPrefixes.GEM, VanillaMaterialKeys.DIAMOND))
            .addIngredient(fluidCreator.fromTagKey(RagiumFluids.MOLTEN_RAGINITE, 100 * 8))
            .setResult(HTMaterialResultHelper.item(CommonTagPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL))
            .save(output)

        // Creosote + Redstone -> Lubricant
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromTagKey(CommonTagPrefixes.DUST, VanillaMaterialKeys.REDSTONE))
            .addIngredient(fluidCreator.fromTagKey(RagiumFluids.CREOSOTE, 1000))
            .setResult(fluidResult.create(RagiumFluids.LUBRICANT, 500))
            .saveSuffixed(output, "_from_creosote_with_redstone")
        // Creosote + Raginite -> Lubricant
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromTagKey(CommonTagPrefixes.DUST, RagiumMaterialKeys.RAGINITE))
            .addIngredient(fluidCreator.fromTagKey(RagiumFluids.CREOSOTE, 1000))
            .setResult(fluidResult.create(RagiumFluids.LUBRICANT, 750))
            .saveSuffixed(output, "_from_creosote_with_raginite")

        // Ethanol + Plant Oil -> Biofuel
        HTMixingRecipeBuilder
            .create()
            .addIngredient(fluidCreator.fromTagKey(RagiumFluids.ETHANOL, 750))
            .addIngredient(fluidCreator.fromTagKey(RagiumFluids.PLANT_OIL, 1000))
            .setResult(fluidResult.create(RagiumFluids.BIOFUEL, 500))
            .saveSuffixed(output, "_from_ethanol")
    }
}
