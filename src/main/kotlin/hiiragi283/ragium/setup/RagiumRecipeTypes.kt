package hiiragi283.ragium.setup

import hiiragi283.core.api.recipe.base.HTDoubleMultiOutputRecipe
import hiiragi283.core.api.recipe.base.HTItemOrFluidRecipe
import hiiragi283.core.api.recipe.base.HTSingleMultiOutputRecipe
import hiiragi283.core.common.registry.register.HTDeferredRecipeTypeRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.recipe.base.HTEnchantingRecipe
import hiiragi283.ragium.api.recipe.base.HTMixingRecipe
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTAssemblingRecipe
import hiiragi283.ragium.common.recipe.HTElectrolyzingRecipe
import hiiragi283.ragium.common.recipe.HTFreezingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTWashingRecipe
import net.minecraft.world.item.crafting.RecipeType
import java.util.function.Supplier

object RagiumRecipeTypes {
    @JvmField
    val REGISTER = HTDeferredRecipeTypeRegister(RagiumAPI.MOD_ID)

    // Machine - Basic
    @JvmField
    val ALLOYING: Supplier<RecipeType<HTAlloyingRecipe>> = REGISTER.registerType(RagiumConst.ALLOYING)

    @JvmField
    val ASSEMBLING: Supplier<RecipeType<HTAssemblingRecipe>> = REGISTER.registerType(RagiumConst.ASSEMBLING)

    @JvmField
    val CUTTING: Supplier<RecipeType<HTSingleMultiOutputRecipe.Serializable>> = REGISTER.registerType(RagiumConst.CUTTING)

    @JvmField
    val PLANTING: Supplier<RecipeType<HTDoubleMultiOutputRecipe.Serializable>> = REGISTER.registerType(RagiumConst.PLANTING)

    // Machine - Advanced
    @JvmField
    val FREEZING: Supplier<RecipeType<HTFreezingRecipe>> = REGISTER.registerType(RagiumConst.FREEZING)

    @JvmField
    val MELTING: Supplier<RecipeType<HTMeltingRecipe>> = REGISTER.registerType(RagiumConst.MELTING)

    @JvmField
    val PYROLYZING: Supplier<RecipeType<HTItemOrFluidRecipe.Serializable>> = REGISTER.registerType(RagiumConst.PYROLYZING)

    @JvmField
    val REFINING: Supplier<RecipeType<HTItemOrFluidRecipe.Serializable>> = REGISTER.registerType(RagiumConst.REFINING)

    // Machine - Elite
    @JvmField
    val CHEMICAL_WASHING: Supplier<RecipeType<HTItemOrFluidRecipe.Serializable>> = REGISTER.registerType(RagiumConst.CHEMICAL_WASHING)

    @JvmField
    val ELECTROLYZING: Supplier<RecipeType<HTElectrolyzingRecipe>> = REGISTER.registerType(RagiumConst.ELECTROLYZING)

    @JvmField
    val MIXING: Supplier<RecipeType<HTMixingRecipe.Serializable>> = REGISTER.registerType(RagiumConst.MIXING)

    @JvmField
    val WASHING: Supplier<RecipeType<HTWashingRecipe>> = REGISTER.registerType(RagiumConst.WASHING)

    // Machine - Ultimate
    @JvmField
    val ENCHANTING: Supplier<RecipeType<HTEnchantingRecipe>> = REGISTER.registerType(RagiumConst.ENCHANTING)
}
