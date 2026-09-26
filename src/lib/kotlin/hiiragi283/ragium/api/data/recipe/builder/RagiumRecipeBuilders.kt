@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.api.data.recipe.builder

import hiiragi283.lib.data.recipe.builder.HTFluidToDoubleFluidRecipeBuilder
import hiiragi283.lib.data.recipe.builder.HTFluidToItemRecipeBuilder
import hiiragi283.lib.data.recipe.builder.HTItemAndFluidToRecipeBuilder
import hiiragi283.lib.data.recipe.builder.HTItemToDoubleItemRecipeBuilder
import hiiragi283.lib.data.recipe.builder.HTItemToFluidRecipeBuilder
import hiiragi283.lib.data.recipe.builder.HTItemToItemAndFluidRecipeBuilder
import hiiragi283.lib.data.recipe.builder.HTItemToItemRecipeBuilder
import hiiragi283.lib.data.recipe.builder.HTSingleRecipeBuilder
import hiiragi283.lib.data.recipe.builder.HTTripleItemToItemRecipeBuilder
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.recipe.RTAlloyingRecipe
import hiiragi283.ragium.api.recipe.RTAssemblingRecipe
import hiiragi283.ragium.api.recipe.RTBathingRecipe
import hiiragi283.ragium.api.recipe.RTBrewingRecipe
import hiiragi283.ragium.api.recipe.RTCentrifugingRecipe
import hiiragi283.ragium.api.recipe.RTCompressingRecipe
import hiiragi283.ragium.api.recipe.RTCrushingRecipe
import hiiragi283.ragium.api.recipe.RTCuttingRecipe
import hiiragi283.ragium.api.recipe.RTDrainingRecipe
import hiiragi283.ragium.api.recipe.RTEnchantingRecipe
import hiiragi283.ragium.api.recipe.RTFillingRecipe
import hiiragi283.ragium.api.recipe.RTFreezingRecipe
import hiiragi283.ragium.api.recipe.RTMeltingRecipe
import hiiragi283.ragium.api.recipe.RTMixingRecipe
import hiiragi283.ragium.api.recipe.RTPlantingRecipe
import hiiragi283.ragium.api.recipe.RTPyrolyzingRecipe
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Ragiumで使用されるレシピビルダーをまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object RagiumRecipeBuilders {
    // Mechanical
    @JvmStatic
    inline fun assembling(
        builderAction: HTTripleItemToItemRecipeBuilder<RTAssemblingRecipe>.() -> Unit
    ): HTTripleItemToItemRecipeBuilder<RTAssemblingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTTripleItemToItemRecipeBuilder(RagiumConstants.ASSEMBLING, ::RTAssemblingRecipe).apply(builderAction)
    }

    @JvmStatic
    inline fun compressing(
        builderAction: HTItemToItemRecipeBuilder<RTCompressingRecipe>.() -> Unit
    ): HTItemToItemRecipeBuilder<RTCompressingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTSingleRecipeBuilder(RagiumConstants.COMPRESSING, ::RTCompressingRecipe).apply(builderAction)
    }

    @JvmStatic
    inline fun crushing(
        builderAction: HTItemToDoubleItemRecipeBuilder<RTCrushingRecipe>.() -> Unit
    ): HTItemToDoubleItemRecipeBuilder<RTCrushingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemToDoubleItemRecipeBuilder(RagiumConstants.CRUSHING, ::RTCrushingRecipe).apply(builderAction)
    }

    @JvmStatic
    inline fun cutting(
        builderAction: HTItemToDoubleItemRecipeBuilder<RTCuttingRecipe>.() -> Unit
    ): HTItemToDoubleItemRecipeBuilder<RTCuttingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemToDoubleItemRecipeBuilder(RagiumConstants.CUTTING, ::RTCuttingRecipe).apply(builderAction)
    }

    @JvmStatic
    inline fun draining(
        builderAction: HTItemToItemAndFluidRecipeBuilder<RTDrainingRecipe>.() -> Unit
    ): HTItemToItemAndFluidRecipeBuilder<RTDrainingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemToItemAndFluidRecipeBuilder(RagiumConstants.DRAINING, ::RTDrainingRecipe).apply(builderAction)
    }

    @JvmStatic
    inline fun filling(
        builderAction: HTItemAndFluidToRecipeBuilder.ToItem<RTFillingRecipe>.() -> Unit
    ): HTItemAndFluidToRecipeBuilder.ToItem<RTFillingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemAndFluidToRecipeBuilder.ToItem(RagiumConstants.FILLING, ::RTFillingRecipe).apply(builderAction)
    }

    // Heat
    @JvmStatic
    inline fun alloying(
        builderAction: HTTripleItemToItemRecipeBuilder<RTAlloyingRecipe>.() -> Unit
    ): HTTripleItemToItemRecipeBuilder<RTAlloyingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTTripleItemToItemRecipeBuilder(RagiumConstants.ALLOYING, ::RTAlloyingRecipe).apply(builderAction)
    }

    @JvmStatic
    inline fun freezing(
        builderAction: HTFluidToItemRecipeBuilder<RTFreezingRecipe>.() -> Unit
    ): HTFluidToItemRecipeBuilder<RTFreezingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTSingleRecipeBuilder(RagiumConstants.FREEZING, ::RTFreezingRecipe).apply(builderAction)
    }

    @JvmStatic
    inline fun melting(
        builderAction: HTItemToFluidRecipeBuilder<RTMeltingRecipe>.() -> Unit
    ): HTItemToFluidRecipeBuilder<RTMeltingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTSingleRecipeBuilder(RagiumConstants.MELTING, ::RTMeltingRecipe).apply(builderAction)
    }

    @JvmStatic
    inline fun pyrolyzing(
        builderAction: HTItemToItemAndFluidRecipeBuilder<RTPyrolyzingRecipe>.() -> Unit
    ): HTItemToItemAndFluidRecipeBuilder<RTPyrolyzingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemToItemAndFluidRecipeBuilder(RagiumConstants.PYROLYZING, ::RTPyrolyzingRecipe).apply(builderAction)
    }

    @JvmStatic
    inline fun refining(builderAction: RTRefiningRecipeBuilder.() -> Unit): RTRefiningRecipeBuilder {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return RTRefiningRecipeBuilder().apply(builderAction)
    }

    // Chemical
    @JvmStatic
    inline fun bathing(
        builderAction: HTItemAndFluidToRecipeBuilder.ToItem<RTBathingRecipe>.() -> Unit
    ): HTItemAndFluidToRecipeBuilder.ToItem<RTBathingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemAndFluidToRecipeBuilder.ToItem(RagiumConstants.BATHING, ::RTBathingRecipe).apply(builderAction)
    }

    @JvmStatic
    inline fun centrifuging(
        builderAction: HTFluidToDoubleFluidRecipeBuilder<RTCentrifugingRecipe>.() -> Unit
    ): HTFluidToDoubleFluidRecipeBuilder<RTCentrifugingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTFluidToDoubleFluidRecipeBuilder(RagiumConstants.CENTRIFUGING, ::RTCentrifugingRecipe)
            .apply(builderAction)
    }

    @JvmStatic
    inline fun mixing(
        builderAction: HTItemAndFluidToRecipeBuilder.ToFluid<RTMixingRecipe>.() -> Unit
    ): HTItemAndFluidToRecipeBuilder.ToFluid<RTMixingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemAndFluidToRecipeBuilder.ToFluid(RagiumConstants.MIXING, ::RTMixingRecipe).apply(builderAction)
    }

    @JvmStatic
    inline fun reacting(builderAction: RTReactingRecipeBuilder.() -> Unit): RTReactingRecipeBuilder {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return RTReactingRecipeBuilder().apply(builderAction)
    }

    // Bio
    @JvmStatic
    inline fun brewing(
        builderAction: HTItemAndFluidToRecipeBuilder.ToFluid<RTBrewingRecipe>.() -> Unit
    ): HTItemAndFluidToRecipeBuilder.ToFluid<RTBrewingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemAndFluidToRecipeBuilder.ToFluid(RagiumConstants.BREWING, ::RTBrewingRecipe).apply(builderAction)
    }

    @JvmStatic
    inline fun planting(
        builderAction: HTItemToDoubleItemRecipeBuilder<RTPlantingRecipe>.() -> Unit
    ): HTItemToDoubleItemRecipeBuilder<RTPlantingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemToDoubleItemRecipeBuilder(RagiumConstants.PLANTING, ::RTPlantingRecipe).apply(builderAction)
    }

    // Electronics
    @JvmStatic
    inline fun electrolyzing(builderAction: RTElectrolyzingRecipeBuilder.() -> Unit): RTElectrolyzingRecipeBuilder {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return RTElectrolyzingRecipeBuilder().apply(builderAction)
    }

    @JvmStatic
    inline fun resourceExtracting(
        builderAction: RTResourceExtractingRecipeBuilder.() -> Unit
    ): RTResourceExtractingRecipeBuilder {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return RTResourceExtractingRecipeBuilder().apply(builderAction)
    }

    // Arcane
    @JvmStatic
    inline fun enchanting(
        builderAction: HTItemToItemRecipeBuilder<RTEnchantingRecipe>.() -> Unit
    ): HTItemToItemRecipeBuilder<RTEnchantingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTSingleRecipeBuilder(RagiumConstants.ENCHANTING, ::RTEnchantingRecipe).apply(builderAction)
    }
}
