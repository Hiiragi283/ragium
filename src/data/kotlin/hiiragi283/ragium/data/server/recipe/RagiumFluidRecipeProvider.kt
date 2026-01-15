package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.VanillaFluidContents
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTSingleRecipeBuilder
import hiiragi283.ragium.common.item.HTMoldType
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumFluidRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Water
        HTSingleRecipeBuilder
            .melting(
                itemCreator.fromItem(Items.SNOW_BLOCK),
                fluidResult.water(1000),
            ).setTime(20 * 5)
            .saveSuffixed(output, "_from_snow_block")

        meltAndSolidify(
            itemCreator.fromItem(Items.SNOWBALL),
            itemResult.create(Items.SNOWBALL),
            VanillaFluidContents.WATER,
            250,
            HTMoldType.BALL,
            "snowball",
            20,
        )
        meltAndSolidify(
            itemCreator.fromItem(Items.ICE),
            itemResult.create(Items.ICE),
            VanillaFluidContents.WATER,
            1000,
            HTMoldType.BLOCK,
            "ice",
        )
        // Lava
        HTSingleRecipeBuilder
            .melting(
                itemCreator.fromTagKeys(listOf(Tags.Items.COBBLESTONES, Tags.Items.STONES)),
                fluidResult.lava(125),
            ).setTime(20 * 30)
            .saveSuffixed(output, "_from_stones")
        HTSingleRecipeBuilder
            .melting(
                itemCreator.fromTagKey(Tags.Items.NETHERRACKS),
                fluidResult.lava(125),
            ).saveSuffixed(output, "_from_netherrack")
        HTSingleRecipeBuilder
            .melting(
                itemCreator.fromItem(Items.MAGMA_BLOCK),
                fluidResult.lava(250),
            ).saveSuffixed(output, "_from_magma")

        HTSingleRecipeBuilder
            .solidifying(
                fluidCreator.lava(1000),
                itemCreator.fromItem(HTMoldType.BLOCK),
                itemResult.create(Items.OBSIDIAN),
            ).save(output)
        // Latex
        HTSingleRecipeBuilder
            .solidifying(
                fluidCreator.fromTagKey(HCFluids.LATEX, 1000),
                itemCreator.fromItem(HTMoldType.BALL),
                itemResult.create(HCItems.RAW_RUBBER, 2),
            ).save(output)
        // Meat
        HTSingleRecipeBuilder
            .melting(
                itemCreator.fromTagKey(Tags.Items.FOODS_RAW_MEAT),
                createFluidResult(RagiumMaterialKeys.MEAT, HTMaterialPropertyKeys.MOLTEN_FLUID) { it * 2 },
            ).saveSuffixed(output, "_from_raw")

        HTSingleRecipeBuilder
            .melting(
                itemCreator.fromItem(Items.ROTTEN_FLESH),
                createFluidResult(RagiumMaterialKeys.MEAT, HTMaterialPropertyKeys.MOLTEN_FLUID),
            ).saveSuffixed(output, "_from_rotten")
        // Glass
        meltAndSolidify(
            itemCreator.fromTagKey(Tags.Items.GLASS_BLOCKS),
            itemResult.create(Items.GLASS),
            HCFluids.MOLTEN_GLASS,
            1000,
            HTMoldType.BLOCK,
            "block",
        )
        meltAndSolidify(
            itemCreator.fromTagKey(Tags.Items.GLASS_PANES),
            itemResult.create(Items.GLASS_PANE),
            HCFluids.MOLTEN_GLASS,
            375,
            HTMoldType.BLANK,
            "pane",
        )
    }

    @JvmStatic
    private fun meltAndSolidify(
        input: HTItemIngredient,
        result: HTItemResult,
        fluid: HTFluidContent<*, *, *>,
        amount: Int,
        mold: HTMoldType,
        suffix: String,
        time: Int = 20 * 10,
    ) {
        // Melting
        HTSingleRecipeBuilder
            .melting(
                input,
                fluidResult.create(fluid, amount),
            ).setTime(time)
            .saveSuffixed(output, "_from_$suffix")
        // Solidify
        HTSingleRecipeBuilder
            .solidifying(
                fluidCreator.fromTagKey(fluid, amount),
                itemCreator.fromItem(mold),
                result,
            ).setTime(time)
            .save(output)
    }
}
