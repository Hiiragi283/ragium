package hiiragi283.ragium.common.init

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.HTFluidOutput
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTRecipeDefinition
import hiiragi283.ragium.api.registry.HTMachineRecipeType
import hiiragi283.ragium.api.registry.HTRecipeTypeRegister
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTExtractingRecipe
import hiiragi283.ragium.common.recipe.HTInfusingRecipe
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import net.minecraft.world.item.crafting.RecipeManager
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.event.OnDatapackSyncEvent
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
    fun onDatapackSync(event: OnDatapackSyncEvent) {
        val recipeManager: RecipeManager = event.playerList.server.recipeManager
        for (type: HTMachineRecipeType in ALL_TYPES) {
            type.reloadCache(recipeManager)
        }
    }

    //    Factories    //

    private object Factories {
        @JvmStatic
        fun crushing(definition: HTRecipeDefinition): DataResult<HTCrushingRecipe> {
            val ingredient: SizedIngredient =
                definition.getItemIngredient(0) ?: return DataResult.error { "Required one item ingredient!" }
            val output: HTItemOutput =
                definition.getItemOutput(0) ?: return DataResult.error { "Required one item output!" }
            return DataResult.success(HTCrushingRecipe(ingredient, output))
        }

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
