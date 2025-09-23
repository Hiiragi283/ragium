package hiiragi283.ragium.data.server.recipe.compat

import blusunrize.immersiveengineering.api.IETags
import blusunrize.immersiveengineering.common.blocks.wooden.TreatedWoodStyles
import blusunrize.immersiveengineering.common.register.IEBlocks
import blusunrize.immersiveengineering.data.recipes.builder.BaseHelpers
import blusunrize.immersiveengineering.data.recipes.builder.BottlingMachineRecipeBuilder
import blusunrize.immersiveengineering.data.recipes.builder.RefineryRecipeBuilder
import blusunrize.immersiveengineering.data.recipes.builder.SqueezerRecipeBuilder
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.material.HTItemMaterialVariant
import hiiragi283.ragium.common.material.HTMoltenCrystalData
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.impl.data.recipe.HTArcFurnaceRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTFluidTransformRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithFluidToChancedItemRecipeBuilder
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

/**
 * @see [blusunrize.immersiveengineering.data.recipes.MultiblockRecipes]
 */
object RagiumImmersiveRecipeProvider : HTRecipeProvider.Integration(RagiumConst.IMMERSIVE) {
    override fun buildRecipeInternal() {
        // Treated Planks
        HTItemWithFluidToChancedItemRecipeBuilder
            .washing(
                ingredientHelper.item(ItemTags.PLANKS),
                ingredientHelper.fluid(IETags.fluidCreosote, 125),
            ).addResult(resultHelper.item(IEBlocks.WoodenDecoration.TREATED_WOOD[TreatedWoodStyles.HORIZONTAL]!!))
            .save(output)
        // Redstone Acid
        HTFluidTransformRecipeBuilder
            .mixing(
                ingredientHelper.item(HTItemMaterialVariant.DUST, HTVanillaMaterialType.REDSTONE),
                ingredientHelper.water(1000),
                resultHelper.fluid(IETags.fluidRedstoneAcid, 1000),
            ).save(output)

        raginite()
        molten()
    }

    @JvmStatic
    private fun raginite() {
        // Copper -> Ragi-Alloy
        HTArcFurnaceRecipeBuilder
            .builder()
            .output(HTItemMaterialVariant.INGOT, RagiumMaterialType.RAGI_ALLOY)
            .input(ingotOrDust(HTVanillaMaterialType.COPPER))
            .input(HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE, 2)
            .build(output, id("ragi_alloy_ingot"))
        // Gold -> Advanced Ragi-Alloy
        HTArcFurnaceRecipeBuilder
            .builder()
            .output(HTItemMaterialVariant.INGOT, RagiumMaterialType.ADVANCED_RAGI_ALLOY)
            .input(ingotOrDust(HTVanillaMaterialType.GOLD))
            .input(HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE, 4)
            .build(output, id("advanced_ragi_alloy_ingot"))
        // Diamond -> Ragi-Crystal
        HTArcFurnaceRecipeBuilder
            .builder()
            .output(HTItemMaterialVariant.GEM, RagiumMaterialType.RAGI_CRYSTAL)
            .input(gemOrDust(HTVanillaMaterialType.DIAMOND))
            .input(HTItemMaterialVariant.DUST, RagiumMaterialType.RAGINITE, 6)
            .build(output, id("ragi_crystal"))
    }

    @JvmStatic
    private fun molten() {
        for (data: HTMoltenCrystalData in HTMoltenCrystalData.entries) {
            val molten: HTFluidContent<*, *, *> = data.molten
            val material: HTMaterialType = data.material
            // molten -> gem
            BottlingMachineRecipeBuilder
                .builder()
                .output(HTItemMaterialVariant.GEM, material)
                .fluidInput(molten.commonTag, HTMoltenCrystalData.MOLTEN_TO_GEM)
                .build(output, id("bottling/${material.serializedName}"))

            val log: TagKey<Item> = data.log ?: continue
            val sap: HTFluidContent<*, *, *> = data.sap ?: continue
            // log -> sap
            SqueezerRecipeBuilder
                .builder()
                .output(sap.get(), HTMoltenCrystalData.LOG_TO_SAP)
                .input(log)
                .setEnergy(6400)
                .build(output, id("squeezer/${sap.getPath()}"))
            // sap -> molten
            RefineryRecipeBuilder
                .builder()
                .input(sap.commonTag, 1000)
                .output(molten.get(), HTMoltenCrystalData.SAP_TO_MOLTEN)
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
    private fun <T, U : BaseHelpers.ItemInput<T>> U.input(variant: HTItemMaterialVariant, material: HTMaterialType, count: Int = 1): T =
        input(variant.itemTagKey(material), count)

    @JvmStatic
    private fun <T, U : BaseHelpers.ItemOutput<T>> U.output(variant: HTItemMaterialVariant, material: HTMaterialType, count: Int = 1): T =
        output(variant.itemTagKey(material), count)
}
