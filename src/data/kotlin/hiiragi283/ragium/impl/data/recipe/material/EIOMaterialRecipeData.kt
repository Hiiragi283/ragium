package hiiragi283.ragium.impl.data.recipe.material

import com.enderio.base.common.init.EIOItems
import com.enderio.base.common.tag.EIOTags
import hiiragi283.ragium.api.data.recipe.material.HTMaterialRecipeData
import hiiragi283.ragium.common.material.CommonMaterialKeys
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.ModMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import net.minecraft.tags.ItemTags
import net.neoforged.neoforge.common.Tags

data object EIOMaterialRecipeData {
    @JvmField
    val CONDUCTIVE_ALLOY: HTMaterialRecipeData = HTMaterialRecipeData.create {
        ingotOrDust(ModMaterialKeys.Alloys.COPPER_ALLOY)
        ingotOrDust(VanillaMaterialKeys.IRON)
        addInput(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.REDSTONE)

        addOutput(EIOItems.CONDUCTIVE_ALLOY_INGOT, CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.CONDUCTIVE_ALLOY)
    }

    @JvmField
    val COPPER_ALLOY: HTMaterialRecipeData = HTMaterialRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.COPPER)
        addInput(EIOTags.Items.SILICON)

        addOutput(EIOItems.COPPER_ALLOY_INGOT, CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.COPPER_ALLOY)
    }

    @JvmField
    val DARK_STEEL: HTMaterialRecipeData = HTMaterialRecipeData.create {
        ingotOrDust(CommonMaterialKeys.Alloys.STEEL)
        addInput(Tags.Items.OBSIDIANS_NORMAL)

        addOutput(EIOItems.DARK_STEEL_INGOT, CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.DARK_STEEL)
    }

    @JvmField
    val DARK_STEEL_COAL: HTMaterialRecipeData = HTMaterialRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.IRON)
        fuelOrDust(VanillaMaterialKeys.COAL, 2)
        addInput(Tags.Items.OBSIDIANS_NORMAL)

        addOutput(EIOItems.DARK_STEEL_INGOT, CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.DARK_STEEL)
        setSuffix("_with_coal")
    }

    @JvmField
    val DARK_STEEL_COKE: HTMaterialRecipeData = HTMaterialRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.IRON)
        fuelOrDust(CommonMaterialKeys.COAL_COKE)
        addInput(Tags.Items.OBSIDIANS_NORMAL)

        addOutput(EIOItems.DARK_STEEL_INGOT, CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.DARK_STEEL)
        setSuffix("_with_coke")
    }

    @JvmField
    val END_STEEL: HTMaterialRecipeData = HTMaterialRecipeData.create {
        ingotOrDust(ModMaterialKeys.Alloys.DARK_STEEL)
        addInput(Tags.Items.END_STONES)
        addInput(Tags.Items.OBSIDIANS_NORMAL)

        addOutput(EIOItems.END_STEEL_INGOT, CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.END_STEEL)
    }

    @JvmField
    val ENERGETIC_ALLOY: HTMaterialRecipeData = HTMaterialRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.GOLD)
        addInput(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.REDSTONE)
        addInput(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.GLOWSTONE)

        addOutput(EIOItems.ENERGETIC_ALLOY_INGOT, CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.ENERGETIC_ALLOY)
    }

    @JvmField
    val PULSATING_ALLOY: HTMaterialRecipeData = HTMaterialRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.IRON)
        addInput(Tags.Items.ENDER_PEARLS)

        addOutput(EIOItems.PULSATING_ALLOY_INGOT, CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.PULSATING_ALLOY)
    }

    @JvmField
    val REDSTONE_ALLOY: HTMaterialRecipeData = HTMaterialRecipeData.create {
        addInput(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.REDSTONE)
        addInput(EIOTags.Items.SILICON)

        addOutput(EIOItems.REDSTONE_ALLOY_INGOT, CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.REDSTONE_ALLOY)
    }

    @JvmField
    val SOULARIUM: HTMaterialRecipeData = HTMaterialRecipeData.create {
        ingotOrDust(VanillaMaterialKeys.GOLD)
        addInput(ItemTags.SOUL_FIRE_BASE_BLOCKS)

        addOutput(EIOItems.SOULARIUM_INGOT, CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.SOULARIUM)
    }

    @JvmField
    val VIBRANT_ALLOY: HTMaterialRecipeData = HTMaterialRecipeData.create {
        ingotOrDust(ModMaterialKeys.Alloys.ENERGETIC_ALLOY)
        addInput(Tags.Items.ENDER_PEARLS)

        addOutput(EIOItems.VIBRANT_ALLOY_INGOT, CommonMaterialPrefixes.INGOT, ModMaterialKeys.Alloys.VIBRANT_ALLOY)
    }
}
