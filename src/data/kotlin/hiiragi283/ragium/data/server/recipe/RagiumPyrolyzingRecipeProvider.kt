package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTPyrolyzingRecipeBuilder
import hiiragi283.ragium.setup.RagiumFluids
import net.minecraft.tags.ItemTags

object RagiumPyrolyzingRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        // Log -> Charcoal
        HTPyrolyzingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(ItemTags.LOGS_THAT_BURN, 8)
            itemResult = resultCreator.material(CommonTagPrefixes.FUEL, VanillaMaterialKeys.CHARCOAL, 8)
            fluidResult = resultCreator.create(RagiumFluids.CREOSOTE, 1000)
            recipeId suffix "_from_log"
        }
        // Compressed Sawdust -> Charcoal
        HTPyrolyzingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(HCItems.COMPRESSED_SAWDUST, 8)
            itemResult = resultCreator.material(CommonTagPrefixes.FUEL, VanillaMaterialKeys.CHARCOAL, 8)
            fluidResult = resultCreator.create(RagiumFluids.CREOSOTE, 1000)
            time /= 3
            recipeId suffix "_from_sawdust"
        }
        // Coal -> Coke + Creosote
        HTPyrolyzingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.FUEL, VanillaMaterialKeys.COAL, 8)
            itemResult = resultCreator.material(CommonTagPrefixes.FUEL, CommonMaterialKeys.COAL_COKE, 8)
            fluidResult = resultCreator.create(RagiumFluids.CREOSOTE, 2000)
        }

        HTPyrolyzingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.COAL, 8)
            itemResult = resultCreator.material(CommonTagPrefixes.DUST, CommonMaterialKeys.COAL_COKE, 8)
            fluidResult = resultCreator.create(RagiumFluids.CREOSOTE, 2000)
        }

        HTPyrolyzingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.BLOCK, VanillaMaterialKeys.COAL)
            itemResult = resultCreator.material(CommonTagPrefixes.BLOCK, CommonMaterialKeys.COAL_COKE)
            fluidResult = resultCreator.create(RagiumFluids.CREOSOTE, 2000)
        }

        // Crimson Stem -> Crimson Blood
        HTPyrolyzingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(ItemTags.CRIMSON_STEMS, 8)
            itemResult = resultCreator.material(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR)
            fluidResult = resultCreator.molten(HCMaterialKeys.CRIMSON_CRYSTAL)
            recipeId suffix "_from_crimson_stem"
        }
        // Warped Stem -> Dew of the Warp
        HTPyrolyzingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(ItemTags.WARPED_STEMS, 8)
            itemResult = resultCreator.material(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR)
            fluidResult = resultCreator.molten(HCMaterialKeys.WARPED_CRYSTAL)
            recipeId suffix "_from_warped_stem"
        }
    }
}
