package hiiragi283.ragium.common.data.map

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.data.map.HTRuntimeRecipeProvider
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
data class HTCompressingRecipeProvider(
    private val inputPrefix: HTMaterialPrefix,
    private val inputCount: Int,
    private val outputPrefix: HTMaterialPrefix,
    private val outputCount: Int,
    private val catalyst: Optional<HTItemIngredient>,
) : HTRuntimeRecipeProvider {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTCompressingRecipeProvider> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTMaterialPrefix.CODEC.codec
                        .fieldOf("input_prefix")
                        .forGetter(HTCompressingRecipeProvider::inputPrefix),
                    ExtraCodecs.POSITIVE_INT.optionalFieldOf("input_count", 1).forGetter(HTCompressingRecipeProvider::inputCount),
                    HTMaterialPrefix.CODEC.codec
                        .fieldOf("output_prefix")
                        .forGetter(HTCompressingRecipeProvider::outputPrefix),
                    ExtraCodecs.POSITIVE_INT.optionalFieldOf("output_count", 1).forGetter(HTCompressingRecipeProvider::outputCount),
                    HTItemIngredient.CODEC.codec
                        .optionalFieldOf("catalyst")
                        .forGetter(HTCompressingRecipeProvider::catalyst),
                ).apply(instance, ::HTCompressingRecipeProvider)
        }

        @JvmStatic
        fun dust(
            outputPrefix: HTPrefixLike,
            catalyst: HTItemIngredient?,
            inputCount: Int = 1,
            outputCount: Int = 1,
        ): HTCompressingRecipeProvider =
            HTCompressingRecipeProvider(CommonMaterialPrefixes.DUST, inputCount, outputPrefix, outputCount, catalyst)

        @JvmStatic
        fun ingot(
            outputPrefix: HTPrefixLike,
            catalyst: HTItemIngredient?,
            inputCount: Int = 1,
            outputCount: Int = 1,
        ): HTCompressingRecipeProvider =
            HTCompressingRecipeProvider(CommonMaterialPrefixes.INGOT, inputCount, outputPrefix, outputCount, catalyst)
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

    override fun type(): MapCodec<out HTRuntimeRecipeProvider> = CODEC

    override fun generateRecipes(helper: HTRuntimeRecipeProvider.Helper) {
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
