package hiiragi283.ragium.data.server.recipe.compat

import blusunrize.immersiveengineering.api.IETags
import blusunrize.immersiveengineering.common.blocks.wooden.TreatedWoodStyles
import blusunrize.immersiveengineering.common.register.IEBlocks
import blusunrize.immersiveengineering.data.recipes.builder.BaseHelpers
import blusunrize.immersiveengineering.data.recipes.builder.BottlingMachineRecipeBuilder
import blusunrize.immersiveengineering.data.recipes.builder.MixerRecipeBuilder
import blusunrize.immersiveengineering.data.recipes.builder.RefineryRecipeBuilder
import blusunrize.immersiveengineering.data.recipes.builder.SqueezerRecipeBuilder
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTArcFurnaceRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.impl.HTItemWithFluidToObjRecipeBuilder
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.api.util.material.HTItemMaterialVariant
import hiiragi283.ragium.api.util.material.HTMaterialType
import hiiragi283.ragium.api.util.material.HTVanillaMaterialType
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.util.material.RagiumMaterialType
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.neoforged.neoforge.common.Tags

/**
 * @see [blusunrize.immersiveengineering.data.recipes.MultiblockRecipes]
 */
object RagiumImmersiveRecipeProvider : HTRecipeProvider.Integration(RagiumConst.IMMERSIVE) {
    override fun buildRecipeInternal() {
        // Treated Planks
        HTItemWithFluidToObjRecipeBuilder
            .infusing(
                HTIngredientHelper.item(ItemTags.PLANKS),
                HTIngredientHelper.fluid(IETags.fluidCreosote, 125),
                HTResultHelper.item(IEBlocks.WoodenDecoration.TREATED_WOOD[TreatedWoodStyles.HORIZONTAL]!!),
            ).save(output)
        // Redstone Acid
        HTItemWithFluidToObjRecipeBuilder
            .mixing(
                HTIngredientHelper.item(HTItemMaterialVariant.DUST, HTVanillaMaterialType.REDSTONE),
                HTIngredientHelper.water(1000),
                HTResultHelper.fluid(IETags.fluidRedstoneAcid, 1000),
            ).save(output)

        raginite()
        crimson()
        warped()
        eldritch()
    }

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

    private fun crimson() {
        squeezeCrystal(ItemTags.CRIMSON_STEMS, RagiumFluidContents.CRIMSON_SAP, RagiumMaterialType.CRIMSON_CRYSTAL)
    }

    private fun warped() {
        squeezeCrystal(ItemTags.WARPED_STEMS, RagiumFluidContents.WARPED_SAP, RagiumMaterialType.WARPED_CRYSTAL)
    }

    private fun eldritch() {
        // Crimson + Warped -> Eldritch Flux
        RefineryRecipeBuilder
            .builder()
            .output(RagiumFluidContents.ELDRITCH_FLUX.get(), 100)
            .input(RagiumFluidContents.CRIMSON_SAP.toIngredient(100))
            .input(RagiumFluidContents.WARPED_SAP.toIngredient(100))
            .setEnergy(240)
            .build(output, id("refinery/eldritch_flux"))

        MixerRecipeBuilder
            .builder()
            .output(RagiumFluidContents.ELDRITCH_FLUX.get(), 1000)
            .fluidInput(RagiumFluidContents.CRIMSON_SAP.commonTag, 1000)
            .input(HTItemMaterialVariant.GEM, RagiumMaterialType.WARPED_CRYSTAL)
            .setEnergy(3200)
            .build(output, id("mixer/eldritch_flux"))

        MixerRecipeBuilder
            .builder()
            .output(RagiumFluidContents.ELDRITCH_FLUX.get(), 1000)
            .fluidInput(RagiumFluidContents.WARPED_SAP.commonTag, 1000)
            .input(HTItemMaterialVariant.GEM, RagiumMaterialType.CRIMSON_CRYSTAL)
            .setEnergy(3200)
            .build(output, id("mixer/eldritch_flux_alt"))
        // Flux -> Pearl
        BottlingMachineRecipeBuilder
            .builder()
            .output(HTItemMaterialVariant.GEM, RagiumMaterialType.ELDRITCH_PEARL)
            .output(Tags.Items.ENDER_PEARLS)
            .fluidInput(RagiumFluidContents.ELDRITCH_FLUX.commonTag, 1000)
            .input(Tags.Items.ENDER_PEARLS)
            .build(output, id("bottling/eldritch_pearl"))
    }

    //    Extension    //

    private fun squeezeCrystal(log: TagKey<Item>, fluid: HTFluidContent<*, *, *>, material: HTMaterialType) {
        SqueezerRecipeBuilder
            .builder()
            .output(fluid.get(), 125)
            .input(log)
            .setEnergy(6400)
            .build(output, id("squeezer/${fluid.id.path}"))

        val result: TagKey<Item> = HTItemMaterialVariant.GEM.itemTagKey(material)
        BottlingMachineRecipeBuilder
            .builder()
            .output(result)
            .fluidInput(fluid.commonTag, 1000)
            .build(output, id("bottling/${result.location.path}"))
    }

    private fun <T, U : BaseHelpers.ItemInput<T>> U.input(variant: HTItemMaterialVariant, material: HTMaterialType, count: Int = 1): T =
        input(variant.itemTagKey(material), count)

    private fun <T, U : BaseHelpers.ItemOutput<T>> U.output(variant: HTItemMaterialVariant, material: HTMaterialType, count: Int = 1): T =
        output(variant.itemTagKey(material), count)
}
