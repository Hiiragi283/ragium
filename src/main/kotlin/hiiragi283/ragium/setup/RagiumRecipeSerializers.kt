package hiiragi283.ragium.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.MapBiCodec
import hiiragi283.core.api.serialization.codec.MapBiCodecs
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.util.Ior
import hiiragi283.core.common.registry.register.HTDeferredRecipeSerializerRegister
import hiiragi283.core.impl.recipe.HTBasicItemOrFluidRecipe
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.crafting.HTBatteryCombiningRecipe
import hiiragi283.ragium.common.crafting.HTTankCombiningRecipe
import hiiragi283.ragium.common.data.recipe.HTCombiningRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTItemOrFluidRecipeBuilder
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTAssemblingRecipe
import hiiragi283.ragium.common.recipe.HTChemicalWashingRecipe
import hiiragi283.ragium.common.recipe.HTCuttingRecipe
import hiiragi283.ragium.common.recipe.HTFluidMixingRecipe
import hiiragi283.ragium.common.recipe.HTFreezingRecipe
import hiiragi283.ragium.common.recipe.HTHolderEnchantingRecipe
import hiiragi283.ragium.common.recipe.HTImplodingRecipe
import hiiragi283.ragium.common.recipe.HTItemMixingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTPlantingRecipe
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import hiiragi283.ragium.common.recipe.HTWashingRecipe
import hiiragi283.ragium.impl.recipe.HTCombiningRecipe
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
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
    private val COMPLEX_RESULT: MapBiCodec<RegistryFriendlyByteBuf, Ior<HTItemResult, HTFluidResult>> = MapBiCodecs
        .ior(
            HTItemResult.CODEC.fieldOf(HTConst.ITEM_RESULT),
            HTFluidResult.CODEC.fieldOf(HTConst.FLUID_RESULT),
        )

    @JvmStatic
    private fun <R : HTCombiningRecipe> combine(
        inputRange: IntRange,
        factory: HTCombiningRecipeBuilder.Factory<R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        HTItemIngredient.CODEC
            .listOf(inputRange)
            .fieldOf(HTConst.INGREDIENT)
            .forGetter(HTCombiningRecipe::ingredients),
        HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTCombiningRecipe::result),
        HTProcessingRecipe.timeCodec(),
        factory::create,
    )

    @JvmStatic
    private fun <RECIPE : HTBasicItemOrFluidRecipe> itemOrFluid(
        factory: HTItemOrFluidRecipeBuilder.Factory<RECIPE>,
    ): MapBiCodec<RegistryFriendlyByteBuf, RECIPE> = MapBiCodec.composite(
        MapBiCodecs
            .ior(
                HTItemIngredient.CODEC.fieldOf(HTConst.ITEM_INGREDIENT),
                HTFluidIngredient.CODEC.fieldOf(HTConst.FLUID_INGREDIENT),
            ).forGetter(HTBasicItemOrFluidRecipe::ingredient),
        COMPLEX_RESULT.forGetter(HTBasicItemOrFluidRecipe::result),
        HTProcessingRecipe.timeCodec(),
        factory::create,
    )

    // Machine - Basic
    @JvmField
    val ALLOYING: RecipeSerializer<HTAlloyingRecipe> =
        REGISTER.registerSerializer(RagiumConst.ALLOYING, combine(2..3, ::HTAlloyingRecipe))

    @JvmField
    val ASSEMBLING: RecipeSerializer<HTAssemblingRecipe> = REGISTER.registerSerializer(
        RagiumConst.ASSEMBLING,
        MapBiCodec.composite(
            HTItemIngredient.CODEC
                .listOf(2, 2)
                .fieldOf(HTConst.INGREDIENT)
                .forGetter(HTAssemblingRecipe::itemIngredients),
            HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTAssemblingRecipe::result),
            HTProcessingRecipe.timeCodec(),
            ::HTAssemblingRecipe,
        ),
    )

    @JvmField
    val CUTTING: RecipeSerializer<HTCuttingRecipe> = REGISTER.registerSerializer(
        RagiumConst.CUTTING,
        HCRecipeSerializers.singleItemToMulti(HTCuttingRecipe.OUTPUT_RANGE, ::HTCuttingRecipe),
    )

    @JvmField
    val PLANTING: RecipeSerializer<HTPlantingRecipe> = REGISTER.registerSerializer(
        RagiumConst.PLANTING,
        HCRecipeSerializers.doubleItemToMulti(HTPlantingRecipe.OUTPUT_RANGE, ::HTPlantingRecipe),
    )

    // Machine - Advanced
    @JvmField
    val FREEZING: RecipeSerializer<HTFreezingRecipe> = REGISTER.registerSerializer(
        RagiumConst.FREEZING,
        MapBiCodec.composite(
            HTItemIngredient.CODEC.fieldOf(HTConst.ITEM_INGREDIENT).forGetter(HTFreezingRecipe::itemIngredient),
            HTFluidIngredient.CODEC.fieldOf(HTConst.FLUID_INGREDIENT).forGetter(HTFreezingRecipe::fluidIngredient),
            HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTFreezingRecipe::result),
            HTProcessingRecipe.timeCodec(),
            ::HTFreezingRecipe,
        ),
    )

    @JvmField
    val IMPLODING: RecipeSerializer<HTImplodingRecipe> = REGISTER.registerSerializer(
        RagiumConst.IMPLODING,
        MapBiCodec.composite(
            HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTImplodingRecipe::ingredient),
            HTItemIngredient.CODEC.fieldOf("explosive").forGetter(HTImplodingRecipe::explosive),
            HTItemResult.CODEC
                .listOrElement(HTImplodingRecipe.OUTPUT_RANGE)
                .fieldOf(HTConst.RESULTS)
                .forGetter(HTImplodingRecipe::results),
            HTProcessingRecipe.timeCodec(),
            ::HTImplodingRecipe,
        ),
    )

    @JvmField
    val MELTING: RecipeSerializer<HTMeltingRecipe> = REGISTER.registerSerializer(
        RagiumConst.MELTING,
        MapBiCodec.composite(
            HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTMeltingRecipe::ingredient),
            HTFluidResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTMeltingRecipe::result),
            HTProcessingRecipe.timeCodec(),
            ::HTMeltingRecipe,
        ),
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
        HCRecipeSerializers.singleItemToMulti(
            HTWashingRecipe.OUTPUT_RANGE,
            HTWashingRecipe::ingredient,
            HTWashingRecipe::results,
            ::HTWashingRecipe,
        ),
    )

    // Machine - Elite
    @JvmField
    val CHEMICAL_WASHING: RecipeSerializer<HTChemicalWashingRecipe> =
        REGISTER.registerSerializer(RagiumConst.CHEMICAL_WASHING, itemOrFluid(::HTChemicalWashingRecipe))

    @JvmField
    val FLUID_MIXING: RecipeSerializer<HTFluidMixingRecipe> = REGISTER.registerSerializer(
        RagiumConst.FLUID_MIXING,
        MapBiCodec
            .composite(
                HTItemIngredient.CODEC.optionalFieldOf(HTConst.ITEM_INGREDIENT).forGetter(HTFluidMixingRecipe::itemIngredient),
                HTFluidIngredient.CODEC
                    .listOrElement(1, 2)
                    .fieldOf(HTConst.FLUID_INGREDIENT)
                    .forGetter(HTFluidMixingRecipe::fluidIngredients),
                HTFluidResult.CODEC
                    .listOrElement(1, 2)
                    .fieldOf(HTConst.FLUID_RESULT)
                    .forGetter(HTFluidMixingRecipe::results),
                HTProcessingRecipe.timeCodec(),
                ::HTFluidMixingRecipe,
            ).validate { recipe: HTFluidMixingRecipe ->
                if (recipe.itemIngredient.isEmpty && recipe.fluidIngredients.size == 1) {
                    error("Fluid Mixing recipe required two fluid ingredients, or item and fluid ingredients")
                }
                recipe
            },
    )

    @JvmField
    val ITEM_MIXING: RecipeSerializer<HTItemMixingRecipe> = REGISTER.registerSerializer(
        RagiumConst.ITEM_MIXING,
        MapBiCodec
            .composite(
                HTItemIngredient.CODEC
                    .listOf(2, 2)
                    .fieldOf(HTConst.ITEM_INGREDIENT)
                    .forGetter(HTItemMixingRecipe::itemIngredients),
                HTFluidIngredient.CODEC.fieldOf(HTConst.FLUID_INGREDIENT).forGetter(HTItemMixingRecipe::fluidIngredient),
                COMPLEX_RESULT.forGetter(HTItemMixingRecipe::result),
                HTProcessingRecipe.timeCodec(),
                ::HTItemMixingRecipe,
            ),
    )

    // Device - Ultimate
    @JvmField
    val HOLDER_ENCHANTING: RecipeSerializer<HTHolderEnchantingRecipe> = REGISTER.registerSerializer(
        "${RagiumConst.ENCHANTING}/holder",
        MapBiCodec.composite(
            HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTHolderEnchantingRecipe::ingredient),
            VanillaBiCodecs.holder(Registries.ENCHANTMENT).fieldOf("enchantment").forGetter(HTHolderEnchantingRecipe::holder),
            ::HTHolderEnchantingRecipe,
        ),
    )
}
