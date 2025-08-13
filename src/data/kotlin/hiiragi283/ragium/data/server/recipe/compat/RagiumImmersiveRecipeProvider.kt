package hiiragi283.ragium.data.server.recipe.compat

import blusunrize.immersiveengineering.api.IETags
import blusunrize.immersiveengineering.common.blocks.wooden.TreatedWoodStyles
import blusunrize.immersiveengineering.common.register.IEBlocks
import blusunrize.immersiveengineering.data.recipes.builder.BottlingMachineRecipeBuilder
import blusunrize.immersiveengineering.data.recipes.builder.MixerRecipeBuilder
import blusunrize.immersiveengineering.data.recipes.builder.RefineryRecipeBuilder
import blusunrize.immersiveengineering.data.recipes.builder.SqueezerRecipeBuilder
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTArcFurnaceRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTItemWithFluidToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.setup.RagiumFluidContents
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
                HTIngredientHelper.fluid(RagiumCommonTags.Fluids.CREOSOTE, 125),
                HTResultHelper.item(IEBlocks.WoodenDecoration.TREATED_WOOD[TreatedWoodStyles.HORIZONTAL]!!),
            ).save(output)
        // Redstone Acid
        HTItemWithFluidToObjRecipeBuilder
            .mixing(
                HTIngredientHelper.item(Tags.Items.DUSTS_REDSTONE),
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
            .output(RagiumCommonTags.Items.INGOTS_RAGI_ALLOY)
            .input(ingotOrDust("copper"))
            .input(RagiumCommonTags.Items.DUSTS_RAGINITE, 2)
            .build(output, id("ragi_alloy_ingot"))
        // Gold -> Advanced Ragi-Alloy
        HTArcFurnaceRecipeBuilder
            .builder()
            .output(RagiumCommonTags.Items.INGOTS_ADVANCED_RAGI_ALLOY)
            .input(ingotOrDust("gold"))
            .input(RagiumCommonTags.Items.DUSTS_RAGINITE, 4)
            .build(output, id("advanced_ragi_alloy_ingot"))
        // Diamond -> Ragi-Crystal
        HTArcFurnaceRecipeBuilder
            .builder()
            .output(RagiumCommonTags.Items.GEMS_RAGI_CRYSTAL)
            .input(gemOrDust("diamond"))
            .input(RagiumCommonTags.Items.DUSTS_RAGINITE, 6)
            .build(output, id("ragi_crystal"))
    }

    private fun crimson() {
        squeezeCrystal(
            ItemTags.CRIMSON_STEMS,
            RagiumFluidContents.CRIMSON_SAP,
            RagiumCommonTags.Items.GEMS_CRIMSON_CRYSTAL,
        )
    }

    private fun warped() {
        squeezeCrystal(
            ItemTags.WARPED_STEMS,
            RagiumFluidContents.WARPED_SAP,
            RagiumCommonTags.Items.GEMS_WARPED_CRYSTAL,
        )
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
            .input(RagiumCommonTags.Items.GEMS_WARPED_CRYSTAL)
            .setEnergy(3200)
            .build(output, id("mixer/eldritch_flux"))

        MixerRecipeBuilder
            .builder()
            .output(RagiumFluidContents.ELDRITCH_FLUX.get(), 1000)
            .fluidInput(RagiumFluidContents.WARPED_SAP.commonTag, 1000)
            .input(RagiumCommonTags.Items.GEMS_CRIMSON_CRYSTAL)
            .setEnergy(3200)
            .build(output, id("mixer/eldritch_flux_alt"))
        // Flux -> Pearl
        BottlingMachineRecipeBuilder
            .builder()
            .output(RagiumCommonTags.Items.GEMS_ELDRITCH_PEARL)
            .output(Tags.Items.ENDER_PEARLS)
            .fluidInput(RagiumFluidContents.ELDRITCH_FLUX.commonTag, 1000)
            .input(Tags.Items.ENDER_PEARLS)
            .build(output, id("bottling/eldritch_pearl"))
    }

    //    Extension    //

    private fun squeezeCrystal(log: TagKey<Item>, fluid: HTFluidContent<*, *, *>, result: TagKey<Item>) {
        SqueezerRecipeBuilder
            .builder()
            .output(fluid.get(), 125)
            .input(log)
            .setEnergy(6400)
            .build(output, id("squeezer/${fluid.id.path}"))

        BottlingMachineRecipeBuilder
            .builder()
            .output(result)
            .fluidInput(fluid.commonTag, 1000)
            .build(output, id("bottling/${result.location.path}"))
    }
}
