package hiiragi283.ragium.setup

import hiiragi283.core.common.registry.register.HTDeferredRecipeSerializerRegister
import hiiragi283.core.impl.recipe.HTBasicItemOrFluidRecipe
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.crafting.HTBatteryCombiningRecipe
import hiiragi283.ragium.common.crafting.HTTankCombiningRecipe
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTAssemblingRecipe
import hiiragi283.ragium.common.recipe.HTChemicalReactingRecipe
import hiiragi283.ragium.common.recipe.HTChemicalWashingRecipe
import hiiragi283.ragium.common.recipe.HTCompressingRecipe
import hiiragi283.ragium.common.recipe.HTCuttingRecipe
import hiiragi283.ragium.common.recipe.HTFreezingRecipe
import hiiragi283.ragium.common.recipe.HTImplodingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import hiiragi283.ragium.common.recipe.HTWashingRecipe
import hiiragi283.ragium.common.recipe.RTEnchantingRecipe
import hiiragi283.ragium.common.recipe.RTPlantingRecipe
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

    // Machine - Basic
    @JvmField
    val ALLOYING: RecipeSerializer<HTAlloyingRecipe> = REGISTER.registerSerializer(RagiumConst.ALLOYING, HTAlloyingRecipe.CODEC)

    @JvmField
    val ASSEMBLING: RecipeSerializer<HTAssemblingRecipe> = REGISTER.registerSerializer(RagiumConst.ASSEMBLING, HTAssemblingRecipe.CODEC)

    @JvmField
    val CUTTING: RecipeSerializer<HTCuttingRecipe> = REGISTER.registerSerializer(RagiumConst.CUTTING, HTCuttingRecipe.CODEC)

    @JvmField
    val COMPRESSING: RecipeSerializer<HTCompressingRecipe> = REGISTER.registerSerializer(RagiumConst.COMPRESSING, HTCompressingRecipe.CODEC)

    @JvmField
    val PLANTING: RecipeSerializer<RTPlantingRecipe> = REGISTER.registerSerializer(RagiumConst.PLANTING, RTPlantingRecipe.CODEC)

    // Machine - Advanced
    @JvmField
    val FREEZING: RecipeSerializer<HTFreezingRecipe> = REGISTER.registerSerializer(RagiumConst.FREEZING, HTFreezingRecipe.CODEC)

    @JvmField
    val IMPLODING: RecipeSerializer<HTImplodingRecipe> = REGISTER.registerSerializer(RagiumConst.IMPLODING, HTImplodingRecipe.CODEC)

    @JvmField
    val MELTING: RecipeSerializer<HTMeltingRecipe> = REGISTER.registerSerializer(RagiumConst.MELTING, HTMeltingRecipe.CODEC)

    @JvmField
    val PYROLYZING: RecipeSerializer<HTPyrolyzingRecipe> =
        REGISTER.registerSerializer(RagiumConst.PYROLYZING, HTBasicItemOrFluidRecipe.codec(::HTPyrolyzingRecipe))

    @JvmField
    val REFINING: RecipeSerializer<HTRefiningRecipe> =
        REGISTER.registerSerializer(RagiumConst.REFINING, HTBasicItemOrFluidRecipe.codec(::HTRefiningRecipe))

    @JvmField
    val WASHING: RecipeSerializer<HTWashingRecipe> = REGISTER.registerSerializer(RagiumConst.WASHING, HTWashingRecipe.CODEC)

    // Machine - Elite
    @JvmField
    val CHEMICAL_REACTING: RecipeSerializer<HTChemicalReactingRecipe> =
        REGISTER.registerSerializer(RagiumConst.CHEMICAL_REACTING, HTChemicalReactingRecipe.CODEC)

    @JvmField
    val CHEMICAL_WASHING: RecipeSerializer<HTChemicalWashingRecipe> =
        REGISTER.registerSerializer(RagiumConst.CHEMICAL_WASHING, HTBasicItemOrFluidRecipe.codec(::HTChemicalWashingRecipe))

    @JvmField
    val MIXING: RecipeSerializer<HTMixingRecipe> = REGISTER.registerSerializer(RagiumConst.MIXING, HTMixingRecipe.CODEC)

    // Device - Ultimate
    @JvmField
    val HOLDER_ENCHANTING: RecipeSerializer<RTEnchantingRecipe> =
        REGISTER.registerSerializer("${RagiumConst.ENCHANTING}/holder", RTEnchantingRecipe.CODEC)
}
