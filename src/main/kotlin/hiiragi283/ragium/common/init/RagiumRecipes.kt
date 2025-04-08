package hiiragi283.ragium.common.init

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.*
import hiiragi283.ragium.api.registry.HTMachineRecipeType
import hiiragi283.ragium.api.registry.HTRecipeTypeRegister
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTExtractingRecipe
import hiiragi283.ragium.common.recipe.HTInfusingRecipe
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RecipesUpdatedEvent
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.event.AddReloadListenerEvent
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object RagiumRecipes {
    @JvmField
    val REGISTER = HTRecipeTypeRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    private fun register(name: String, factory: (HTRecipeDefinition) -> DataResult<out HTMachineRecipe>): HTMachineRecipeType =
        REGISTER.register(name, HTMachineRecipeType(name, factory))

    @JvmField
    val CRUSHING: HTMachineRecipeType = register("crushing", Factories::crushing)

    @JvmField
    val EXTRACTING: HTMachineRecipeType = register("extracting", Factories::extracting)

    @JvmField
    val INFUSING: HTMachineRecipeType = register("infusing", Factories::infusing)

    @JvmField
    val REFINING: HTMachineRecipeType = register("refining", Factories::refining)

    //     Event    //

    @JvmField
    val ALL_TYPES: List<HTMachineRecipeType> = listOf(
        CRUSHING,
        EXTRACTING,
        INFUSING,
        REFINING,
    )

    @SubscribeEvent
    fun onResourceReloaded(event: AddReloadListenerEvent) {
        ALL_TYPES.forEach(HTMachineRecipeType::setChanged)
    }

    @SubscribeEvent
    fun onRecipesUpdated(event: RecipesUpdatedEvent) {
        ALL_TYPES.forEach { it.reloadCache(event.recipeManager) }
    }

    //    Factories    //

    private object Factories {
        @JvmStatic
        fun <R : HTSimpleItemRecipe> itemProcess(
            factory: (SizedIngredient, HTItemOutput) -> R,
            definition: HTRecipeDefinition,
        ): DataResult<R> {
            val ingredient: SizedIngredient =
                definition.getItemIngredient(0) ?: return DataResult.error { "Required one item ingredient!" }
            val output: HTItemOutput =
                definition.getItemOutput(0) ?: return DataResult.error { "Required one item output!" }
            return DataResult.success(factory(ingredient, output))
        }

        @JvmStatic
        fun crushing(definition: HTRecipeDefinition): DataResult<HTCrushingRecipe> = itemProcess(::HTCrushingRecipe, definition)

        @JvmStatic
        fun extracting(definition: HTRecipeDefinition): DataResult<HTExtractingRecipe> {
            val ingredient: SizedIngredient =
                definition.getItemIngredient(0) ?: return DataResult.error { "Required one item ingredient!" }
            val itemOutput: HTItemOutput? = definition.getItemOutput(0)
            val fluidOutput: HTFluidOutput? = definition.getFluidOutput(0)
            if (itemOutput == null && fluidOutput == null) {
                return DataResult.error { "Either one fluid or item output required!" }
            }
            return DataResult.success(HTExtractingRecipe(ingredient, itemOutput, fluidOutput))
        }

        @JvmStatic
        fun infusing(definition: HTRecipeDefinition): DataResult<HTInfusingRecipe> {
            val itemIng: SizedIngredient =
                definition.getItemIngredient(0) ?: return DataResult.error { "Required one item ingredient!" }
            val fluidIng: SizedFluidIngredient =
                definition.getFluidIngredient(0) ?: return DataResult.error { "Required one fluid ingredient!" }
            val output: HTItemOutput =
                definition.getItemOutput(0) ?: return DataResult.error { "Required one item output!" }
            return DataResult.success(
                HTInfusingRecipe(
                    itemIng,
                    fluidIng,
                    output,
                ),
            )
        }

        @JvmStatic
        fun refining(definition: HTRecipeDefinition): DataResult<HTRefiningRecipe> {
            val ingredient: SizedFluidIngredient =
                definition.getFluidIngredient(0) ?: return DataResult.error { "Required one fluid ingredient!" }
            val itemOutput: HTItemOutput? = definition.getItemOutput(0)
            val fluidOutput: HTFluidOutput =
                definition.getFluidOutput(0) ?: return DataResult.error { "Required one fluid output!" }
            return DataResult.success(HTRefiningRecipe(ingredient, fluidOutput, itemOutput))
        }
    }
}
