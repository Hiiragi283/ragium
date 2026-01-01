package hiiragi283.ragium.setup

import hiiragi283.core.api.recipe.input.HTRecipeInput
import hiiragi283.core.common.registry.HTDeferredRecipeType
import hiiragi283.core.common.registry.register.HTDeferredRecipeTypeRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTCuttingRecipe
import hiiragi283.ragium.common.recipe.HTDryingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import hiiragi283.ragium.common.recipe.HTSolidifyingRecipe

typealias HTSimpleRecipeType<R> = HTDeferredRecipeType<HTRecipeInput, R>

object RagiumRecipeTypes {
    @JvmField
    val REGISTER = HTDeferredRecipeTypeRegister(RagiumAPI.MOD_ID)

    @JvmField
    val ALLOYING: HTSimpleRecipeType<HTAlloyingRecipe> = REGISTER.registerType(RagiumConst.ALLOYING)

    @JvmField
    val CRUSHING: HTSimpleRecipeType<HTCrushingRecipe> = REGISTER.registerType(RagiumConst.CRUSHING)

    @JvmField
    val CUTTING: HTSimpleRecipeType<HTCuttingRecipe> = REGISTER.registerType(RagiumConst.CUTTING)

    @JvmField
    val DRYING: HTSimpleRecipeType<HTDryingRecipe> = REGISTER.registerType(RagiumConst.DRYING)

    @JvmField
    val MELTING: HTSimpleRecipeType<HTMeltingRecipe> = REGISTER.registerType(RagiumConst.MELTING)

    @JvmField
    val MIXING: HTSimpleRecipeType<HTMixingRecipe> = REGISTER.registerType(RagiumConst.MIXING)

    @JvmField
    val PYROLYZING: HTSimpleRecipeType<HTPyrolyzingRecipe> = REGISTER.registerType(RagiumConst.PYROLYZING)

    @JvmField
    val REFINING: HTSimpleRecipeType<HTRefiningRecipe> = REGISTER.registerType(RagiumConst.REFINING)

    @JvmField
    val SOLIDIFYING: HTSimpleRecipeType<HTSolidifyingRecipe> = REGISTER.registerType(RagiumConst.SOLIDIFYING)
}
