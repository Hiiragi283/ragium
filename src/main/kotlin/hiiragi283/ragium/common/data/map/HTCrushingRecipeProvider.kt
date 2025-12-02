package hiiragi283.ragium.common.data.map

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.data.map.HTRuntimeRecipeProvider
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.prefix.HTMaterialPrefix
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.impl.data.recipe.HTItemToObjRecipeBuilder
import net.minecraft.util.ExtraCodecs

@JvmRecord
data class HTCrushingRecipeProvider(
    private val inputPrefix: HTMaterialPrefix,
    private val inputCount: Int,
    private val outputPrefix: HTMaterialPrefix,
    private val outputCount: Int,
) : HTRuntimeRecipeProvider {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTCrushingRecipeProvider> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTMaterialPrefix.CODEC.codec
                        .fieldOf("input_prefix")
                        .forGetter(HTCrushingRecipeProvider::inputPrefix),
                    ExtraCodecs.POSITIVE_INT.optionalFieldOf("input_count", 1).forGetter(HTCrushingRecipeProvider::inputCount),
                    HTMaterialPrefix.CODEC.codec
                        .fieldOf("output_prefix")
                        .forGetter(HTCrushingRecipeProvider::outputPrefix),
                    ExtraCodecs.POSITIVE_INT.optionalFieldOf("output_count", 1).forGetter(HTCrushingRecipeProvider::outputCount),
                ).apply(instance, ::HTCrushingRecipeProvider)
        }

        @JvmStatic
        fun dust(inputPrefix: HTPrefixLike, inputCount: Int, outputCount: Int): HTCrushingRecipeProvider =
            HTCrushingRecipeProvider(inputPrefix, inputCount, CommonMaterialPrefixes.DUST, outputCount)
    }

    constructor(inputPrefix: HTPrefixLike, inputCount: Int, outputPrefix: HTPrefixLike, outputCount: Int) : this(
        inputPrefix.asMaterialPrefix(),
        inputCount,
        outputPrefix.asMaterialPrefix(),
        outputCount,
    )

    override fun type(): MapCodec<out HTRuntimeRecipeProvider> = CODEC

    override fun generateRecipes(helper: HTRuntimeRecipeProvider.Helper) {
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
