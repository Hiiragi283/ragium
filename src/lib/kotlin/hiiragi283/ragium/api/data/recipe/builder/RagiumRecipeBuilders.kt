@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.api.data.recipe.builder

import hiiragi283.lib.data.recipe.builder.HTFluidSeparatingRecipeBuilder
import hiiragi283.lib.data.recipe.builder.HTItemAndFluidToRecipeBuilder
import hiiragi283.lib.data.recipe.builder.HTItemOrFluidRecipeBuilder
import hiiragi283.lib.data.recipe.builder.HTItemSeparatingRecipeBuilder
import hiiragi283.lib.data.recipe.builder.HTItemToFluidRecipeBuilder
import hiiragi283.lib.data.recipe.builder.HTItemToItemRecipeBuilder
import hiiragi283.lib.data.recipe.builder.HTItemToMultiItemRecipeBuilder
import hiiragi283.lib.data.recipe.builder.HTLocationToFluidRecipeBuilder
import hiiragi283.lib.data.recipe.builder.HTSingleRecipeBuilder
import hiiragi283.lib.data.recipe.builder.HTSizedShapelessRecipeBuilder
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.recipe.RTAlloyingRecipe
import hiiragi283.ragium.api.recipe.RTAssemblingRecipe
import hiiragi283.ragium.api.recipe.RTBathingRecipe
import hiiragi283.ragium.api.recipe.RTBrewingRecipe
import hiiragi283.ragium.api.recipe.RTCentrifugingRecipe
import hiiragi283.ragium.api.recipe.RTCompressingRecipe
import hiiragi283.ragium.api.recipe.RTCrushingRecipe
import hiiragi283.ragium.api.recipe.RTCuttingRecipe
import hiiragi283.ragium.api.recipe.RTElectrolyzingRecipe
import hiiragi283.ragium.api.recipe.RTEnchantingRecipe
import hiiragi283.ragium.api.recipe.RTEngravingRecipe
import hiiragi283.ragium.api.recipe.RTExchangingRecipe
import hiiragi283.ragium.api.recipe.RTFillingRecipe
import hiiragi283.ragium.api.recipe.RTFreezingRecipe
import hiiragi283.ragium.api.recipe.RTMeltingRecipe
import hiiragi283.ragium.api.recipe.RTPlantingRecipe
import hiiragi283.ragium.api.recipe.RTPressingRecipe
import hiiragi283.ragium.api.recipe.RTPyrolyzingRecipe
import hiiragi283.ragium.api.recipe.RTRefiningRecipe
import hiiragi283.ragium.api.recipe.RTResourcePumpingRecipe
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
        builderAction: HTSizedShapelessRecipeBuilder<RTAssemblingRecipe>.() -> Unit
    ): HTSizedShapelessRecipeBuilder<RTAssemblingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTSizedShapelessRecipeBuilder(RagiumConstants.ASSEMBLING, ::RTAssemblingRecipe).apply(builderAction)
    }

    @JvmStatic
    inline fun compressing(
        builderAction: HTItemToItemRecipeBuilder<RTCompressingRecipe>.() -> Unit
    ): HTItemToItemRecipeBuilder<RTCompressingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTSingleRecipeBuilder.Basic(RagiumConstants.COMPRESSING, ::RTCompressingRecipe).apply(builderAction)
    }

    @JvmStatic
    inline fun crushing(
        builderAction: HTItemToMultiItemRecipeBuilder<RTCrushingRecipe>.() -> Unit
    ): HTItemToMultiItemRecipeBuilder<RTCrushingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemToMultiItemRecipeBuilder(RagiumConstants.CRUSHING, ::RTCrushingRecipe).apply(builderAction)
    }

    @JvmStatic
    inline fun cutting(
        builderAction: HTItemToMultiItemRecipeBuilder<RTCuttingRecipe>.() -> Unit
    ): HTItemToMultiItemRecipeBuilder<RTCuttingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemToMultiItemRecipeBuilder(RagiumConstants.CUTTING, ::RTCuttingRecipe).apply(builderAction)
    }

    @JvmStatic
    inline fun centrifuging(
        builderAction: HTItemSeparatingRecipeBuilder<RTCentrifugingRecipe>.() -> Unit
    ): HTItemSeparatingRecipeBuilder<RTCentrifugingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemSeparatingRecipeBuilder(RagiumConstants.CENTRIFUGING, ::RTCentrifugingRecipe).apply(builderAction)
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

    @JvmStatic
    inline fun pressing(
        builderAction: HTSingleRecipeBuilder.Catalysed<HTItemIngredient, HTItemResult, RTPressingRecipe>.() -> Unit
    ): HTSingleRecipeBuilder.Catalysed<HTItemIngredient, HTItemResult, RTPressingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTSingleRecipeBuilder.Catalysed(RagiumConstants.PRESSING, ::RTPressingRecipe).apply(builderAction)
    }

    // Heat
    @JvmStatic
    inline fun alloying(
        builderAction: HTSizedShapelessRecipeBuilder<RTAlloyingRecipe>.() -> Unit
    ): HTSizedShapelessRecipeBuilder<RTAlloyingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTSizedShapelessRecipeBuilder(RagiumConstants.ALLOYING, ::RTAlloyingRecipe).apply(builderAction)
    }

    @JvmStatic
    inline fun freezing(
        builderAction: HTSingleRecipeBuilder.Catalysed<HTFluidIngredient, HTItemResult, RTFreezingRecipe>.() -> Unit
    ): HTSingleRecipeBuilder.Catalysed<HTFluidIngredient, HTItemResult, RTFreezingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTSingleRecipeBuilder.Catalysed(RagiumConstants.FREEZING, ::RTFreezingRecipe).apply(builderAction)
    }

    @JvmStatic
    inline fun melting(
        builderAction: HTItemToFluidRecipeBuilder<RTMeltingRecipe>.() -> Unit
    ): HTItemToFluidRecipeBuilder<RTMeltingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTSingleRecipeBuilder.Basic(RagiumConstants.MELTING, ::RTMeltingRecipe).apply(builderAction)
    }

    @JvmStatic
    inline fun pyrolyzing(
        builderAction: HTItemSeparatingRecipeBuilder<RTPyrolyzingRecipe>.() -> Unit
    ): HTItemSeparatingRecipeBuilder<RTPyrolyzingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemSeparatingRecipeBuilder(RagiumConstants.PYROLYZING, ::RTPyrolyzingRecipe).apply(builderAction)
    }

    @JvmStatic
    inline fun refining(
        builderAction: HTFluidSeparatingRecipeBuilder<RTRefiningRecipe>.() -> Unit
    ): HTFluidSeparatingRecipeBuilder<RTRefiningRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTFluidSeparatingRecipeBuilder(RagiumConstants.REFINING, ::RTRefiningRecipe).apply(builderAction)
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
    inline fun exchanging(
        builderAction: HTItemOrFluidRecipeBuilder<RTExchangingRecipe>.() -> Unit
    ): HTItemOrFluidRecipeBuilder<RTExchangingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTItemOrFluidRecipeBuilder(RagiumConstants.EXCHANGING, ::RTExchangingRecipe).apply(builderAction)
    }

    @JvmStatic
    inline fun mixing(builderAction: RTMixingRecipeBuilder.() -> Unit): RTMixingRecipeBuilder {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return RTMixingRecipeBuilder().apply(builderAction)
    }

    @JvmStatic
    inline fun reacting(builderAction: RTReactingRecipeBuilder.() -> Unit): RTReactingRecipeBuilder {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return RTReactingRecipeBuilder().apply(builderAction)
    }

    @JvmStatic
    inline fun washing(builderAction: RTWashingRecipeBuilder.() -> Unit): RTWashingRecipeBuilder {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return RTWashingRecipeBuilder().apply(builderAction)
    }

    // Resource
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
        builderAction: HTItemToItemRecipeBuilder<RTPlantingRecipe>.() -> Unit
    ): HTItemToItemRecipeBuilder<RTPlantingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTSingleRecipeBuilder.Basic(RagiumConstants.PLANTING, ::RTPlantingRecipe).apply(builderAction)
    }

    @JvmStatic
    inline fun resourcePumping(
        builderAction: HTLocationToFluidRecipeBuilder<RTResourcePumpingRecipe>.() -> Unit
    ): HTLocationToFluidRecipeBuilder<RTResourcePumpingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTLocationToFluidRecipeBuilder(RagiumConstants.RESOURCE_PUMPING, ::RTResourcePumpingRecipe)
            .apply(builderAction)
    }

    // Electronics
    @JvmStatic
    inline fun electrolyzing(
        builderAction: HTFluidSeparatingRecipeBuilder<RTElectrolyzingRecipe>.() -> Unit
    ): HTFluidSeparatingRecipeBuilder<RTElectrolyzingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTFluidSeparatingRecipeBuilder(
            RagiumConstants.ELECTROLYZING,
            ::RTElectrolyzingRecipe
        ).apply(builderAction)
    }

    @JvmStatic
    inline fun engraving(
        builderAction: HTSingleRecipeBuilder.Catalysed<HTItemIngredient, HTItemResult, RTEngravingRecipe>.() -> Unit
    ): HTSingleRecipeBuilder.Catalysed<HTItemIngredient, HTItemResult, RTEngravingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTSingleRecipeBuilder.Catalysed(RagiumConstants.ENGRAVING, ::RTEngravingRecipe).apply(builderAction)
    }

    // Arcane
    @JvmStatic
    inline fun enchanting(
        builderAction: HTItemToItemRecipeBuilder<RTEnchantingRecipe>.() -> Unit
    ): HTItemToItemRecipeBuilder<RTEnchantingRecipe> {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return HTSingleRecipeBuilder.Basic(RagiumConstants.ENCHANTING, ::RTEnchantingRecipe).apply(builderAction)
    }
}
