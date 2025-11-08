package hiiragi283.ragium.data.server.recipe.compat

import blusunrize.immersiveengineering.api.IETags
import blusunrize.immersiveengineering.api.crafting.IngredientWithSize
import blusunrize.immersiveengineering.common.blocks.wooden.TreatedWoodStyles
import blusunrize.immersiveengineering.common.register.IEBlocks
import blusunrize.immersiveengineering.data.recipes.builder.BottlingMachineRecipeBuilder
import blusunrize.immersiveengineering.data.recipes.builder.CrusherRecipeBuilder
import blusunrize.immersiveengineering.data.recipes.builder.RefineryRecipeBuilder
import blusunrize.immersiveengineering.data.recipes.builder.SqueezerRecipeBuilder
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.material.HTMaterialRecipeData
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.RagiumMoltenCrystalData
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.impl.data.recipe.HTArcFurnaceRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTFluidTransformRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithFluidToChancedItemRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.material.RagiumMaterialRecipeData
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient

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
        misc()
    }

    @JvmStatic
    private fun raginite() {
        crushFromData(RagiumMaterialRecipeData.RAGINITE_ORE)
            .build(output, id("crusher/raginite_ore"))
        crushFromData(RagiumMaterialRecipeData.RAGI_CRYSTAL_ORE)
            .build(output, id("crusher/ragi_crystal_ore"))

        alloyFromData(RagiumMaterialRecipeData.RAGI_ALLOY)
            .build(output, id(RagiumMaterialKeys.RAGI_ALLOY.name))
        alloyFromData(RagiumMaterialRecipeData.ADVANCED_RAGI_ALLOY)
            .build(output, id(RagiumMaterialKeys.ADVANCED_RAGI_ALLOY.name))
        alloyFromData(RagiumMaterialRecipeData.RAGI_CRYSTAL)
            .build(output, id(RagiumMaterialKeys.RAGI_CRYSTAL.name))
    }

    @JvmStatic
    private fun azure() {
        alloyFromData(RagiumMaterialRecipeData.AZURE_SHARD)
            .build(output, id(RagiumMaterialKeys.AZURE.name))
        alloyFromData(RagiumMaterialRecipeData.AZURE_STEEL)
            .build(output, id(RagiumMaterialKeys.AZURE_STEEL.name))
    }

    @JvmStatic
    private fun deepSteel() {
        alloyFromData(RagiumMaterialRecipeData.DEEP_STEEL)
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
        crushFromData(RagiumMaterialRecipeData.CRIMSON_ORE)
            .build(output, id("crusher/crimson_crystal_ore"))
        crushFromData(RagiumMaterialRecipeData.WARPED_ORE)
            .build(output, id("crusher/warped_crystal_ore"))

        // Crimson + Warped -> Eldritch Flux
        RefineryRecipeBuilder
            .builder()
            .output(RagiumFluidContents.ELDRITCH_FLUX.get(), 100)
            .input(RagiumFluidContents.CRIMSON_BLOOD.toIngredient(100))
            .input(RagiumFluidContents.DEW_OF_THE_WARP.toIngredient(100))
            .catalyst(RagiumModTags.Items.ELDRITCH_PEARL_BINDER)
            .setEnergy(240)
            .build(output, id("refinery/eldritch_flux"))

        alloyFromData(RagiumMaterialRecipeData.ELDRITCH_PEARL)
            .build(output, id(RagiumMaterialKeys.ELDRITCH_PEARL.name))
        alloyFromData(RagiumMaterialRecipeData.ELDRITCH_PEARL_BULK)
            .build(output, id(RagiumMaterialKeys.ELDRITCH_PEARL.name).withSuffix("_alt"))
    }

    @JvmStatic
    private fun misc() {
        alloyFromData(RagiumMaterialRecipeData.NIGHT_METAL)
            .build(output, id(RagiumMaterialKeys.NIGHT_METAL.name))
        alloyFromData(RagiumMaterialRecipeData.IRIDESCENTIUM)
            .build(output, id(RagiumMaterialKeys.IRIDESCENTIUM.name))
    }

    //    Extension    //

    @JvmStatic
    private fun BottlingMachineRecipeBuilder.output(
        prefix: HTPrefixLike,
        material: HTMaterialLike,
        count: Int = 1,
    ): BottlingMachineRecipeBuilder = output(prefix.itemTagKey(material), count)

    @JvmStatic
    private fun alloyFromData(data: HTMaterialRecipeData): HTArcFurnaceRecipeBuilder {
        val builder: HTArcFurnaceRecipeBuilder = HTArcFurnaceRecipeBuilder.builder()
        // Inputs
        for ((ingredient: Ingredient, count: Int) in data.getIngredients()) {
            builder.input(ingredient, count)
        }
        // Outputs
        data.getOutputs { (item: Item?, tagKey: TagKey<Item>?, count: Int) ->
            when {
                tagKey != null -> builder.output(tagKey, count)
                item != null -> builder.output(item, count)
            }
        }
        return builder
    }

    @JvmStatic
    private fun crushFromData(data: HTMaterialRecipeData): CrusherRecipeBuilder {
        val builder: CrusherRecipeBuilder = CrusherRecipeBuilder.builder()
        // Input
        builder.input(data.getIngredient(0))
        // Outputs
        data.forEachOutput { i: Int, (item: Item?, tagKey: TagKey<Item>?, count: Int, chance: Float) ->
            when (i) {
                0 -> when {
                    tagKey != null -> builder.output(tagKey, count)
                    item != null -> builder.output(item, count)
                }

                else -> when {
                    tagKey != null -> builder.addSecondary(IngredientWithSize(tagKey, count), chance)
                    item != null -> builder.addSecondary(IngredientWithSize.of(ItemStack(item, count)), chance)
                }
            }
        }
        return builder
    }
}
