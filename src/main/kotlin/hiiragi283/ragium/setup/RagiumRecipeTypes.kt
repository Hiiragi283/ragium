package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.HTBlockInteractingRecipe
import hiiragi283.ragium.api.recipe.HTCauldronDroppingRecipe
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTPressingRecipe
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object RagiumRecipeTypes {
    @JvmField
    val REGISTER: DeferredRegister<RecipeType<*>> = DeferredRegister.create(Registries.RECIPE_TYPE, RagiumAPI.MOD_ID)

    @JvmStatic
    private fun registerMachine(name: String): Supplier<RecipeType<HTMachineRecipe>> = REGISTER.register(name, RecipeType<*>::simple)

    @JvmStatic
    private fun <R : Recipe<*>> register(name: String): Supplier<RecipeType<R>> = REGISTER.register(name, RecipeType<*>::simple)

    @JvmField
    val ALLOYING: Supplier<RecipeType<HTAlloyingRecipe>> = register(RagiumConstantValues.ALLOYING)

    @JvmField
    val BEE_HIVE: Supplier<RecipeType<HTMachineRecipe>> = registerMachine(RagiumConstantValues.BEE_HIVE)

    @JvmField
    val BLOCK_INTERACTING: Supplier<RecipeType<HTBlockInteractingRecipe>> = register(RagiumConstantValues.BLOCK_INTERACTING)

    @JvmField
    val CAULDRON_DROPPING: Supplier<RecipeType<HTCauldronDroppingRecipe>> = register(RagiumConstantValues.CAULDRON_DROPPING)

    @JvmField
    val CRUSHING: Supplier<RecipeType<HTCrushingRecipe>> = register(RagiumConstantValues.CRUSHING)

    @JvmField
    val EXTRACTING: Supplier<RecipeType<HTMachineRecipe>> = registerMachine(RagiumConstantValues.EXTRACTING)

    @JvmField
    val INFUSING: Supplier<RecipeType<HTMachineRecipe>> = registerMachine(RagiumConstantValues.INFUSING)

    @JvmField
    val PRESSING: Supplier<RecipeType<HTPressingRecipe>> = register(RagiumConstantValues.PRESSING)

    @JvmField
    val REFINING: Supplier<RecipeType<HTMachineRecipe>> = registerMachine(RagiumConstantValues.REFINING)

    @JvmField
    val SOLIDIFYING: Supplier<RecipeType<HTMachineRecipe>> = registerMachine(RagiumConstantValues.SOLIDIFYING)
}
