package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.common.HTMoldType
import hiiragi283.ragium.common.data.recipe.HTCompressingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumCompressingRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        // Sand -> Sandstone
        HTCompressingRecipeBuilder
            .block(
                itemCreator.fromTagKey(Tags.Items.SANDS_COLORLESS, 4),
                resultHelper.item(Items.SANDSTONE),
            ).save(output)
        HTCompressingRecipeBuilder
            .block(
                itemCreator.fromTagKey(Tags.Items.SANDS_RED, 4),
                resultHelper.item(Items.RED_SANDSTONE),
            ).save(output)

        // Clay -> Mud
        HTCompressingRecipeBuilder
            .block(
                itemCreator.fromItem(Items.CLAY),
                resultHelper.item(Items.MUD),
            ).save(output)
        // Mud -> Packed Mud
        HTCompressingRecipeBuilder
            .block(
                itemCreator.fromItem(Items.MUD),
                resultHelper.item(Items.PACKED_MUD),
            ).save(output)

        // Snow -> Ice
        HTCompressingRecipeBuilder
            .block(
                itemCreator.fromItem(Items.SNOW_BLOCK, 4),
                resultHelper.item(Items.ICE),
            ).save(output)

        // Moss
        HTCompressingRecipeBuilder
            .block(
                itemCreator.fromItems(listOf(Items.VINE, Items.MOSS_CARPET), count = 8),
                resultHelper.item(Items.MOSS_BLOCK),
            ).save(output)
        // Sculk
        HTCompressingRecipeBuilder
            .block(
                itemCreator.fromItem(Items.SCULK_VEIN, 8),
                resultHelper.item(Items.SCULK),
            ).save(output)

        // Paper
        HTCompressingRecipeBuilder
            .plate(
                itemCreator.fromTagKey(Tags.Items.CROPS_SUGAR_CANE),
                resultHelper.item(Items.PAPER, 2),
            ).save(output)

        // Sawdust -> Compressed
        HTCompressingRecipeBuilder
            .ingot(
                itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.WOOD, 8),
                resultHelper.item(RagiumItems.COMPRESSED_SAWDUST),
            ).save(output)
        // Sawdust -> Wood Plate
        compressingTo(HTMoldType.PLATE, VanillaMaterialKeys.WOOD, CommonMaterialPrefixes.DUST)

        // Coal -> Diamond
        HTCompressingRecipeBuilder
            .ingot(
                itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.COAL, 8),
                resultHelper.item(RagiumItems.COAL_CHIP),
            ).save(output)

        HTShapedRecipeBuilder
            .create(RagiumItems.COAL_CHUNK)
            .hollow8()
            .define('A', RagiumItems.COAL_CHIP)
            .define('B', CommonMaterialPrefixes.NUGGET, RagiumMaterialKeys.NIGHT_METAL)
            .save(output)

        HTCompressingRecipeBuilder
            .gem(
                itemCreator.fromItem(RagiumItems.COAL_CHUNK),
                resultHelper.item(CommonMaterialPrefixes.GEM, VanillaMaterialKeys.DIAMOND),
            ).saveSuffixed(output, "_from_coal")
    }
}
