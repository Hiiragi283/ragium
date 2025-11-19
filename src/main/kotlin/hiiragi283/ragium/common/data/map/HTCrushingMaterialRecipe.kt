package hiiragi283.ragium.common.data.map

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.data.map.HTMaterialRecipe
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.prefix.HTMaterialPrefix
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.impl.data.recipe.HTItemToObjRecipeBuilder
import net.minecraft.util.ExtraCodecs

@JvmRecord
data class HTCrushingMaterialRecipe(
    private val inputPrefix: HTMaterialPrefix,
    private val inputCount: Int,
    private val outputPrefix: HTMaterialPrefix,
    private val outputCount: Int,
) : HTMaterialRecipe {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTCrushingMaterialRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTMaterialPrefix.CODEC.codec
                        .fieldOf("input_prefix")
                        .forGetter(HTCrushingMaterialRecipe::inputPrefix),
                    ExtraCodecs.POSITIVE_INT.optionalFieldOf("input_count", 1).forGetter(HTCrushingMaterialRecipe::inputCount),
                    HTMaterialPrefix.CODEC.codec
                        .fieldOf("output_prefix")
                        .forGetter(HTCrushingMaterialRecipe::outputPrefix),
                    ExtraCodecs.POSITIVE_INT.optionalFieldOf("output_count", 1).forGetter(HTCrushingMaterialRecipe::outputCount),
                ).apply(instance, ::HTCrushingMaterialRecipe)
        }

        @JvmStatic
        fun dust(inputPrefix: HTPrefixLike, inputCount: Int, outputCount: Int): HTCrushingMaterialRecipe =
            HTCrushingMaterialRecipe(inputPrefix, inputCount, CommonMaterialPrefixes.DUST, outputCount)
    }

    constructor(inputPrefix: HTPrefixLike, inputCount: Int, outputPrefix: HTPrefixLike, outputCount: Int) : this(
        inputPrefix.asMaterialPrefix(),
        inputCount,
        outputPrefix.asMaterialPrefix(),
        outputCount,
    )

    override fun type(): MapCodec<out HTMaterialRecipe> = CODEC

    override fun generateRecipes(helper: HTMaterialRecipe.Helper) {
        for (key: HTMaterialKey in helper.getAllMaterials()) {
            if (!helper.isPresentTag(inputPrefix, key)) continue
            if (!helper.isPresentTag(outputPrefix, key)) continue

            HTItemToObjRecipeBuilder
                .pulverizing(
                    helper.itemCreator.fromTagKey(inputPrefix, key, inputCount),
                    helper.resultHelper.item(outputPrefix, key, outputCount),
                ).saveSuffixed(helper.output, "_from_${inputPrefix.name}")
        }
    }
}
