package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.util.RagiumConstantValues
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object RagiumRecipeTypes {
    @JvmField
    val REGISTER: DeferredRegister<RecipeType<*>> = DeferredRegister.create(Registries.RECIPE_TYPE, RagiumAPI.MOD_ID)

    @JvmStatic
    private fun register(name: String): Supplier<RecipeType<HTMachineRecipe>> = REGISTER.register(name, RecipeType<*>::simple)

    @JvmField
    val BEE_HIVE: Supplier<RecipeType<HTMachineRecipe>> = register(RagiumConstantValues.BEE_HIVE)

    @JvmField
    val CRUSHING: Supplier<RecipeType<HTMachineRecipe>> = register(RagiumConstantValues.CRUSHING)

    @JvmField
    val EXTRACTING: Supplier<RecipeType<HTMachineRecipe>> = register(RagiumConstantValues.EXTRACTING)

    @JvmField
    val INFUSING: Supplier<RecipeType<HTMachineRecipe>> = register(RagiumConstantValues.INFUSING)

    @JvmField
    val REFINING: Supplier<RecipeType<HTMachineRecipe>> = register(RagiumConstantValues.REFINING)

    @JvmField
    val SOLIDIFYING: Supplier<RecipeType<HTMachineRecipe>> = register(RagiumConstantValues.SOLIDIFYING)
}
