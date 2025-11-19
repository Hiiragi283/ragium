package hiiragi283.ragium.common.data.map

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.data.map.HTMaterialRecipe
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.data.recipe.ingredient.HTItemIngredientCreator
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.prefix.HTMaterialPrefix
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.impl.data.recipe.HTCombineItemToObjRecipeBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.TagKey
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.item.Item

class HTRawSmeltingMaterialRecipe(
    val prefix: HTMaterialPrefix,
    val inputCount: Int,
    val outputCount: Int,
    val flux: TagKey<Item>,
    val fluxCount: Int,
) : HTMaterialRecipe {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTRawSmeltingMaterialRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTMaterialPrefix.CODEC
                        .codec
                        .fieldOf("prefix")
                        .forGetter(HTRawSmeltingMaterialRecipe::prefix),
                    ExtraCodecs.POSITIVE_INT.fieldOf("input_count").forGetter(HTRawSmeltingMaterialRecipe::inputCount),
                    ExtraCodecs.POSITIVE_INT.fieldOf("output_count").forGetter(HTRawSmeltingMaterialRecipe::outputCount),
                    TagKey.hashedCodec(Registries.ITEM).fieldOf("flux").forGetter(HTRawSmeltingMaterialRecipe::flux),
                    ExtraCodecs.POSITIVE_INT.fieldOf("flux_count").forGetter(HTRawSmeltingMaterialRecipe::fluxCount),
                ).apply(instance, ::HTRawSmeltingMaterialRecipe)
        }
    }

    constructor(
        prefix: HTPrefixLike,
        inputCount: Int,
        outputCount: Int,
        flux: TagKey<Item>,
        fluxCount: Int,
    ) : this(prefix.asMaterialPrefix(), inputCount, outputCount, flux, fluxCount)

    override fun type(): MapCodec<out HTMaterialRecipe> = CODEC

    override fun generateRecipes(helper: HTMaterialRecipe.Helper) {
        val itemCreator: HTItemIngredientCreator = helper.itemCreator
        val resultHelper: HTResultHelper = helper.resultHelper
        val output: RecipeOutput = helper.output
        for (key: HTMaterialKey in helper.getAllMaterials()) {
            if (!helper.isPresentTag(CommonMaterialPrefixes.INGOT, key)) continue
            if (helper.isPresentTag(prefix, key)) {
                HTCombineItemToObjRecipeBuilder
                    .alloying(
                        resultHelper.item(CommonMaterialPrefixes.INGOT, key, outputCount),
                        itemCreator.fromTagKey(prefix, key, inputCount),
                        itemCreator.fromTagKey(flux, fluxCount),
                    ).saveSuffixed(output, "_from_${prefix.name}_with_${flux.location.path}")
            }
        }
    }
}
