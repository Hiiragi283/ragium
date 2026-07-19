@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.common.data.recipe.HTItemAndFluidToItemRecipeBuilder
import hiiragi283.core.common.data.recipe.HTItemOrFluidRecipeBuilder
import hiiragi283.core.common.data.recipe.HTItemToFluidRecipeBuilder
import hiiragi283.core.common.data.recipe.HTItemToItemRecipeBuilder
import hiiragi283.core.common.data.recipe.HTItemToMultiItemRecipeBuilder
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTBathingRecipe
import hiiragi283.ragium.common.recipe.HTCompressingRecipe
import hiiragi283.ragium.common.recipe.HTCuttingRecipe
import hiiragi283.ragium.common.recipe.HTImplodingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import hiiragi283.ragium.common.recipe.HTWashingRecipe
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

data object RagiumRecipeBuilder {
    //    Basic    //

    @JvmStatic
    inline fun compressing(builderAction: HTItemToItemRecipeBuilder<HTCompressingRecipe>.() -> Unit): HTItemToItemRecipeBuilder<HTCompressingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemToItemRecipeBuilder(RagiumConst.COMPRESSING, ::HTCompressingRecipe).apply(builderAction)
    }

    @JvmStatic
    inline fun cutting(builderAction: HTItemToMultiItemRecipeBuilder<HTCuttingRecipe>.() -> Unit): HTItemToMultiItemRecipeBuilder<HTCuttingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemToMultiItemRecipeBuilder(RagiumConst.CUTTING, ::HTCuttingRecipe).apply {
            time /= 2
            builderAction()
        }
    }

    //    Advanced    //

    @JvmStatic
    inline fun imploding(builderAction: HTItemToItemRecipeBuilder<HTImplodingRecipe>.() -> Unit): HTItemToItemRecipeBuilder<HTImplodingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemToItemRecipeBuilder(RagiumConst.IMPLODING, ::HTImplodingRecipe).apply {
            time /= 2
            builderAction()
        }
    }

    @JvmStatic
    inline fun melting(builderAction: HTItemToFluidRecipeBuilder<HTMeltingRecipe>.() -> Unit): HTItemToFluidRecipeBuilder<HTMeltingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemToFluidRecipeBuilder(RagiumConst.MELTING, ::HTMeltingRecipe).apply(builderAction)
    }

    @JvmStatic
    inline fun pyrolyzing(builderAction: HTItemOrFluidRecipeBuilder<HTPyrolyzingRecipe>.() -> Unit): HTItemOrFluidRecipeBuilder<HTPyrolyzingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemOrFluidRecipeBuilder(RagiumConst.PYROLYZING, ::HTPyrolyzingRecipe).apply {
            time *= 3
            builderAction()
        }
    }

    @JvmStatic
    inline fun washing(builderAction: HTItemToMultiItemRecipeBuilder<HTWashingRecipe>.() -> Unit): HTItemToMultiItemRecipeBuilder<HTWashingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemToMultiItemRecipeBuilder(RagiumConst.WASHING, ::HTWashingRecipe).apply {
            time /= 2
            builderAction()
        }
    }

    //    Elite    //

    @JvmStatic
    inline fun bathing(builderAction: HTItemAndFluidToItemRecipeBuilder<HTBathingRecipe>.() -> Unit): HTItemAndFluidToItemRecipeBuilder<HTBathingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemAndFluidToItemRecipeBuilder(RagiumConst.BATHING, ::HTBathingRecipe).apply(builderAction)
    }
}
