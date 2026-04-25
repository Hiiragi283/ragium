package hiiragi283.ragium.setup

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.serialization.codec.listOrElement
import hiiragi283.core.api.util.Ior
import hiiragi283.core.common.registry.register.HTDeferredRecipeSerializerRegister
import hiiragi283.core.impl.recipe.HTBasicItemOrFluidRecipe
import hiiragi283.core.impl.recipe.HTBasicMultiOutputRecipe
import hiiragi283.core.impl.recipe.HTBasicSingleMultiOutputRecipe
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.crafting.HTBatteryCombiningRecipe
import hiiragi283.ragium.common.crafting.HTTankCombiningRecipe
import hiiragi283.ragium.common.data.recipe.HTItemOrFluidRecipeBuilder
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTAssemblingRecipe
import hiiragi283.ragium.common.recipe.HTChemicalWashingRecipe
import hiiragi283.ragium.common.recipe.HTCuttingRecipe
import hiiragi283.ragium.common.recipe.HTFreezingRecipe
import hiiragi283.ragium.common.recipe.HTHolderEnchantingRecipe
import hiiragi283.ragium.common.recipe.HTImplodingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTPlantingRecipe
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import hiiragi283.ragium.common.recipe.HTWashingRecipe
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer

object RagiumRecipeSerializers {
    @JvmField
    val REGISTER = HTDeferredRecipeSerializerRegister(RagiumAPI.MOD_ID)

    //    Custom    //

    // Crafting
    @JvmField
    val BATTERY_COMBINING: SimpleCraftingRecipeSerializer<HTBatteryCombiningRecipe> = REGISTER.registerSerializer(
        "battery_combining",
        SimpleCraftingRecipeSerializer(::HTBatteryCombiningRecipe),
    )

    @JvmField
    val TANK_COMBINING: SimpleCraftingRecipeSerializer<HTTankCombiningRecipe> = REGISTER.registerSerializer(
        "tank_combining",
        SimpleCraftingRecipeSerializer(::HTTankCombiningRecipe),
    )

    //    Machine    //

    @JvmStatic
    private val COMPLEX_RESULT: MapCodec<Ior<HTItemResult, HTFluidResult>> = HTCodecs
        .ior(
            HTItemResult.CODEC.fieldOf(HTConst.ITEM_RESULT),
            HTFluidResult.CODEC.fieldOf(HTConst.FLUID_RESULT),
        )

