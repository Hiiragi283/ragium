package hiiragi283.ragium.common.init

import com.mojang.datafixers.util.Either
import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.HTFluidOutput
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.api.recipe.HTRecipeDefinition
import hiiragi283.ragium.api.recipe.HTSimpleFluidRecipe
import hiiragi283.ragium.api.recipe.HTSimpleItemRecipe
import hiiragi283.ragium.api.registry.HTMachineRecipeType
import hiiragi283.ragium.common.recipe.HTCentrifugingRecipe
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTExtractingRecipe
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
    val CENTRIFUGING = HTMachineRecipeType("centrifuging", Factories::centrifuging)

    @JvmField
    val CRUSHING = HTMachineRecipeType("crushing", Factories::crushing)

    @JvmField
    val EXTRACTING = HTMachineRecipeType("extracting", Factories::extracting)

    @JvmField
    val REFINING = HTMachineRecipeType("refining", Factories::refining)

    //     Event    //

    @JvmField
    val ALL_TYPES: List<HTMachineRecipeType> = listOf(
        CENTRIFUGING,
        CRUSHING,
        EXTRACTING,
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
        fun centrifuging(definition: HTRecipeDefinition): DataResult<HTCentrifugingRecipe> {
            val ingredient: Either<SizedIngredient, SizedFluidIngredient> =
                when {
                    definition.getItemIngredient(0) != null -> Either.left(definition.getItemIngredient(0))
                    definition.getFluidIngredient(0) != null -> Either.right(definition.getFluidIngredient(0))
                    else -> return DataResult.error { "Either one item or fluid ingredient required!" }
                }
            return DataResult.success(HTCentrifugingRecipe(ingredient, definition.itemOutputs, definition.fluidOutputs))
        }

        //    Fluid -> Fluid    //

        @JvmStatic
        fun <R : HTSimpleFluidRecipe> fluidProcess(
            factory: (SizedFluidIngredient, HTFluidOutput) -> R,
            definition: HTRecipeDefinition,
        ): DataResult<R> {
            val ingredient: SizedFluidIngredient =
                definition.getFluidIngredient(0) ?: return DataResult.error { "Required one fluid ingredient!" }
            val output: HTFluidOutput =
                definition.getFluidOutput(0) ?: return DataResult.error { "Required one fluid output!" }
            return DataResult.success(factory(ingredient, output))
        }

        @JvmStatic
        fun refining(definition: HTRecipeDefinition): DataResult<HTRefiningRecipe> = fluidProcess(::HTRefiningRecipe, definition)

        //    Item -> Item    //

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
        fun extracting(definition: HTRecipeDefinition): DataResult<HTExtractingRecipe> = itemProcess(::HTExtractingRecipe, definition)
    }
}
