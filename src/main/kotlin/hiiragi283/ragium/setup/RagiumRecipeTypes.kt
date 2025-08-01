package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.common.recipe.HTInfusingRecipe
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

    // val BLOCK_INTERACTING: Supplier<RecipeType<HTBlockInteractingRecipe>> = register(RagiumConst.BLOCK_INTERACTING)

    @JvmField
    val INFUSING: Supplier<RecipeType<HTInfusingRecipe>> = register(RagiumConst.INFUSING)

    @JvmField
    val MELTING: Supplier<RecipeType<HTMeltingRecipe>> = register(RagiumConst.MELTING)

    @JvmField
    val PRESSING: Supplier<RecipeType<HTPressingRecipe>> = register(RagiumConst.PRESSING)

    @JvmField
    val REFINING: Supplier<RecipeType<HTRefiningRecipe>> = register(RagiumConst.REFINING)

    @JvmField
    val SOLIDIFYING: Supplier<RecipeType<HTSolidifyingRecipe>> = register(RagiumConst.SOLIDIFYING)
}
