package hiiragi283.ragium.data.server.recipe.compat

import blusunrize.immersiveengineering.api.IETags
import blusunrize.immersiveengineering.common.blocks.wooden.TreatedWoodStyles
import blusunrize.immersiveengineering.common.register.IEBlocks
import blusunrize.immersiveengineering.data.recipes.builder.BottlingMachineRecipeBuilder
import blusunrize.immersiveengineering.data.recipes.builder.RefineryRecipeBuilder
import blusunrize.immersiveengineering.data.recipes.builder.SqueezerRecipeBuilder
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.HTMaterialPrefix
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.RagiumMoltenCrystalData
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.impl.data.recipe.HTArcFurnaceRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTFluidTransformRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithFluidToChancedItemRecipeBuilder
import hiiragi283.ragium.setup.CommonMaterialPrefixes
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

/**
 * @see blusunrize.immersiveengineering.data.recipes.MultiblockRecipes
 */
object RagiumImmersiveRecipeProvider : HTRecipeProvider.Integration(RagiumConst.IMMERSIVE) {
    override fun buildRecipeInternal() {
        // Treated Planks
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                itemCreator.fromTagKey(ItemTags.PLANKS),
                fluidCreator.fromTagKey(IETags.fluidCreosote, 125),
            ).addResult(resultHelper.item(IEBlocks.WoodenDecoration.TREATED_WOOD[TreatedWoodStyles.HORIZONTAL]!!))
            .save(output)
        // Redstone Acid
        HTFluidTransformRecipeBuilder
            .mixing(
                itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.REDSTONE),
                fluidCreator.water(1000),
                resultHelper.fluid(IETags.fluidRedstoneAcid, 1000),
            ).save(output)

        raginite()
        azure()
        deepSteel()

        molten()
    }

    @JvmStatic
    private fun raginite() {
        // Copper -> Ragi-Alloy
        HTArcFurnaceRecipeBuilder
            .builder()
            .output(CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.RAGI_ALLOY)
            .input(ingotOrDust(VanillaMaterialKeys.COPPER))
            .input(CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE, 2)
            .build(output, id(RagiumMaterialKeys.RAGI_ALLOY.name))
        // Gold -> Advanced Ragi-Alloy
        HTArcFurnaceRecipeBuilder
            .builder()
            .output(CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY)
            .input(ingotOrDust(VanillaMaterialKeys.GOLD))
            .input(CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE, 4)
            .build(output, id(RagiumMaterialKeys.ADVANCED_RAGI_ALLOY.name))
        // Diamond -> Ragi-Crystal
        HTArcFurnaceRecipeBuilder
            .builder()
            .output(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL)
            .input(gemOrDust(VanillaMaterialKeys.DIAMOND))
            .input(CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE, 6)
            .build(output, id(RagiumMaterialKeys.RAGI_CRYSTAL.name))
    }

    @JvmStatic
    private fun azure() {
        // Amethyst + Lapis -> Azure Shard
        HTArcFurnaceRecipeBuilder
            .builder()
            .output(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.AZURE, 2)
            .input(gemOrDust(VanillaMaterialKeys.AMETHYST))
            .input(gemOrDust(VanillaMaterialKeys.LAPIS))
            .build(output, id(RagiumMaterialKeys.AZURE.name))
        // Iron -> Azure Steel
        HTArcFurnaceRecipeBuilder
            .builder()
            .output(CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.AZURE_STEEL)
            .input(ingotOrDust(VanillaMaterialKeys.IRON))
            .input(gemOrDust(RagiumMaterialKeys.AZURE), 2)
            .build(output, id(RagiumMaterialKeys.AZURE_STEEL.name))
    }

    @JvmStatic
    private fun deepSteel() {
        // Azure Steel -> Deep Steel
        HTArcFurnaceRecipeBuilder
            .builder()
            .output(CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.DEEP_STEEL)
            .input(ingotOrDust(RagiumMaterialKeys.AZURE_STEEL), 4)
            .input(CommonMaterialPrefixes.SCRAP, RagiumMaterialKeys.DEEP_STEEL, 4)
            .build(output, id(RagiumMaterialKeys.DEEP_STEEL.name))
    }

    @JvmStatic
    private fun molten() {
        for (data: RagiumMoltenCrystalData in RagiumMoltenCrystalData.entries) {
            val molten: HTFluidContent<*, *, *> = data.molten
            // molten -> gem
            BottlingMachineRecipeBuilder
                .builder()
                .output(CommonMaterialPrefixes.GEM, data)
                .fluidInput(molten.commonTag, RagiumConst.MOLTEN_TO_GEM)
                .build(output, id("bottling/${data.asMaterialName()}"))

            val log: TagKey<Item> = data.log ?: continue
            val sap: HTFluidContent<*, *, *> = data.sap ?: continue
            // log -> sap
            SqueezerRecipeBuilder
                .builder()
                .output(sap.get(), RagiumConst.LOG_TO_SAP)
                .input(log)
                .setEnergy(6400)
                .build(output, id("squeezer/${sap.getPath()}"))
            // sap -> molten
            RefineryRecipeBuilder
                .builder()
                .input(sap.commonTag, 1000)
                .output(molten.get(), RagiumConst.SAP_TO_MOLTEN)
                .build(output, id("refinery/${molten.getPath()}"))
        }

        // Crimson + Warped -> Eldritch Flux
        RefineryRecipeBuilder
            .builder()
            .output(RagiumFluidContents.ELDRITCH_FLUX.get(), 100)
            .input(RagiumFluidContents.CRIMSON_BLOOD.toIngredient(100))
            .input(RagiumFluidContents.DEW_OF_THE_WARP.toIngredient(100))
            .catalyst(RagiumModTags.Items.ELDRITCH_PEARL_BINDER)
            .setEnergy(240)
            .build(output, id("refinery/eldritch_flux"))
    }

    //    Extension    //

    @JvmStatic
    private fun BottlingMachineRecipeBuilder.output(
        prefix: HTMaterialPrefix,
        material: HTMaterialLike,
        count: Int = 1,
    ): BottlingMachineRecipeBuilder = output(prefix.itemTagKey(material), count)
}
