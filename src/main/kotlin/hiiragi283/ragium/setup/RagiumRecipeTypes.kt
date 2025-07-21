package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.HTBlockInteractingRecipe
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTExtractingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTPressingRecipe
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import hiiragi283.ragium.common.recipe.HTSolidifyingRecipe
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object RagiumRecipeTypes {
    @JvmField
    val REGISTER: DeferredRegister<RecipeType<*>> = DeferredRegister.create(Registries.RECIPE_TYPE, RagiumAPI.MOD_ID)

    @JvmStatic
    private fun <R : Recipe<*>> register(name: String): Supplier<RecipeType<R>> = REGISTER.register(name, RecipeType<*>::simple)

    @JvmField
    val ALLOYING: Supplier<RecipeType<HTAlloyingRecipe>> = register(RagiumConstantValues.ALLOYING)

    @JvmField
    val BLOCK_INTERACTING: Supplier<RecipeType<HTBlockInteractingRecipe>> = register(RagiumConstantValues.BLOCK_INTERACTING)

    @JvmField
    val CRUSHING: Supplier<RecipeType<HTCrushingRecipe>> = register(RagiumConstantValues.CRUSHING)

    @JvmField
    val EXTRACTING: Supplier<RecipeType<HTExtractingRecipe>> = register(RagiumConstantValues.EXTRACTING)

    @JvmField
    val MELTING: Supplier<RecipeType<HTMeltingRecipe>> = register(RagiumConstantValues.MELTING)

    @JvmField
    val PRESSING: Supplier<RecipeType<HTPressingRecipe>> = register(RagiumConstantValues.PRESSING)

    @JvmField
    val REFINING: Supplier<RecipeType<HTRefiningRecipe>> = register(RagiumConstantValues.REFINING)

    @JvmField
    val SOLIDIFYING: Supplier<RecipeType<HTSolidifyingRecipe>> = register(RagiumConstantValues.SOLIDIFYING)
}
