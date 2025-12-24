package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.registry.HTFluidWithTag
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTSingleRecipeBuilder
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumMeltingRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        water()
        lava()
    }

    @JvmStatic
    private fun water() {
        // Snow -> Water
        HTSingleRecipeBuilder
            .melting(
                itemCreator.fromItem(Items.SNOWBALL),
                resultHelper.fluid(HTFluidWithTag.WATER, 250),
            ).setTime(5)
            .saveSuffixed(output, "_from_snowball")

        HTSingleRecipeBuilder
            .melting(
                itemCreator.fromItem(Items.SNOW_BLOCK),
                resultHelper.fluid(HTFluidWithTag.WATER, 1000),
            ).setTime(20)
            .saveSuffixed(output, "_from_snow_block")
        // Ice -> Water
        HTSingleRecipeBuilder
            .melting(
                itemCreator.fromItem(Items.ICE),
                resultHelper.fluid(HTFluidWithTag.WATER, 1000),
            ).setTime(20 * 10)
            .saveSuffixed(output, "_from_ice")
    }

    @JvmStatic
    private fun lava() {
        // Cobblestone -> Lava
        HTSingleRecipeBuilder
            .melting(
                itemCreator.fromTagKeys(listOf(Tags.Items.COBBLESTONES, Tags.Items.STONES, Tags.Items.NETHERRACKS)),
                resultHelper.fluid(HTFluidWithTag.LAVA, 125),
            ).setTime(20 * 30)
            .saveSuffixed(output, "_from_stones")
        // Magma Block -> Lava
        HTSingleRecipeBuilder
            .melting(
                itemCreator.fromItem(Items.MAGMA_BLOCK),
                resultHelper.fluid(HTFluidWithTag.LAVA, 125),
            ).saveSuffixed(output, "_from_magma")
    }
}
