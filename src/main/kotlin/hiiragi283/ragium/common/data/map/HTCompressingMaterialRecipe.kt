package hiiragi283.ragium.common.data.map

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.data.map.HTMaterialRecipe
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.prefix.HTMaterialPrefix
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.util.wrapOptional
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.impl.data.recipe.HTItemWithCatalystRecipeBuilder
import net.minecraft.util.ExtraCodecs
import java.util.Optional

@JvmRecord
data class HTCompressingMaterialRecipe(
    private val inputPrefix: HTMaterialPrefix,
    private val inputCount: Int,
    private val outputPrefix: HTMaterialPrefix,
    private val outputCount: Int,
    private val catalyst: Optional<HTItemIngredient>,
) : HTMaterialRecipe {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTCompressingMaterialRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTMaterialPrefix.CODEC.codec
                        .fieldOf("input_prefix")
                        .forGetter(HTCompressingMaterialRecipe::inputPrefix),
                    ExtraCodecs.POSITIVE_INT.optionalFieldOf("input_count", 1).forGetter(HTCompressingMaterialRecipe::inputCount),
                    HTMaterialPrefix.CODEC.codec
                        .fieldOf("output_prefix")
                        .forGetter(HTCompressingMaterialRecipe::outputPrefix),
                    ExtraCodecs.POSITIVE_INT.optionalFieldOf("output_count", 1).forGetter(HTCompressingMaterialRecipe::outputCount),
                    HTItemIngredient.CODEC.codec
                        .optionalFieldOf("catalyst")
                        .forGetter(HTCompressingMaterialRecipe::catalyst),
                ).apply(instance, ::HTCompressingMaterialRecipe)
        }

        @JvmStatic
        fun dust(
            outputPrefix: HTPrefixLike,
            catalyst: HTItemIngredient?,
            inputCount: Int = 1,
            outputCount: Int = 1,
        ): HTCompressingMaterialRecipe =
            HTCompressingMaterialRecipe(CommonMaterialPrefixes.DUST, inputCount, outputPrefix, outputCount, catalyst)

        @JvmStatic
        fun ingot(
            outputPrefix: HTPrefixLike,
            catalyst: HTItemIngredient?,
            inputCount: Int = 1,
            outputCount: Int = 1,
        ): HTCompressingMaterialRecipe =
            HTCompressingMaterialRecipe(CommonMaterialPrefixes.INGOT, inputCount, outputPrefix, outputCount, catalyst)
    }

    constructor(
        inputPrefix: HTPrefixLike,
        inputCount: Int,
        outputPrefix: HTPrefixLike,
        outputCount: Int,
        catalyst: HTItemIngredient?,
    ) : this(
        inputPrefix.asMaterialPrefix(),
        inputCount,
        outputPrefix.asMaterialPrefix(),
        outputCount,
        catalyst.wrapOptional(),
    )

    override fun type(): MapCodec<out HTMaterialRecipe> = CODEC

    override fun generateRecipes(helper: HTMaterialRecipe.Helper) {
        for (key: HTMaterialKey in helper.getAllMaterials()) {
            if (!helper.isPresentTag(inputPrefix, key)) continue
            if (!helper.isPresentTag(outputPrefix, key)) continue

            HTItemWithCatalystRecipeBuilder
                .compressing(
                    helper.itemCreator.fromTagKey(inputPrefix, key, inputCount),
                    catalyst,
                ).setResult(helper.resultHelper.item(outputPrefix, key, outputCount))
                .saveSuffixed(helper.output, "_from_${inputPrefix.name}")
        }
    }
}