    @JvmStatic
    private fun <RECIPE : HTBasicItemOrFluidRecipe> itemOrFluid(factory: HTItemOrFluidRecipeBuilder.Factory<RECIPE>): MapCodec<RECIPE> =
        RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTCodecs
                        .ior(
                            HTItemIngredient.CODEC.fieldOf(HTConst.ITEM_INGREDIENT),
                            HTFluidIngredient.CODEC.fieldOf(HTConst.FLUID_INGREDIENT),
                        ).forGetter(HTBasicItemOrFluidRecipe::ingredient),
                    COMPLEX_RESULT.forGetter(HTBasicItemOrFluidRecipe::result),
                    HTProcessingRecipe.timeCodec(),
                ).apply(instance, factory::create)
        }

    // Machine - Basic
    @JvmField
    val ALLOYING: RecipeSerializer<HTAlloyingRecipe> = REGISTER.registerSerializer(
        RagiumConst.ALLOYING,
        RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTItemIngredient.CODEC
                        .listOf(2, 3)
                        .fieldOf(HTConst.INGREDIENT)
                        .forGetter { listOfNotNull(it.primary, it.secondary, it.tertiary) },
                    HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTAlloyingRecipe::result),
                    HTProcessingRecipe.timeCodec(),
                ).apply(instance, ::HTAlloyingRecipe)
        },
    )

    @JvmField
    val ASSEMBLING: RecipeSerializer<HTAssemblingRecipe> = REGISTER.registerSerializer(
        RagiumConst.ASSEMBLING,
        RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTItemIngredient.CODEC
                        .listOf(2, 2)
                        .fieldOf(HTConst.INGREDIENT)
                        .forGetter(HTAssemblingRecipe::itemIngredients),
                    HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTAssemblingRecipe::result),
                    HTProcessingRecipe.timeCodec(),
                ).apply(instance, ::HTAssemblingRecipe)
        },
    )

    @JvmField
    val CUTTING: RecipeSerializer<HTCuttingRecipe> = REGISTER.registerSerializer(
        RagiumConst.CUTTING,
        HTBasicSingleMultiOutputRecipe.codec(HTCuttingRecipe.OUTPUT_RANGE, ::HTCuttingRecipe),
    )

    @JvmField
    val PLANTING: RecipeSerializer<HTPlantingRecipe> = REGISTER.registerSerializer(
        RagiumConst.PLANTING,
        RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTCodecs.INGREDIENT.fieldOf("plant").forGetter(HTPlantingRecipe::plant),
                    HTCodecs.INGREDIENT.fieldOf("soil").forGetter(HTPlantingRecipe::soil),
                    HTBasicMultiOutputRecipe.resultCodec(HTPlantingRecipe.OUTPUT_RANGE),
                    HTProcessingRecipe.timeCodec(),
                ).apply(instance, ::HTPlantingRecipe)
        },
    )

    // Machine - Advanced
    @JvmField
    val FREEZING: RecipeSerializer<HTFreezingRecipe> = REGISTER.registerSerializer(
        RagiumConst.FREEZING,
        RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTItemIngredient.CODEC.fieldOf(HTConst.ITEM_INGREDIENT).forGetter(HTFreezingRecipe::itemIngredient),
                    HTFluidIngredient.CODEC.fieldOf(HTConst.FLUID_INGREDIENT).forGetter(HTFreezingRecipe::fluidIngredient),
                    HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTFreezingRecipe::result),
                    HTProcessingRecipe.timeCodec(),
                ).apply(instance, ::HTFreezingRecipe)
        },
    )

    @JvmField
    val IMPLODING: RecipeSerializer<HTImplodingRecipe> = REGISTER.registerSerializer(
        RagiumConst.IMPLODING,
        RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTImplodingRecipe::ingredient),
                    HTItemIngredient.CODEC.fieldOf("explosive").forGetter(HTImplodingRecipe::explosive),
                    HTItemResult.CODEC
                        .listOrElement(HTImplodingRecipe.OUTPUT_RANGE)
                        .fieldOf(HTConst.RESULTS)
                        .forGetter(HTImplodingRecipe::results),
                    HTProcessingRecipe.timeCodec(),
                ).apply(instance, ::HTImplodingRecipe)
        },
    )

    @JvmField
    val MELTING: RecipeSerializer<HTMeltingRecipe> = REGISTER.registerSerializer(
        RagiumConst.MELTING,
        RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTMeltingRecipe::ingredient),
                    HTFluidResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTMeltingRecipe::result),
                    HTProcessingRecipe.timeCodec(),
                ).apply(instance, ::HTMeltingRecipe)
        },
    )

    @JvmField
    val PYROLYZING: RecipeSerializer<HTPyrolyzingRecipe> =
        REGISTER.registerSerializer(RagiumConst.PYROLYZING, itemOrFluid(::HTPyrolyzingRecipe))

    @JvmField
    val REFINING: RecipeSerializer<HTRefiningRecipe> =
        REGISTER.registerSerializer(RagiumConst.REFINING, itemOrFluid(::HTRefiningRecipe))

    @JvmField
    val WASHING: RecipeSerializer<HTWashingRecipe> = REGISTER.registerSerializer(
        RagiumConst.WASHING,
        RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTWashingRecipe::ingredient),
                    HTBasicMultiOutputRecipe.resultCodec(HTWashingRecipe.OUTPUT_RANGE),
                    HTProcessingRecipe.timeCodec(),
                ).apply(instance, ::HTWashingRecipe)
        },
    )

    // Machine - Elite
    @JvmField
    val CHEMICAL_WASHING: RecipeSerializer<HTChemicalWashingRecipe> =
        REGISTER.registerSerializer(RagiumConst.CHEMICAL_WASHING, itemOrFluid(::HTChemicalWashingRecipe))

    // Device - Ultimate
    @JvmField
    val HOLDER_ENCHANTING: RecipeSerializer<HTHolderEnchantingRecipe> = REGISTER.registerSerializer(
        "${RagiumConst.ENCHANTING}/holder",
        RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTHolderEnchantingRecipe::ingredient),
                    HTCodecs.holder(Registries.ENCHANTMENT).fieldOf("enchantment").forGetter(HTHolderEnchantingRecipe::holder),
                ).apply(instance, ::HTHolderEnchantingRecipe)
        },
    )
}
