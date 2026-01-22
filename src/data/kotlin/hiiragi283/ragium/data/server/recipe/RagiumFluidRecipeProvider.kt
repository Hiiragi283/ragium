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
                inputCreator.create(Items.SNOW_BLOCK),
                fluidResult.water(1000),
            ).setTime(20 * 5)
            .saveSuffixed(output, "_from_snow_block")

        meltAndSolidify(
            inputCreator.create(Items.SNOWBALL),
            itemResult.create(Items.SNOWBALL),
            VanillaFluidContents.WATER,
            250,
            HTMoldType.BALL,
            "snowball",
            20,
        )
        meltAndSolidify(
            inputCreator.create(Items.ICE),
            itemResult.create(Items.ICE),
            VanillaFluidContents.WATER,
            1000,
            HTMoldType.BLOCK,
            "ice",
        )
        // Lava
        HTSingleRecipeBuilder
            .melting(
                inputCreator.create(listOf(Tags.Items.COBBLESTONES, Tags.Items.STONES)),
                fluidResult.lava(125),
            ).setTime(20 * 30)
            .saveSuffixed(output, "_from_stones")
        HTSingleRecipeBuilder
            .melting(
                inputCreator.create(Tags.Items.NETHERRACKS),
                fluidResult.lava(125),
            ).saveSuffixed(output, "_from_netherrack")
        HTSingleRecipeBuilder
            .melting(
                inputCreator.create(Items.MAGMA_BLOCK),
                fluidResult.lava(250),
            ).saveSuffixed(output, "_from_magma")

        HTSingleRecipeBuilder
            .solidifying(
                inputCreator.lava(1000),
                inputCreator.create(HTMoldType.BLOCK),
                itemResult.create(Items.OBSIDIAN),
            ).save(output)
        // Latex
        HTSingleRecipeBuilder
            .solidifying(
                inputCreator.create(HCFluids.LATEX, 1000),
                inputCreator.create(HTMoldType.BALL),
                itemResult.create(HCItems.RAW_RUBBER, 2),
            ).save(output)
        // Meat
        HTSingleRecipeBuilder
            .melting(
                inputCreator.create(Items.ROTTEN_FLESH),
                createFluidResult(RagiumMaterialKeys.MEAT, HTMaterialPropertyKeys.MOLTEN_FLUID),
            ).saveSuffixed(output, "_from_rotten")
        // Glass
        meltAndSolidify(
            inputCreator.create(Tags.Items.GLASS_PANES),
            itemResult.create(Items.GLASS_PANE),
            HCFluids.MOLTEN_GLASS,
            375,
            HTMoldType.PLATE,
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
                inputCreator.create(fluid, amount),
                inputCreator.create(mold),
                result,
            ).setTime(time)
            .save(output)
    }
}
