package hiiragi283.ragium.common.init

import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTRecipeDefinition
import hiiragi283.ragium.api.registry.HTRecipeType
import hiiragi283.ragium.common.recipe.HTCentrifugingRecipe
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTExtractingRecipe
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.RecipeSerializer
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RecipesUpdatedEvent
import net.neoforged.neoforge.event.AddReloadListenerEvent

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object RagiumRecipes {
    @JvmStatic
    fun <R : HTMachineRecipe> of(name: String, factory: (HTRecipeDefinition) -> DataResult<R>): HTRecipeType<HTMachineInput, R> =
        HTRecipeType(
            name,
            object : RecipeSerializer<R> {
                override fun codec(): MapCodec<R> = HTRecipeDefinition.CODEC.flatXmap(factory, HTMachineRecipe::getDefinition)

                override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, R> = HTRecipeDefinition.STREAM_CODEC.map(
                    { definition: HTRecipeDefinition -> factory(definition).orThrow },
                    { recipe: R -> recipe.getDefinition().orThrow },
                )
            },
        )

    @JvmField
    val CENTRIFUGING: HTRecipeType<HTMachineInput, HTCentrifugingRecipe> =
        of("centrifuging", RagiumRecipeFactories::centrifuging)

    @JvmField
    val CRUSHING: HTRecipeType<HTMachineInput, HTCrushingRecipe> =
        of("crushing", RagiumRecipeFactories::crushing)

    @JvmField
    val EXTRACTING: HTRecipeType<HTMachineInput, HTExtractingRecipe> =
        of("extracting", RagiumRecipeFactories::extracting)

    @JvmField
    val REFINING: HTRecipeType<HTMachineInput, HTRefiningRecipe> =
        of("refining", RagiumRecipeFactories::refining)

    @JvmField
    val ALL_TYPES = listOf(
        CENTRIFUGING,
        CRUSHING,
        EXTRACTING,
        REFINING,
    )

    @SubscribeEvent
    fun onResourceReloaded(event: AddReloadListenerEvent) {
        ALL_TYPES.forEach(HTRecipeType<*, *>::setChanged)
    }

    @SubscribeEvent
    fun onRecipesUpdated(event: RecipesUpdatedEvent) {
        ALL_TYPES.forEach { it.reloadCache(event.recipeManager) }
    }
}
