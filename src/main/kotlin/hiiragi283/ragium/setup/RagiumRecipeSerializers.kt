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
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.crafting.HTStorageCombiningRecipe
import hiiragi283.ragium.common.data.recipe.HTCombiningRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTItemOrFluidRecipeBuilder
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTAssemblingRecipe
import hiiragi283.ragium.common.recipe.HTCuttingRecipe
import hiiragi283.ragium.common.recipe.HTElectrolyzingRecipe
import hiiragi283.ragium.common.recipe.HTFreezingRecipe
import hiiragi283.ragium.common.recipe.HTHolderEnchantingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import hiiragi283.ragium.common.recipe.HTPlantingRecipe
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import hiiragi283.ragium.common.recipe.HTWashingRecipe
import hiiragi283.ragium.impl.recipe.HTBasicItemOrFluidRecipe
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
    val STORAGE_COMBINING: SimpleCraftingRecipeSerializer<HTStorageCombiningRecipe> = REGISTER.registerSerializer(
        "storage_combining",
        SimpleCraftingRecipeSerializer(::HTStorageCombiningRecipe),
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
    val ASSEMBLING: RecipeSerializer<HTAssemblingRecipe> =
        REGISTER.registerSerializer(RagiumConst.ASSEMBLING, combine(2..2, ::HTAssemblingRecipe))

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

    // Machine - Elite
    @JvmField
    val ELECTROLYZING: RecipeSerializer<HTElectrolyzingRecipe> = REGISTER.registerSerializer(
        RagiumConst.ELECTROLYZING,
        MapBiCodec.composite(
            HTFluidIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTElectrolyzingRecipe::ingredient),
            HTFluidResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTElectrolyzingRecipe::result),
            COMPLEX_RESULT.forGetter(HTElectrolyzingRecipe::extraResult),
            HTProcessingRecipe.timeCodec(),
            ::HTElectrolyzingRecipe,
        ),
    )

    @JvmField
    val MIXING: RecipeSerializer<HTMixingRecipe> = REGISTER.registerSerializer(
        RagiumConst.MIXING,
        MapBiCodec.composite(
            MapBiCodecs
                .ior(
                    HTItemIngredient.CODEC.fieldOf(HTConst.ITEM_INGREDIENT),
                    HTFluidIngredient.CODEC.listOf(1, HTMixingRecipe.MAX_FLUID_INPUT).fieldOf(HTConst.FLUID_INGREDIENT),
                ).forGetter(HTMixingRecipe::ingredient),
            COMPLEX_RESULT.forGetter(HTMixingRecipe::result),
            HTProcessingRecipe.timeCodec(),
            ::HTMixingRecipe,
        ),
    )

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

    // Machine - Ultimate
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
