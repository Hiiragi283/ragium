package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTIngredientHelper
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.impl.HTItemToObjRecipeBuilder
import hiiragi283.ragium.api.material.HTItemMaterialVariant
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.Tags

object RagiumCompressingRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        // Sand -> Sandstone
        HTItemToObjRecipeBuilder
            .compressing(
                HTIngredientHelper.item(Tags.Items.SANDS_COLORLESS, 4),
                HTResultHelper.INSTANCE.item(Items.SANDSTONE),
            ).save(output)
        HTItemToObjRecipeBuilder
            .compressing(
                HTIngredientHelper.item(Tags.Items.SANDS_RED, 4),
                HTResultHelper.INSTANCE.item(Items.RED_SANDSTONE),
            ).save(output)

        // Clay -> Mud
        HTItemToObjRecipeBuilder
            .compressing(
                HTIngredientHelper.item(Items.CLAY),
                HTResultHelper.INSTANCE.item(Items.MUD),
            ).save(output)
        // Mud -> Packed Mud
        HTItemToObjRecipeBuilder
            .compressing(
                HTIngredientHelper.item(Items.MUD),
                HTResultHelper.INSTANCE.item(Items.PACKED_MUD),
            ).save(output)

        // Snow -> Ice
        HTItemToObjRecipeBuilder
            .compressing(
                HTIngredientHelper.item(Items.SNOW_BLOCK, 4),
                HTResultHelper.INSTANCE.item(Items.ICE),
            ).save(output)

        // Moss
        HTItemToObjRecipeBuilder
            .compressing(
                HTIngredientHelper.item(Ingredient.of(Items.VINE, Items.MOSS_CARPET), 8),
                HTResultHelper.INSTANCE.item(Items.MOSS_BLOCK),
            ).save(output)
        // Sculk
        HTItemToObjRecipeBuilder
            .compressing(
                HTIngredientHelper.item(Items.SCULK_VEIN, 8),
                HTResultHelper.INSTANCE.item(Items.SCULK),
            ).save(output)

        // TNT
        HTItemToObjRecipeBuilder
            .compressing(
                HTIngredientHelper.item(Items.GUNPOWDER, 4),
                HTResultHelper.INSTANCE.item(Items.TNT),
            ).save(output)

        // Paper
        HTItemToObjRecipeBuilder
            .compressing(
                HTIngredientHelper.item(Tags.Items.CROPS_SUGAR_CANE),
                HTResultHelper.INSTANCE.item(Items.PAPER, 2),
            ).save(output)

        // Sawdust -> Compressed
        HTItemToObjRecipeBuilder
            .compressing(
                HTIngredientHelper.item(HTItemMaterialVariant.DUST, HTVanillaMaterialType.WOOD, 8),
                HTResultHelper.INSTANCE.item(RagiumItems.COMPRESSED_SAWDUST),
            ).save(output)
        // Coal -> Diamond
        HTItemToObjRecipeBuilder
            .compressing(
                HTIngredientHelper.fuelOrDust(HTVanillaMaterialType.COAL, 64),
                HTResultHelper.INSTANCE.item(HTItemMaterialVariant.GEM, HTVanillaMaterialType.DIAMOND),
            ).saveSuffixed(output, "_from_coal")

        // Basalt Mesh
        HTItemToObjRecipeBuilder
            .compressing(
                HTIngredientHelper.item(Items.BASALT, 8),
                HTResultHelper.INSTANCE.item(RagiumItems.BASALT_MESH),
            ).save(output)
    }
}
