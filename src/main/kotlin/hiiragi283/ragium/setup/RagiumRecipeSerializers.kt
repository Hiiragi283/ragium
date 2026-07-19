package hiiragi283.ragium.setup

import hiiragi283.core.api.recipe.RecipeSerializer
import hiiragi283.core.impl.recipe.HTBasicItemOrFluidRecipe
import hiiragi283.ragium.common.crafting.HTBatteryCombiningRecipe
import hiiragi283.ragium.common.crafting.HTTankCombiningRecipe
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTAssemblingRecipe
import hiiragi283.ragium.common.recipe.HTBathingRecipe
import hiiragi283.ragium.common.recipe.HTChemicalReactingRecipe
import hiiragi283.ragium.common.recipe.HTCompressingRecipe
import hiiragi283.ragium.common.recipe.HTCuttingRecipe
import hiiragi283.ragium.common.recipe.HTFreezingRecipe
import hiiragi283.ragium.common.recipe.HTImplodingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import hiiragi283.ragium.common.recipe.HTPrintingRecipe
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import hiiragi283.ragium.common.recipe.HTWashingRecipe
import hiiragi283.ragium.common.recipe.RTEnchantingRecipe
import hiiragi283.ragium.common.recipe.RTPlantingRecipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer

data object RagiumRecipeSerializers {
    //    Custom    //

    // Crafting
    @JvmField
    val BATTERY_COMBINING: SimpleCraftingRecipeSerializer<HTBatteryCombiningRecipe> = SimpleCraftingRecipeSerializer(::HTBatteryCombiningRecipe)

    @JvmField
    val TANK_COMBINING: SimpleCraftingRecipeSerializer<HTTankCombiningRecipe> = SimpleCraftingRecipeSerializer(::HTTankCombiningRecipe)

    //    Machine    //

    // Machine - Basic
    @JvmField
    val ALLOYING: RecipeSerializer<HTAlloyingRecipe> = RecipeSerializer(HTAlloyingRecipe.CODEC)

    @JvmField
    val ASSEMBLING: RecipeSerializer<HTAssemblingRecipe> = RecipeSerializer(HTAssemblingRecipe.CODEC)

    @JvmField
    val PRINTING: RecipeSerializer<HTPrintingRecipe> = RecipeSerializer(HTPrintingRecipe.CODEC)

    @JvmField
    val CUTTING: RecipeSerializer<HTCuttingRecipe> = RecipeSerializer(HTCuttingRecipe.CODEC)

    @JvmField
    val COMPRESSING: RecipeSerializer<HTCompressingRecipe> = RecipeSerializer(HTCompressingRecipe.CODEC)

    @JvmField
    val PLANTING: RecipeSerializer<RTPlantingRecipe> = RecipeSerializer(RTPlantingRecipe.CODEC)

    // Machine - Advanced
    @JvmField
    val FREEZING: RecipeSerializer<HTFreezingRecipe> = RecipeSerializer(HTFreezingRecipe.CODEC)

    @JvmField
    val IMPLODING: RecipeSerializer<HTImplodingRecipe> = RecipeSerializer(HTImplodingRecipe.CODEC)

    @JvmField
    val MELTING: RecipeSerializer<HTMeltingRecipe> = RecipeSerializer(HTMeltingRecipe.CODEC)

    @JvmField
    val PYROLYZING: RecipeSerializer<HTPyrolyzingRecipe> = RecipeSerializer(HTBasicItemOrFluidRecipe.codec(::HTPyrolyzingRecipe))

    @JvmField
    val REFINING: RecipeSerializer<HTRefiningRecipe> = RecipeSerializer(HTRefiningRecipe.CODEC)

    @JvmField
    val WASHING: RecipeSerializer<HTWashingRecipe> = RecipeSerializer(HTWashingRecipe.CODEC)

    // Machine - Elite
    @JvmField
    val BATHING: RecipeSerializer<HTBathingRecipe> = RecipeSerializer(HTBathingRecipe.CODEC)

    @JvmField
    val CHEMICAL_REACTING: RecipeSerializer<HTChemicalReactingRecipe> = RecipeSerializer(HTChemicalReactingRecipe.CODEC)

    @JvmField
    val MIXING: RecipeSerializer<HTMixingRecipe> = RecipeSerializer(HTMixingRecipe.CODEC)

    // Device - Ultimate
    @JvmField
    val HOLDER_ENCHANTING: RecipeSerializer<RTEnchantingRecipe> = RecipeSerializer(RTEnchantingRecipe.CODEC)
}
