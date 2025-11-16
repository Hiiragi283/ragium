package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.common.material.CommonMaterialKeys
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.impl.data.recipe.HTItemWithCatalystRecipeBuilder
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.DyeItem
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumExtractingRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        // Vanilla
        HTItemWithCatalystRecipeBuilder
            .extracting(
                itemCreator.fromTagKey(Tags.Items.GRAVELS),
                resultHelper.item(Items.FLINT),
            ).saveSuffixed(output, "_from_gravel")

        HTItemWithCatalystRecipeBuilder
            .extracting(
                itemCreator.fromTagKey(Tags.Items.SANDSTONE_RED_BLOCKS),
                resultHelper.item(Items.REDSTONE),
            ).saveSuffixed(output, "_from_red_sandstone")

        HTItemWithCatalystRecipeBuilder
            .extracting(
                itemCreator.fromItem(Items.BROWN_MUSHROOM_BLOCK),
                resultHelper.item(Items.BROWN_MUSHROOM, 3),
            ).saveSuffixed(output, "_from_block")

        HTItemWithCatalystRecipeBuilder
            .extracting(
                itemCreator.fromItem(Items.RED_MUSHROOM_BLOCK),
                resultHelper.item(Items.RED_MUSHROOM, 3),
            ).saveSuffixed(output, "_from_block")
        // Ragium
        HTItemWithCatalystRecipeBuilder
            .extracting(
                itemCreator.fromTagKey(CommonMaterialPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.REDSTONE),
                resultHelper.item(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.CINNABAR, 3),
            ).saveSuffixed(output, "_from_redstone")

        HTItemWithCatalystRecipeBuilder
            .extracting(
                itemCreator.fromTagKey(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS),
                resultHelper.item(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SALTPETER),
            ).saveSuffixed(output, "_from_sandstone")

        HTItemWithCatalystRecipeBuilder
            .extracting(
                itemCreator.fromTagKey(Tags.Items.GUNPOWDERS),
                resultHelper.item(CommonMaterialPrefixes.DUST, CommonMaterialKeys.Gems.SULFUR),
            ).saveSuffixed(output, "_from_gunpowder")

        dyes()
    }

    @Suppress("DEPRECATION")
    @JvmStatic
    private fun dyes() {
        // Charcoal -> Brown
        HTItemWithCatalystRecipeBuilder
            .extracting(
                itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.CHARCOAL),
                resultHelper.item(Items.BROWN_DYE),
            ).saveSuffixed(output, "_from_charcoal")
        // Coal -> Black
        HTItemWithCatalystRecipeBuilder
            .extracting(
                itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.COAL),
                resultHelper.item(Items.BLACK_DYE),
            ).saveSuffixed(output, "_from_coal")

        for (color: DyeColor in DyeColor.entries) {
            val name: String = color.serializedName
            val dye: DyeItem = DyeItem.byColor(color)

            HTItemWithCatalystRecipeBuilder
                .extracting(
                    itemCreator.fromTagKey(CommonMaterialPrefixes.RAW_MATERIAL.itemTagKey("dyes/$name")),
                    resultHelper.item(dye, 2),
                ).saveSuffixed(output, "_from_$name")
        }
    }
}
