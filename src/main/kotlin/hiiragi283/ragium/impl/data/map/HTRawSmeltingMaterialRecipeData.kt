package hiiragi283.ragium.impl.data.map

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.map.HTMaterialRecipeData
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.ingredient.HTItemIngredientCreator
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialPrefix
import hiiragi283.ragium.impl.data.recipe.HTCombineItemToObjRecipeBuilder
import hiiragi283.ragium.setup.CommonMaterialPrefixes
import net.minecraft.core.registries.Registries
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.TagKey
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.item.Item

class HTRawSmeltingMaterialRecipeData(
    val prefix: HTMaterialPrefix,
    val inputCount: Int,
    val outputCount: Int,
    val flux: TagKey<Item>,
    val fluxCount: Int,
) : HTMaterialRecipeData {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTRawSmeltingMaterialRecipeData> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    RagiumAPI.MATERIAL_PREFIX_REGISTRY
                        .byNameCodec()
                        .fieldOf("prefix")
                        .forGetter(HTRawSmeltingMaterialRecipeData::prefix),
                    ExtraCodecs.POSITIVE_INT.fieldOf("input_count").forGetter(HTRawSmeltingMaterialRecipeData::inputCount),
                    ExtraCodecs.POSITIVE_INT.fieldOf("output_count").forGetter(HTRawSmeltingMaterialRecipeData::outputCount),
                    TagKey.hashedCodec(Registries.ITEM).fieldOf("flux").forGetter(HTRawSmeltingMaterialRecipeData::flux),
                    ExtraCodecs.POSITIVE_INT.fieldOf("flux_count").forGetter(HTRawSmeltingMaterialRecipeData::fluxCount),
                ).apply(instance, ::HTRawSmeltingMaterialRecipeData)
        }
    }

    override fun type(): MapCodec<out HTMaterialRecipeData> = CODEC

    override fun generateRecipes(helper: HTMaterialRecipeData.Helper) {
        val itemCreator: HTItemIngredientCreator = helper.itemCreator
        val resultHelper: HTResultHelper = helper.resultHelper
        val output: RecipeOutput = helper.output
        for (key: HTMaterialKey in helper.getAllMaterials()) {
            val ingot: TagKey<Item> = CommonMaterialPrefixes.INGOT.itemTagKey(key)
            if (!helper.isPresentTag(CommonMaterialPrefixes.INGOT, key)) continue
            if (helper.isPresentTag(prefix, key)) {
                HTCombineItemToObjRecipeBuilder
                    .alloying(
                        resultHelper.item(ingot, outputCount),
                        itemCreator.fromTagKey(prefix, key, inputCount),
                        itemCreator.fromTagKey(flux, fluxCount),
                    ).tagCondition(ingot)
                    .saveSuffixed(output, "_from_${prefix.name}_with_${flux.location.path}")
            }
        }
    }
}
