package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.impl.HTItemToObjRecipeBuilder
import hiiragi283.ragium.api.util.material.HTBlockMaterialVariant
import hiiragi283.ragium.api.util.material.HTItemMaterialVariant
import hiiragi283.ragium.api.util.material.HTVanillaMaterialType
import hiiragi283.ragium.util.material.RagiumMaterialType
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.DyeItem
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumExtractingRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        // Vanilla
        HTItemToObjRecipeBuilder
            .extracting(
                HTIngredientHelper.item(Tags.Items.GRAVELS),
                HTResultHelper.item(Items.FLINT),
            ).saveSuffixed(output, "_from_gravel")

        HTItemToObjRecipeBuilder
            .extracting(
                HTIngredientHelper.item(Tags.Items.SANDSTONE_RED_BLOCKS),
                HTResultHelper.item(Items.REDSTONE),
            ).saveSuffixed(output, "_from_red_sandstone")

        HTItemToObjRecipeBuilder
            .extracting(
                HTIngredientHelper.item(Items.BROWN_MUSHROOM_BLOCK),
                HTResultHelper.item(Items.BROWN_MUSHROOM, 3),
            ).saveSuffixed(output, "_from_block")

        HTItemToObjRecipeBuilder
            .extracting(
                HTIngredientHelper.item(Items.RED_MUSHROOM_BLOCK),
                HTResultHelper.item(Items.RED_MUSHROOM, 3),
            ).saveSuffixed(output, "_from_block")
        // Ragium
        HTItemToObjRecipeBuilder
            .extracting(
                HTIngredientHelper.item(HTBlockMaterialVariant.STORAGE_BLOCK, HTVanillaMaterialType.REDSTONE),
                HTResultHelper.item(HTItemMaterialVariant.DUST, RagiumMaterialType.CINNABAR, 3),
            ).saveSuffixed(output, "_from_redstone")

        HTItemToObjRecipeBuilder
            .extracting(
                HTIngredientHelper.item(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS),
                HTResultHelper.item(HTItemMaterialVariant.DUST, RagiumMaterialType.SALTPETER),
            ).saveSuffixed(output, "_from_sandstone")

        HTItemToObjRecipeBuilder
            .extracting(
                HTIngredientHelper.item(Tags.Items.GUNPOWDERS),
                HTResultHelper.item(HTItemMaterialVariant.DUST, RagiumMaterialType.SULFUR),
            ).saveSuffixed(output, "_from_gunpowder")

        dyes()
    }

    private fun dyes() {
        // Charcoal -> Brown
        HTItemToObjRecipeBuilder
            .extracting(
                HTIngredientHelper.fuelOrDust(HTVanillaMaterialType.CHARCOAL),
                HTResultHelper.item(Items.BROWN_DYE),
            ).saveSuffixed(output, "_from_charcoal")
        // Coal -> Black
        HTItemToObjRecipeBuilder
            .extracting(
                HTIngredientHelper.fuelOrDust(HTVanillaMaterialType.COAL),
                HTResultHelper.item(Items.BLACK_DYE),
            ).saveSuffixed(output, "_from_coal")

        for (color: DyeColor in DyeColor.entries) {
            val name: String = color.serializedName
            val dye: DyeItem = DyeItem.byColor(color)

            HTItemToObjRecipeBuilder
                .extracting(
                    HTIngredientHelper.item(HTItemMaterialVariant.RAW_MATERIAL.itemTagKey("dyes/$name")),
                    HTResultHelper.item(dye, 2),
                ).saveSuffixed(output, "_from_$name")
        }
    }
}
