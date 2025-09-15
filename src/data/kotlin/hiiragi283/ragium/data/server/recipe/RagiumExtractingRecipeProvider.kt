package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.impl.HTItemToObjRecipeBuilder
import hiiragi283.ragium.common.material.HTBlockMaterialVariant
import hiiragi283.ragium.common.material.HTItemMaterialVariant
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.material.RagiumMaterialType
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.DyeItem
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumExtractingRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        // Vanilla
        HTItemToObjRecipeBuilder
            .extracting(
                ingredientHelper.item(Tags.Items.GRAVELS),
                resultHelper.item(Items.FLINT),
            ).saveSuffixed(output, "_from_gravel")

        HTItemToObjRecipeBuilder
            .extracting(
                ingredientHelper.item(Tags.Items.SANDSTONE_RED_BLOCKS),
                resultHelper.item(Items.REDSTONE),
            ).saveSuffixed(output, "_from_red_sandstone")

        HTItemToObjRecipeBuilder
            .extracting(
                ingredientHelper.item(Items.BROWN_MUSHROOM_BLOCK),
                resultHelper.item(Items.BROWN_MUSHROOM, 3),
            ).saveSuffixed(output, "_from_block")

        HTItemToObjRecipeBuilder
            .extracting(
                ingredientHelper.item(Items.RED_MUSHROOM_BLOCK),
                resultHelper.item(Items.RED_MUSHROOM, 3),
            ).saveSuffixed(output, "_from_block")
        // Ragium
        HTItemToObjRecipeBuilder
            .extracting(
                ingredientHelper.item(HTBlockMaterialVariant.STORAGE_BLOCK, HTVanillaMaterialType.REDSTONE),
                resultHelper.item(HTItemMaterialVariant.DUST, RagiumMaterialType.CINNABAR, 3),
            ).saveSuffixed(output, "_from_redstone")

        HTItemToObjRecipeBuilder
            .extracting(
                ingredientHelper.item(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS),
                resultHelper.item(HTItemMaterialVariant.DUST, RagiumMaterialType.SALTPETER),
            ).saveSuffixed(output, "_from_sandstone")

        HTItemToObjRecipeBuilder
            .extracting(
                ingredientHelper.item(Tags.Items.GUNPOWDERS),
                resultHelper.item(HTItemMaterialVariant.DUST, RagiumMaterialType.SULFUR),
            ).saveSuffixed(output, "_from_gunpowder")

        dyes()
    }

    @JvmStatic
    private fun dyes() {
        // Charcoal -> Brown
        HTItemToObjRecipeBuilder
            .extracting(
                ingredientHelper.fuelOrDust(HTVanillaMaterialType.CHARCOAL),
                resultHelper.item(Items.BROWN_DYE),
            ).saveSuffixed(output, "_from_charcoal")
        // Coal -> Black
        HTItemToObjRecipeBuilder
            .extracting(
                ingredientHelper.fuelOrDust(HTVanillaMaterialType.COAL),
                resultHelper.item(Items.BLACK_DYE),
            ).saveSuffixed(output, "_from_coal")

        for (color: DyeColor in DyeColor.entries) {
            val name: String = color.serializedName
            val dye: DyeItem = DyeItem.byColor(color)

            HTItemToObjRecipeBuilder
                .extracting(
                    ingredientHelper.item(HTItemMaterialVariant.RAW_MATERIAL.itemTagKey("dyes/$name")),
                    resultHelper.item(dye, 2),
                ).saveSuffixed(output, "_from_$name")
        }
    }
}
