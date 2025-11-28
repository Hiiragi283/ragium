package hiiragi283.ragium.common.data.map

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.data.map.HTMaterialRecipe
import hiiragi283.ragium.api.material.HTMaterialDefinition
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.attribute.HTStorageBlockMaterialAttribute
import hiiragi283.ragium.api.material.get
import hiiragi283.ragium.api.material.getDefaultPrefix
import hiiragi283.ragium.api.material.prefix.HTMaterialPrefix
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.impl.data.recipe.HTItemToObjRecipeBuilder
import kotlin.collections.iterator

data object HTBlockCrushingMaterialRecipe : HTMaterialRecipe {
    @JvmField
    val CODEC: MapCodec<HTBlockCrushingMaterialRecipe> = MapCodec.unit { HTBlockCrushingMaterialRecipe }

    override fun type(): MapCodec<HTBlockCrushingMaterialRecipe> = CODEC

    override fun generateRecipes(helper: HTMaterialRecipe.Helper) {
        for ((key: HTMaterialKey, definition: HTMaterialDefinition) in helper.getDefinitions()) {
            val basePrefix: HTMaterialPrefix = definition.getDefaultPrefix() ?: continue
            val resultPrefix: HTPrefixLike = when {
                basePrefix.isOf(CommonMaterialPrefixes.GEM) -> CommonMaterialPrefixes.GEM
                basePrefix.isOf(CommonMaterialPrefixes.FUEL) -> CommonMaterialPrefixes.FUEL
                else -> CommonMaterialPrefixes.DUST
            }

            val storageBlock: HTStorageBlockMaterialAttribute = if (basePrefix.isOf(CommonMaterialPrefixes.INGOT)) {
                HTStorageBlockMaterialAttribute.THREE_BY_THREE
            } else {
                definition.get<HTStorageBlockMaterialAttribute>() ?: continue
            }

            if (!helper.isPresentTag(CommonMaterialPrefixes.STORAGE_BLOCK, key)) continue
            if (!helper.isPresentTag(CommonMaterialPrefixes.DUST, key)) continue

            HTItemToObjRecipeBuilder
                .pulverizing(
                    helper.itemCreator.fromTagKey(CommonMaterialPrefixes.STORAGE_BLOCK, key),
                    helper.resultHelper.item(resultPrefix, key, storageBlock.baseCount),
                ).saveSuffixed(helper.output, "_from_storage_block")
        }
    }
}
