package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.HTDeferredRecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RecipesUpdatedEvent
import net.neoforged.neoforge.event.AddReloadListenerEvent

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object RagiumRecipes {
    @JvmField
    val CENTRIFUGING: HTDeferredRecipeType<SingleRecipeInput, HTCentrifugingRecipe> = HTDeferredRecipeType(
        "centrifuging",
        HTCentrifugingRecipe.CODEC,
        HTCentrifugingRecipe.STREAM_CODEC,
    )

    @JvmField
    val CRUSHING: HTDeferredRecipeType<SingleRecipeInput, HTCrushingRecipe> = HTDeferredRecipeType(
        "crushing",
        HTSingleItemRecipe.codec(::HTCrushingRecipe),
        HTSingleItemRecipe.streamCodec(::HTCrushingRecipe),
    )

    @JvmField
    val EXTRACTING: HTDeferredRecipeType<SingleRecipeInput, HTExtractingRecipe> = HTDeferredRecipeType(
        "extracting",
        HTSingleItemRecipe.codec(::HTExtractingRecipe),
        HTSingleItemRecipe.streamCodec(::HTExtractingRecipe),
    )

    @JvmField
    val ALL_TYPES = listOf(
        CENTRIFUGING,
        CRUSHING,
        EXTRACTING,
    )

    @SubscribeEvent
    fun onResourceReloaded(event: AddReloadListenerEvent) {
        ALL_TYPES.forEach(HTDeferredRecipeType<*, *>::setChanged)
    }

    @SubscribeEvent
    fun onRecipesUpdated(event: RecipesUpdatedEvent) {
        ALL_TYPES.forEach { it.reloadCache(event.recipeManager) }
    }
}
