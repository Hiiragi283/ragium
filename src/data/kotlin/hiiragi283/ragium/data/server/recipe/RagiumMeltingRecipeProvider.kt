package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.registry.HTFluidWithTag
import hiiragi283.core.setup.HCFluids
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTSingleRecipeBuilder
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumMeltingRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Snow -> Water
        HTSingleRecipeBuilder
            .melting(
                itemCreator.fromItem(Items.SNOWBALL),
                fluidResult.create(HTFluidWithTag.WATER, 250),
            ).setTime(5)
            .saveSuffixed(output, "_from_snowball")

        HTSingleRecipeBuilder
            .melting(
                itemCreator.fromItem(Items.SNOW_BLOCK),
                fluidResult.create(HTFluidWithTag.WATER, 1000),
            ).setTime(20)
            .saveSuffixed(output, "_from_snow_block")
        // Ice -> Water
        HTSingleRecipeBuilder
            .melting(
                itemCreator.fromItem(Items.ICE),
                fluidResult.create(HTFluidWithTag.WATER, 1000),
            ).setTime(20 * 10)
            .saveSuffixed(output, "_from_ice")

        // Cobblestone -> Lava
        HTSingleRecipeBuilder
            .melting(
                itemCreator.fromTagKeys(listOf(Tags.Items.COBBLESTONES, Tags.Items.STONES)),
                fluidResult.create(HTFluidWithTag.LAVA, 125),
            ).setTime(20 * 30)
            .saveSuffixed(output, "_from_stones")
        // Netherrack -> Lava
        HTSingleRecipeBuilder
            .melting(
                itemCreator.fromTagKey(Tags.Items.NETHERRACKS),
                fluidResult.create(HTFluidWithTag.LAVA, 125),
            ).saveSuffixed(output, "_from_netherrack")
        // Magma Block -> Lava
        HTSingleRecipeBuilder
            .melting(
                itemCreator.fromItem(Items.MAGMA_BLOCK),
                fluidResult.create(HTFluidWithTag.LAVA, 250),
            ).saveSuffixed(output, "_from_magma")

        // Glass
        HTSingleRecipeBuilder
            .melting(
                itemCreator.fromTagKey(Tags.Items.GLASS_BLOCKS),
                fluidResult.create(HCFluids.MOLTEN_GLASS, 1000),
            ).saveSuffixed(output, "_from_block")

        HTSingleRecipeBuilder
            .melting(
                itemCreator.fromTagKey(Tags.Items.GLASS_PANES),
                fluidResult.create(HCFluids.MOLTEN_GLASS, 375),
            ).saveSuffixed(output, "_from_pane")
    }
}
