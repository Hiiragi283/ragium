package hiiragi283.ragium.impl.data.map

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.data.map.HTMaterialRecipeData
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialPrefix
import hiiragi283.ragium.impl.data.recipe.HTItemToObjRecipeBuilder
import hiiragi283.ragium.setup.CommonMaterialPrefixes
import net.minecraft.util.ExtraCodecs

@JvmRecord
data class HTCrushingMaterialRecipeData(private val prefix: HTMaterialPrefix, private val inputCount: Int, private val outputCount: Int) :
    HTMaterialRecipeData {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTCrushingMaterialRecipeData> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTMaterialPrefix.CODEC.codec
                        .fieldOf("prefix")
                        .forGetter(HTCrushingMaterialRecipeData::prefix),
                    ExtraCodecs.POSITIVE_INT.optionalFieldOf("input_count", 1).forGetter(HTCrushingMaterialRecipeData::inputCount),
                    ExtraCodecs.POSITIVE_INT.optionalFieldOf("output_count", 1).forGetter(HTCrushingMaterialRecipeData::outputCount),
                ).apply(instance, ::HTCrushingMaterialRecipeData)
        }
    }

    override fun type(): MapCodec<out HTMaterialRecipeData> = CODEC

    override fun generateRecipes(helper: HTMaterialRecipeData.Helper) {
        for (key: HTMaterialKey in helper.getAllMaterials()) {
            if (!helper.isPresentTag(prefix, key)) continue
            if (!helper.isPresentTag(CommonMaterialPrefixes.DUST, key)) continue

            HTItemToObjRecipeBuilder
                .pulverizing(
                    helper.itemCreator.fromTagKey(prefix, key, inputCount),
                    helper.resultHelper.item(CommonMaterialPrefixes.DUST, key, outputCount),
                ).saveSuffixed(helper.output, "_from_${prefix.name}")
        }
    }
}
