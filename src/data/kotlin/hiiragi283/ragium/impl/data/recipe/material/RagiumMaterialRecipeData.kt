package hiiragi283.ragium.impl.data.recipe.material

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeData
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.tags.ItemTags
import net.neoforged.neoforge.common.Tags

data object RagiumMaterialRecipeData {
    //    Raginite    //

    @JvmField
    val RAGINITE_ORE: HTRecipeData = HTRecipeData.create {
        addInput(CommonMaterialPrefixes.ORE, RagiumMaterialKeys.RAGINITE)

        addOutput(RagiumItems.getDust(RagiumMaterialKeys.RAGINITE), CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE, 8)
        addOutput(RagiumItems.getDust(RagiumMaterialKeys.RAGINITE), CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE, 4, 1 / 2f)
        addOutput(
            RagiumItems.getGem(RagiumMaterialKeys.RAGI_CRYSTAL),
            CommonMaterialPrefixes.GEM,
            RagiumMaterialKeys.RAGI_CRYSTAL,
            1,
            1 / 4f,
        )
        setSuffix("_from_ore")
    }

    @JvmField
    val RAGI_ALLOY: HTRecipeData = HTRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.COPPER)
        addInput(CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE, 2)

        addOutput(RagiumItems.getIngot(RagiumMaterialKeys.RAGI_ALLOY), CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.RAGI_ALLOY)
    }

    @JvmField
    val ADVANCED_RAGI_ALLOY: HTRecipeData = HTRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.GOLD)
        addInput(CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE, 4)

        addOutput(
            RagiumItems.getIngot(RagiumMaterialKeys.ADVANCED_RAGI_ALLOY),
            CommonMaterialPrefixes.INGOT,
            RagiumMaterialKeys.ADVANCED_RAGI_ALLOY,
        )
    }

    @JvmField
    val RAGI_CRYSTAL_ORE: HTRecipeData = createGemOre(RagiumMaterialKeys.RAGI_CRYSTAL)

    @JvmField
    val RAGI_CRYSTAL: HTRecipeData = HTRecipeData.create {
        gemOrDust(VanillaMaterialKeys.DIAMOND)
        addInput(CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE, 6)

        addOutput(RagiumItems.getGem(RagiumMaterialKeys.RAGI_CRYSTAL), CommonMaterialPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL)
    }

    //    Azure    //

    @JvmField
    val AZURE_SHARD: HTRecipeData = HTRecipeData.create {
        gemOrDust(VanillaMaterialKeys.AMETHYST)
        gemOrDust(VanillaMaterialKeys.LAPIS)

        addOutput(RagiumItems.getGem(RagiumMaterialKeys.AZURE), CommonMaterialPrefixes.GEM, RagiumMaterialKeys.AZURE, 2)
    }

    @JvmField
    val AZURE_STEEL: HTRecipeData = HTRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.IRON)
        gemOrDust(RagiumMaterialKeys.AZURE, 2)

        addOutput(RagiumItems.getIngot(RagiumMaterialKeys.AZURE_STEEL), CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.AZURE_STEEL)
    }

    //    Deep    //

    @JvmField
    val DEEP_SCRAP: HTRecipeData = HTRecipeData.create {
        addInput(RagiumCommonTags.Items.ORES_DEEP_SCRAP)

        addOutput(RagiumItems.getScrap(RagiumMaterialKeys.DEEP_STEEL), CommonMaterialPrefixes.SCRAP, RagiumMaterialKeys.DEEP_STEEL, 2)
    }

    @JvmField
    val DEEP_STEEL: HTRecipeData = HTRecipeData.create {
        ingotOrDust(RagiumMaterialKeys.AZURE_STEEL, 4)
        addInput(CommonMaterialPrefixes.SCRAP, RagiumMaterialKeys.DEEP_STEEL, 4)

        addOutput(RagiumItems.getIngot(RagiumMaterialKeys.DEEP_STEEL), CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.DEEP_STEEL)
    }

    //    Crimson    //

    @JvmField
    val CRIMSON_ORE: HTRecipeData = createGemOre(RagiumMaterialKeys.CRIMSON_CRYSTAL)

    @JvmField
    val CRIMSON_SAP: HTRecipeData = HTRecipeData.create {
        addInput(ItemTags.CRIMSON_STEMS)

        addOutput(RagiumFluidContents.CRIMSON_SAP, RagiumConst.LOG_TO_SAP)

        setSuffix("_from_stem")
    }

    @JvmField
    val CRIMSON_BLOOD: HTRecipeData = HTRecipeData.create {
        addInput(RagiumFluidContents.CRIMSON_SAP, 1000)

        addOutput(RagiumFluidContents.CRIMSON_BLOOD, RagiumConst.SAP_TO_MOLTEN)

        setSuffix("_from_sap")
    }

    @JvmField
    val CRIMSON_CRYSTAL: HTRecipeData = createGemCast(RagiumMaterialKeys.CRIMSON_CRYSTAL, RagiumFluidContents.CRIMSON_BLOOD)

    //    Warped    //

    @JvmField
    val WARPED_ORE: HTRecipeData = createGemOre(RagiumMaterialKeys.WARPED_CRYSTAL)

    @JvmField
    val WARPED_SAP: HTRecipeData = HTRecipeData.create {
        addInput(ItemTags.WARPED_STEMS)

        addOutput(RagiumFluidContents.WARPED_SAP, RagiumConst.LOG_TO_SAP)

        setSuffix("_from_stem")
    }

    @JvmField
    val DEW_OF_THE_WARP: HTRecipeData = HTRecipeData.create {
        addInput(RagiumFluidContents.WARPED_SAP, 1000)

        addOutput(RagiumFluidContents.DEW_OF_THE_WARP, RagiumConst.SAP_TO_MOLTEN)

        setSuffix("_from_sap")
    }

    @JvmField
    val WARPED_CRYSTAL: HTRecipeData = createGemCast(RagiumMaterialKeys.WARPED_CRYSTAL, RagiumFluidContents.DEW_OF_THE_WARP)

    //    Eldritch    //

    @JvmField
    val ELDRITCH_FLUX: HTRecipeData = HTRecipeData.create {
        addInput(RagiumModTags.Items.ELDRITCH_PEARL_BINDER)
        addInput(RagiumFluidContents.CRIMSON_BLOOD, RagiumConst.MOLTEN_TO_GEM / 2)
        addInput(RagiumFluidContents.DEW_OF_THE_WARP, RagiumConst.MOLTEN_TO_GEM / 2)

        addOutput(RagiumFluidContents.ELDRITCH_FLUX, RagiumConst.MOLTEN_TO_GEM)
    }

    @JvmField
    val ELDRITCH_FLUX_CRIMSON: HTRecipeData = HTRecipeData.create {
        addInput(RagiumModTags.Items.ELDRITCH_PEARL_BINDER, count = 2)
        gemOrDust(RagiumMaterialKeys.CRIMSON_CRYSTAL)
        addInput(RagiumFluidContents.DEW_OF_THE_WARP, RagiumConst.MOLTEN_TO_GEM)

        addOutput(RagiumFluidContents.ELDRITCH_FLUX, RagiumConst.MOLTEN_TO_GEM * 2)
        addOutput(RagiumItems.getGem(RagiumMaterialKeys.ELDRITCH_PEARL), CommonMaterialPrefixes.GEM, RagiumMaterialKeys.ELDRITCH_PEARL)

        setSuffix("_crimson")
    }

    @JvmField
    val ELDRITCH_FLUX_WARPED: HTRecipeData = HTRecipeData.create {
        addInput(RagiumModTags.Items.ELDRITCH_PEARL_BINDER, count = 2)
        gemOrDust(RagiumMaterialKeys.WARPED_CRYSTAL)
        addInput(RagiumFluidContents.CRIMSON_BLOOD, RagiumConst.MOLTEN_TO_GEM)

        addOutput(RagiumFluidContents.ELDRITCH_FLUX, RagiumConst.MOLTEN_TO_GEM * 2)
        addOutput(RagiumItems.getGem(RagiumMaterialKeys.ELDRITCH_PEARL), CommonMaterialPrefixes.GEM, RagiumMaterialKeys.ELDRITCH_PEARL)

        setSuffix("_warped")
    }

    @JvmField
    val ELDRITCH_PEARL: HTRecipeData = createGemCast(RagiumMaterialKeys.ELDRITCH_PEARL, RagiumFluidContents.ELDRITCH_FLUX)

    //    Other    //

    @JvmField
    val NIGHT_METAL: HTRecipeData = HTRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.GOLD)
        addInput(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.OBSIDIAN, 4)
        addInput(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.BLACKSTONE, 4)

        addOutput(RagiumItems.getIngot(RagiumMaterialKeys.NIGHT_METAL), CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.NIGHT_METAL)
    }

    @JvmField
    val IRIDESCENTIUM: HTRecipeData = HTRecipeData.create {
        ingotOrDust(RagiumMaterialKeys.NIGHT_METAL, 4)
        gemOrDust(RagiumMaterialKeys.ELDRITCH_PEARL, 4)
        addInput(Tags.Items.NETHER_STARS)

        addOutput(RagiumItems.getIngot(RagiumMaterialKeys.IRIDESCENTIUM), CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.IRIDESCENTIUM)
    }

    @JvmStatic
    private fun createGemOre(material: HTMaterialLike): HTRecipeData = HTRecipeData.create {
        addInput(CommonMaterialPrefixes.ORE, material)

        addOutput(RagiumItems.getGem(material), CommonMaterialPrefixes.GEM, material, 2)
        setSuffix("_from_ore")
    }

    @JvmStatic
    private fun createGemCast(material: HTMaterialLike, fluid: HTFluidContent<*, *, *>): HTRecipeData = HTRecipeData.create {
        addInput(fluid, RagiumConst.MOLTEN_TO_GEM)

        setCatalyst(RagiumItems.getMold(CommonMaterialPrefixes.GEM))

        addOutput(
            RagiumItems.getGem(material),
            CommonMaterialPrefixes.GEM,
            material,
        )
    }
}
