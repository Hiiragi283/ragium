package hiiragi283.ragium.common.init

import com.mojang.datafixers.util.Either
import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.*
import hiiragi283.ragium.api.registry.HTMachineRecipeType
import hiiragi283.ragium.api.registry.HTRecipeTypeRegister
import hiiragi283.ragium.common.recipe.*
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
    val ALLOYING: HTMachineRecipeType = register("alloying", Factories::alloying)

    @JvmField
    val CENTRIFUGING: HTMachineRecipeType = register("centrifuging", Factories::centrifuging)

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
        ALLOYING,
        CENTRIFUGING,
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
        fun alloying(definition: HTRecipeDefinition): DataResult<HTAlloyingRecipe> {
            val firstInput: SizedIngredient =
                definition.getItemIngredient(0) ?: return DataResult.error { "Required item ingredients!" }
            val secondInput: SizedIngredient =
                definition.getItemIngredient(1) ?: return DataResult.error { "Required two item ingredients!" }
            val output: HTItemOutput =
                definition.getItemOutput(0) ?: return DataResult.error { "Required one item output!" }
            return DataResult.success(HTAlloyingRecipe(firstInput, secondInput, output))
        }

        @JvmStatic
        fun centrifuging(definition: HTRecipeDefinition): DataResult<HTCentrifugingRecipe> {
            val ingredient: Either<SizedIngredient, SizedFluidIngredient> =
                when {
                    definition.getItemIngredient(0) != null -> Either.left(definition.getItemIngredient(0))
                    definition.getFluidIngredient(0) != null -> Either.right(definition.getFluidIngredient(0))
                    else -> return DataResult.error { "Either one item or fluid ingredient required!" }
                }

            val itemOutputs: List<HTItemOutput> = definition.itemOutputs
            if (itemOutputs.size > 4) {
                return DataResult.error { "Max item outputs is 4!" }
            }

            val fluidOutputs: List<HTFluidOutput> = definition.fluidOutputs
            if (fluidOutputs.size > 2) {
                return DataResult.error { "Max fluid outputs is 2!" }
            }

            return DataResult.success(HTCentrifugingRecipe(ingredient, itemOutputs, fluidOutputs))
        }

        @JvmStatic
        fun infusing(definition: HTRecipeDefinition): DataResult<HTInfusingRecipe> {
            val itemIng: SizedIngredient =
                definition.getItemIngredient(0) ?: return DataResult.error { "Required one item ingredient!" }
            val fluidIng: SizedFluidIngredient =
                definition.getFluidIngredient(0) ?: return DataResult.error { "Required one fluid ingredient!" }
            if (definition.isEmptyOutput) return DataResult.error { "Either item or fluid output required!" }
            return DataResult.success(
                HTInfusingRecipe(
                    itemIng,
                    fluidIng,
                    definition.getItemOutput(0),
                    definition.getFluidOutput(0),
                ),
            )
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
