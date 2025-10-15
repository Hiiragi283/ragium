package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.common.material.HTItemMaterialVariant
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.impl.data.recipe.HTItemToObjRecipeBuilder
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

object RagiumCompressingRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        // Sand -> Sandstone
        HTItemToObjRecipeBuilder
            .compressing(
                itemCreator.fromTagKey(Tags.Items.SANDS_COLORLESS, 4),
                resultHelper.item(Items.SANDSTONE),
            ).save(output)
        HTItemToObjRecipeBuilder
            .compressing(
                itemCreator.fromTagKey(Tags.Items.SANDS_RED, 4),
                resultHelper.item(Items.RED_SANDSTONE),
            ).save(output)

        // Clay -> Mud
        HTItemToObjRecipeBuilder
            .compressing(
                itemCreator.fromItem(Items.CLAY),
                resultHelper.item(Items.MUD),
            ).save(output)
        // Mud -> Packed Mud
        HTItemToObjRecipeBuilder
            .compressing(
                itemCreator.fromItem(Items.MUD),
                resultHelper.item(Items.PACKED_MUD),
            ).save(output)

        // Snow -> Ice
        HTItemToObjRecipeBuilder
            .compressing(
                itemCreator.fromItem(Items.SNOW_BLOCK, 4),
                resultHelper.item(Items.ICE),
            ).save(output)

        // Moss
        HTItemToObjRecipeBuilder
            .compressing(
                itemCreator.fromItems(Items.VINE, Items.MOSS_CARPET, count = 8),
                resultHelper.item(Items.MOSS_BLOCK),
            ).save(output)
        // Sculk
        HTItemToObjRecipeBuilder
            .compressing(
                itemCreator.fromItem(Items.SCULK_VEIN, 8),
                resultHelper.item(Items.SCULK),
            ).save(output)

        // TNT
        HTItemToObjRecipeBuilder
            .compressing(
                itemCreator.fromItem(Items.GUNPOWDER, 4),
                resultHelper.item(Items.TNT),
            ).save(output)

        // Paper
        HTItemToObjRecipeBuilder
            .compressing(
                itemCreator.fromTagKey(Tags.Items.CROPS_SUGAR_CANE),
                resultHelper.item(Items.PAPER, 2),
            ).save(output)

        // Sawdust -> Compressed
        HTItemToObjRecipeBuilder
            .compressing(
                itemCreator.fromTagKey(HTItemMaterialVariant.DUST, HTVanillaMaterialType.WOOD, 8),
                resultHelper.item(RagiumItems.COMPRESSED_SAWDUST),
            ).save(output)
        // Coal -> Diamond
        HTItemToObjRecipeBuilder
            .compressing(
                itemCreator.fuelOrDust(HTVanillaMaterialType.COAL, 64),
                resultHelper.item(HTItemMaterialVariant.GEM, HTVanillaMaterialType.DIAMOND),
            ).saveSuffixed(output, "_from_coal")

        // Basalt Mesh
        HTItemToObjRecipeBuilder
            .compressing(
                itemCreator.fromItem(Items.BASALT, 8),
                resultHelper.item(RagiumItems.BASALT_MESH),
            ).save(output)
    }
}
