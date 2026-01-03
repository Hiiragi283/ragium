package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.registry.HTFluidWithTag
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.setup.HCFluids
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTSingleRecipeBuilder
import hiiragi283.ragium.common.item.HTMoldType
import hiiragi283.ragium.common.material.RagiumMaterial
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumFluidRecipeBuilder : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Water
        HTSingleRecipeBuilder
            .melting(
                itemCreator.fromItem(Items.SNOW_BLOCK),
                fluidResult.create(HTFluidWithTag.WATER, 1000),
            ).setTime(20 * 5)
            .saveSuffixed(output, "_from_snow_block")

        meltAndSolidify(
            itemCreator.fromItem(Items.SNOWBALL),
            itemResult.create(Items.SNOWBALL),
            HTFluidWithTag.WATER,
            250,
            HTMoldType.BALL,
            "snowball",
            20,
        )
        meltAndSolidify(
            itemCreator.fromItem(Items.ICE),
            itemResult.create(Items.ICE),
            HTFluidWithTag.WATER,
            1000,
            HTMoldType.BLOCK,
            "ice",
        )
        // Lava
        HTSingleRecipeBuilder
            .melting(
                itemCreator.fromTagKeys(listOf(Tags.Items.COBBLESTONES, Tags.Items.STONES)),
                fluidResult.create(HTFluidWithTag.LAVA, 125),
            ).setTime(20 * 30)
            .saveSuffixed(output, "_from_stones")
        HTSingleRecipeBuilder
            .melting(
                itemCreator.fromTagKey(Tags.Items.NETHERRACKS),
                fluidResult.create(HTFluidWithTag.LAVA, 125),
            ).saveSuffixed(output, "_from_netherrack")
        HTSingleRecipeBuilder
            .melting(
                itemCreator.fromItem(Items.MAGMA_BLOCK),
                fluidResult.create(HTFluidWithTag.LAVA, 250),
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
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.RAW_MATERIAL, HCMaterial.Plates.RUBBER, 2),
            ).save(output)
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
        // Crimson Crystal
        HTSingleRecipeBuilder
            .melting(
                itemCreator.fromTagKeys(
                    listOf(HCMaterialPrefixes.GEM, HCMaterialPrefixes.DUST),
                    listOf(HCMaterial.Gems.CRIMSON_CRYSTAL),
                ),
                fluidResult.create(HCFluids.MOLTEN_CRIMSON_CRYSTAL, HTConst.INGOT_AMOUNT),
            ).save(output)
        HTSingleRecipeBuilder
            .solidifying(
                fluidCreator.fromTagKey(HCFluids.MOLTEN_CRIMSON_CRYSTAL, HTConst.INGOT_AMOUNT),
                itemCreator.fromItem(HTMoldType.GEM),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.GEM, HCMaterial.Gems.CRIMSON_CRYSTAL),
            ).save(output)

        meltAndSolidify(
            HCMaterialPrefixes.STORAGE_BLOCK,
            HCMaterial.Gems.CRIMSON_CRYSTAL,
            HCFluids.MOLTEN_CRIMSON_CRYSTAL,
            HTConst.INGOT_AMOUNT * 9,
            HTMoldType.BLOCK,
            "block",
        )
        // Warped Crystal
        HTSingleRecipeBuilder
            .melting(
                itemCreator.fromTagKeys(
                    listOf(HCMaterialPrefixes.GEM, HCMaterialPrefixes.DUST),
                    listOf(HCMaterial.Gems.WARPED_CRYSTAL),
                ),
                fluidResult.create(HCFluids.MOLTEN_WARPED_CRYSTAL, HTConst.INGOT_AMOUNT),
            ).save(output)
        HTSingleRecipeBuilder
            .solidifying(
                fluidCreator.fromTagKey(HCFluids.MOLTEN_WARPED_CRYSTAL, HTConst.INGOT_AMOUNT),
                itemCreator.fromItem(HTMoldType.GEM),
                RagiumMaterialResultHelper.item(HCMaterialPrefixes.GEM, HCMaterial.Gems.WARPED_CRYSTAL),
            ).save(output)

        meltAndSolidify(
            HCMaterialPrefixes.STORAGE_BLOCK,
            HCMaterial.Gems.WARPED_CRYSTAL,
            HCFluids.MOLTEN_WARPED_CRYSTAL,
            HTConst.INGOT_AMOUNT * 9,
            HTMoldType.BLOCK,
            "block",
        )

        // Raginite
        meltAndSolidify(
            HCMaterialPrefixes.DUST,
            RagiumMaterial.RAGINITE,
            RagiumFluids.MOLTEN_RAGINITE,
            100,
            HTMoldType.BLANK,
            "dust",
        )
        meltAndSolidify(
            HCMaterialPrefixes.STORAGE_BLOCK,
            RagiumMaterial.RAGINITE,
            RagiumFluids.MOLTEN_RAGINITE,
            100 * 9,
            HTMoldType.BLOCK,
            "block",
        )
    }

    @JvmStatic
    private fun meltAndSolidify(
        prefix: HTPrefixLike,
        material: HTMaterialLike,
        fluid: HTFluidWithTag<*>,
        amount: Int,
        mold: HTMoldType,
        suffix: String,
    ) {
        meltAndSolidify(
            prefix,
            material,
            RagiumMaterialResultHelper.item(prefix, material),
            fluid,
            amount,
            mold,
            suffix,
        )
    }

    @JvmStatic
    private fun meltAndSolidify(
        prefix: HTPrefixLike,
        material: HTMaterialLike,
        result: HTItemResult,
        fluid: HTFluidWithTag<*>,
        amount: Int,
        mold: HTMoldType,
        suffix: String,
    ) {
        meltAndSolidify(itemCreator.fromTagKey(prefix, material), result, fluid, amount, mold, suffix)
    }

    @JvmStatic
    private fun meltAndSolidify(
        input: HTItemIngredient,
        result: HTItemResult,
        fluid: HTFluidWithTag<*>,
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
