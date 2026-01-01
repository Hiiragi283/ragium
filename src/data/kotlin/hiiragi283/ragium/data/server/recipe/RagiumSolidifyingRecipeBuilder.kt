package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.setup.HCFluids
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTSingleRecipeBuilder
import hiiragi283.ragium.common.item.HTMoldType
import net.minecraft.world.item.Items

object RagiumSolidifyingRecipeBuilder : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Water -> Snowball
        HTSingleRecipeBuilder
            .solidifying(
                fluidCreator.water(250),
                itemCreator.fromItem(HTMoldType.BALL),
                itemResult.create(Items.SNOWBALL),
            ).setTime(20)
            .save(output)
        // Water -> Ice
        HTSingleRecipeBuilder
            .solidifying(
                fluidCreator.water(1000),
                itemCreator.fromItem(HTMoldType.BLOCK),
                itemResult.create(Items.ICE),
            ).save(output)
        // Lava -> Obsidian
        HTSingleRecipeBuilder
            .solidifying(
                fluidCreator.lava(1000),
                itemCreator.fromItem(HTMoldType.BLOCK),
                itemResult.create(Items.OBSIDIAN),
            ).save(output)

        // Latex -> Raw Rubber
        HTSingleRecipeBuilder
            .solidifying(
                fluidCreator.fromTagKey(HCFluids.LATEX, 1000),
                itemCreator.fromItem(HTMoldType.BALL),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.RAW_MATERIAL, HCMaterial.Plates.RUBBER, 2),
            ).save(output)
        // Glass -> Glass Block
        HTSingleRecipeBuilder
            .solidifying(
                fluidCreator.fromTagKey(HCFluids.MOLTEN_GLASS, 1000),
                itemCreator.fromItem(HTMoldType.BLOCK),
                itemResult.create(Items.GLASS),
            ).save(output)
        // Glass -> Glass Pane
        HTSingleRecipeBuilder
            .solidifying(
                fluidCreator.fromTagKey(HCFluids.MOLTEN_GLASS, 375),
                itemCreator.fromItem(HTMoldType.BLANK),
                itemResult.create(Items.GLASS_PANE),
            ).save(output)
    }
}
