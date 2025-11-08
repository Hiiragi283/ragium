package hiiragi283.ragium.impl.data.recipe.material

import hiiragi283.ragium.api.data.recipe.material.HTMaterialRecipeData
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.setup.RagiumItems
import net.neoforged.neoforge.common.Tags

data object RagiumMaterialRecipeData {
    //    Raginite    //

    @JvmField
    val RAGI_ALLOY: HTMaterialRecipeData = HTMaterialRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.COPPER)
        addInput(CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE, 2)

        addOutput(RagiumItems.getIngot(RagiumMaterialKeys.RAGI_ALLOY))
        addOutput(CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.RAGI_ALLOY)
    }

    @JvmField
    val ADVANCED_RAGI_ALLOY: HTMaterialRecipeData = HTMaterialRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.GOLD)
        addInput(CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE, 4)

        addOutput(RagiumItems.getIngot(RagiumMaterialKeys.ADVANCED_RAGI_ALLOY))
        addOutput(CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.ADVANCED_RAGI_ALLOY)
    }

    @JvmField
    val RAGI_CRYSTAL: HTMaterialRecipeData = HTMaterialRecipeData.create {
        gemOrDust(VanillaMaterialKeys.DIAMOND)
        addInput(CommonMaterialPrefixes.DUST, RagiumMaterialKeys.RAGINITE, 6)

        addOutput(RagiumItems.getGem(RagiumMaterialKeys.RAGI_CRYSTAL))
        addOutput(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.RAGI_CRYSTAL)
    }

    //    Azure    //

    @JvmField
    val AZURE_SHARD: HTMaterialRecipeData = HTMaterialRecipeData.create {
        gemOrDust(VanillaMaterialKeys.AMETHYST)
        gemOrDust(VanillaMaterialKeys.LAPIS)

        addOutput(RagiumItems.getGem(RagiumMaterialKeys.AZURE), 2)
        addOutput(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.AZURE, 2)
    }

    @JvmField
    val AZURE_STEEL: HTMaterialRecipeData = HTMaterialRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.IRON)
        gemOrDust(RagiumMaterialKeys.AZURE, 2)

        addOutput(RagiumItems.getIngot(RagiumMaterialKeys.AZURE_STEEL))
        addOutput(CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.AZURE_STEEL)
    }

    //    Deep    //

    @JvmField
    val DEEP_SCRAP: HTMaterialRecipeData = HTMaterialRecipeData.create {
        addInput(RagiumCommonTags.Items.ORES_DEEP_SCRAP)

        addOutput(RagiumItems.getScrap(RagiumMaterialKeys.DEEP_STEEL))
        addOutput(CommonMaterialPrefixes.SCRAP, RagiumMaterialKeys.DEEP_STEEL, 2)
    }

    @JvmField
    val DEEP_STEEL: HTMaterialRecipeData = HTMaterialRecipeData.create {
        ingotOrDust(RagiumMaterialKeys.AZURE_STEEL, 4)
        addInput(CommonMaterialPrefixes.SCRAP, RagiumMaterialKeys.DEEP_STEEL, 4)

        addOutput(RagiumItems.getIngot(RagiumMaterialKeys.DEEP_STEEL))
        addOutput(CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.DEEP_STEEL)
    }

    //    Eldritch    //

    @JvmField
    val ELDRITCH_PEARL: HTMaterialRecipeData = HTMaterialRecipeData.create {
        addInput(RagiumModTags.Items.ELDRITCH_PEARL_BINDER)
        gemOrDust(RagiumMaterialKeys.CRIMSON_CRYSTAL)
        gemOrDust(RagiumMaterialKeys.WARPED_CRYSTAL)

        addOutput(RagiumItems.getGem(RagiumMaterialKeys.ELDRITCH_PEARL))
        addOutput(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.ELDRITCH_PEARL)
    }

    @JvmField
    val ELDRITCH_PEARL_BULK: HTMaterialRecipeData = HTMaterialRecipeData.create {
        addInput(RagiumModTags.Items.ELDRITCH_PEARL_BINDER, count = 3)
        addInput(CommonMaterialPrefixes.STORAGE_BLOCK, RagiumMaterialKeys.CRIMSON_CRYSTAL)
        addInput(CommonMaterialPrefixes.STORAGE_BLOCK, RagiumMaterialKeys.WARPED_CRYSTAL)

        addOutput(RagiumItems.getGem(RagiumMaterialKeys.ELDRITCH_PEARL), 9)
        addOutput(CommonMaterialPrefixes.GEM, RagiumMaterialKeys.ELDRITCH_PEARL, 9)
    }

    //    Other    //

    @JvmField
    val NIGHT_METAL: HTMaterialRecipeData = HTMaterialRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.GOLD)
        addInput(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.OBSIDIAN, 4)
        addInput(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.BLACKSTONE, 4)

        addOutput(RagiumItems.getIngot(RagiumMaterialKeys.NIGHT_METAL))
        addOutput(CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.NIGHT_METAL)
    }

    @JvmField
    val IRIDESCENTIUM: HTMaterialRecipeData = HTMaterialRecipeData.create {
        ingotOrDust(RagiumMaterialKeys.NIGHT_METAL, 4)
        gemOrDust(RagiumMaterialKeys.ELDRITCH_PEARL, 4)
        addInput(Tags.Items.NETHER_STARS)

        addOutput(RagiumItems.getIngot(RagiumMaterialKeys.IRIDESCENTIUM))
        addOutput(CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.IRIDESCENTIUM)
    }
}
