@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.support.data.recipe.HTDoubleItemToItemRecipeBuilder
import hiiragi283.core.support.data.recipe.HTItemAndFluidToItemRecipeBuilder
import hiiragi283.core.support.data.recipe.HTItemOrFluidRecipeBuilder
import hiiragi283.core.support.data.recipe.HTItemToFluidRecipeBuilder
import hiiragi283.core.support.data.recipe.HTItemToItemRecipeBuilder
import hiiragi283.core.support.data.recipe.HTItemToMultiItemRecipeBuilder
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTAssemblingRecipe
import hiiragi283.ragium.common.recipe.HTBathingRecipe
import hiiragi283.ragium.common.recipe.HTCompressingRecipe
import hiiragi283.ragium.common.recipe.HTCuttingRecipe
import hiiragi283.ragium.common.recipe.HTFreezingRecipe
import hiiragi283.ragium.common.recipe.HTImplodingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTPrintingRecipe
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import hiiragi283.ragium.common.recipe.HTWashingRecipe
import hiiragi283.ragium.common.recipe.RTSmeltingRecipe
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

data object RagiumRecipeBuilder {
    //    Mechanical    //

    @JvmStatic
    inline fun assembling(builderAction: HTDoubleItemToItemRecipeBuilder<HTAssemblingRecipe>.() -> Unit): HTDoubleItemToItemRecipeBuilder<HTAssemblingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTDoubleItemToItemRecipeBuilder(RagiumConst.ASSEMBLING, ::HTAssemblingRecipe).apply(builderAction)
    }

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

    @JvmStatic
    inline fun smelting(builderAction: HTItemToItemRecipeBuilder<RTSmeltingRecipe>.() -> Unit): HTItemToItemRecipeBuilder<RTSmeltingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemToItemRecipeBuilder("electric_${HTConst.SMELTING}", ::RTSmeltingRecipe).apply(builderAction)
    }

    //    Heat    //

    @JvmStatic
    inline fun freezing(builderAction: HTItemAndFluidToItemRecipeBuilder<HTFreezingRecipe>.() -> Unit): HTItemAndFluidToItemRecipeBuilder<HTFreezingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemAndFluidToItemRecipeBuilder(RagiumConst.FREEZING, ::HTFreezingRecipe).apply(builderAction)
    }

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

    //    Chemical    //

    @JvmStatic
    inline fun bathing(builderAction: HTItemAndFluidToItemRecipeBuilder<HTBathingRecipe>.() -> Unit): HTItemAndFluidToItemRecipeBuilder<HTBathingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemAndFluidToItemRecipeBuilder(RagiumConst.BATHING, ::HTBathingRecipe).apply(builderAction)
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

    //    Bio    //

    //    Electronics    //

    @JvmStatic
    inline fun printing(builderAction: HTDoubleItemToItemRecipeBuilder<HTPrintingRecipe>.() -> Unit): HTDoubleItemToItemRecipeBuilder<HTPrintingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTDoubleItemToItemRecipeBuilder(RagiumConst.PRINTING, ::HTPrintingRecipe).apply(builderAction)
    }

    //    Arcane    //
}
