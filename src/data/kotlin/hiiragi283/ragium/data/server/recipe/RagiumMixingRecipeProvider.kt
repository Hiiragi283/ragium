package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.material.HCMaterialKeys
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
            .addIngredient(inputCreator.create(Items.DIRT))
            .addIngredient(inputCreator.water(250))
            .setResult(itemResult.create(Items.MUD))
            .setTime(20 * 5)
            .save(output)
        // Gravel + Water -> Flint
        HTMixingRecipeBuilder
            .create()
            .addIngredient(inputCreator.create(Tags.Items.GRAVELS))
            .addIngredient(inputCreator.water(250))
            .setResult(itemResult.create(Items.FLINT))
            .setTime(20 * 5)
            .save(output)

        // Water + Lava -> Obsidian
        HTMixingRecipeBuilder
            .create()
            .addIngredient(inputCreator.water(1000))
            .addIngredient(inputCreator.lava(1000))
            .setResult(itemResult.create(Items.OBSIDIAN))
            .save(output)

        // Eldritch Flux
        HTMixingRecipeBuilder
            .create()
            .addIngredient(inputCreator.create(HiiragiCoreTags.Items.ELDRITCH_PEARL_BINDER))
            .addIngredient(createFluidIng(HCMaterialKeys.CRIMSON_CRYSTAL, HTMaterialPropertyKeys.MOLTEN_FLUID))
            .addIngredient(createFluidIng(HCMaterialKeys.WARPED_CRYSTAL, HTMaterialPropertyKeys.MOLTEN_FLUID))
            .setResult(createFluidResult(HCMaterialKeys.ELDRITCH, HTMaterialPropertyKeys.MOLTEN_FLUID))
            .save(output)

        // Diamond + Raginite -> Ragi-Crystal
        HTMixingRecipeBuilder
            .create()
            .addIngredient(inputCreator.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.DIAMOND))
            .addIngredient(inputCreator.create(RagiumFluids.MOLTEN_RAGINITE, 100 * 8))
            .setResult(itemResult.create(CommonTagPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL))
            .save(output)

        // Creosote + Redstone -> Lubricant
        HTMixingRecipeBuilder
            .create()
            .addIngredient(inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.REDSTONE))
            .addIngredient(inputCreator.create(RagiumFluids.CREOSOTE, 1000))
            .setResult(fluidResult.create(RagiumFluids.LUBRICANT, 500))
            .saveSuffixed(output, "_from_creosote_with_redstone")
        // Creosote + Raginite -> Lubricant
        HTMixingRecipeBuilder
            .create()
            .addIngredient(inputCreator.create(CommonTagPrefixes.DUST, RagiumMaterialKeys.RAGINITE))
            .addIngredient(inputCreator.create(RagiumFluids.CREOSOTE, 1000))
            .setResult(fluidResult.create(RagiumFluids.LUBRICANT, 750))
            .saveSuffixed(output, "_from_creosote_with_raginite")

        // Ethanol + Plant Oil -> Biofuel
        HTMixingRecipeBuilder
            .create()
            .addIngredient(inputCreator.create(RagiumFluids.ETHANOL, 750))
            .addIngredient(inputCreator.create(RagiumFluids.PLANT_OIL, 1000))
            .setResult(fluidResult.create(RagiumFluids.BIOFUEL, 500))
            .saveSuffixed(output, "_from_ethanol")
    }
}
