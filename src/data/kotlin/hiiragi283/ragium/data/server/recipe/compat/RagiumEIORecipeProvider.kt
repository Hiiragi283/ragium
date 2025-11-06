package hiiragi283.ragium.data.server.recipe.compat

import com.enderio.base.common.tag.EIOTags
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.common.material.CommonMaterialKeys
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.ModMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.impl.data.recipe.HTCombineItemToObjRecipeBuilder
import net.minecraft.tags.ItemTags
import net.neoforged.neoforge.common.Tags

object RagiumEIORecipeProvider : HTRecipeProvider.Integration(RagiumConst.EIO_BASE) {
    override fun buildRecipeInternal() {
        alloys()
    }

    private fun alloys() {
        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.CONDUCTIVE_ALLOY),
                itemCreator.ingotOrDust(ModMaterialKeys.Alloys.COPPER_ALLOY),
                itemCreator.ingotOrDust(VanillaMaterialKeys.IRON),
                itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.REDSTONE),
            ).save(output)

        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.COPPER_ALLOY),
                itemCreator.ingotOrDust(VanillaMaterialKeys.COPPER),
                itemCreator.fromTagKey(EIOTags.Items.SILICON),
            ).save(output)

        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.DARK_STEEL),
                itemCreator.ingotOrDust(VanillaMaterialKeys.IRON),
                itemCreator.fuelOrDust(VanillaMaterialKeys.COAL, 2),
                itemCreator.fromTagKey(Tags.Items.OBSIDIANS),
            ).save(output)

        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.DARK_STEEL),
                itemCreator.ingotOrDust(VanillaMaterialKeys.IRON),
                itemCreator.fuelOrDust(CommonMaterialKeys.COAL_COKE),
                itemCreator.fromTagKey(Tags.Items.OBSIDIANS),
            ).saveSuffixed(output, "_from_coal_coke")

        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.END_STEEL),
                itemCreator.fromTagKey(Tags.Items.END_STONES),
                itemCreator.ingotOrDust(ModMaterialKeys.Alloys.DARK_STEEL),
                itemCreator.fromTagKey(Tags.Items.OBSIDIANS),
            ).save(output)

        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.ENERGETIC_ALLOY),
                itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.REDSTONE),
                itemCreator.ingotOrDust(VanillaMaterialKeys.GOLD),
                itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.GLOWSTONE),
            ).save(output)

        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.PULSATING_ALLOY),
                itemCreator.ingotOrDust(VanillaMaterialKeys.IRON),
                itemCreator.fromTagKey(Tags.Items.ENDER_PEARLS),
            ).save(output)

        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.REDSTONE_ALLOY),
                itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.REDSTONE),
                itemCreator.fromTagKey(EIOTags.Items.SILICON),
            ).save(output)

        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.SOULARIUM),
                itemCreator.fromTagKey(ItemTags.SOUL_FIRE_BASE_BLOCKS),
                itemCreator.ingotOrDust(VanillaMaterialKeys.GOLD),
            ).save(output)

        HTCombineItemToObjRecipeBuilder
            .alloying(
                resultHelper.item(CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.VIBRANT_ALLOY),
                itemCreator.ingotOrDust(ModMaterialKeys.Alloys.ENERGETIC_ALLOY),
                itemCreator.fromTagKey(Tags.Items.ENDER_PEARLS),
            ).save(output)
    }
}
