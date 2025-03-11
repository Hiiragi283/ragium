package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.base.HTMachineRecipe
import hiiragi283.ragium.api.recipe.base.HTRecipeType
import hiiragi283.ragium.api.recipe.base.HTSingleItemRecipe
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RecipesUpdatedEvent
import net.neoforged.neoforge.event.AddReloadListenerEvent

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object HTRecipeTypes {
    @JvmField
    val ALLOY_FURNACE =
        HTRecipeType<HTAlloyFurnaceRecipe>("alloy", HTAlloyFurnaceRecipe.CODEC, HTAlloyFurnaceRecipe.STREAM_CODEC)

    @JvmField
    val ASSEMBLER =
        HTRecipeType<HTAssemblerRecipe>("assembler", HTAssemblerRecipe.CODEC, HTAssemblerRecipe.STREAM_CODEC)

    @JvmField
    val BREWERY = HTRecipeType<HTBreweryRecipe>("brewery", HTBreweryRecipe.CODEC, HTBreweryRecipe.STREAM_CODEC)

    @JvmField
    val COMPRESSOR = HTRecipeType<HTCompressorRecipe>("compressor", HTSingleItemRecipe.Serializer(::HTCompressorRecipe))

    @JvmField
    val ENCHANTER =
        HTRecipeType<HTEnchanterRecipe>("enchanter", HTEnchanterRecipe.CODEC, HTEnchanterRecipe.STREAM_CODEC)

    @JvmField
    val EXTRACTOR =
        HTRecipeType<HTExtractorRecipe>("extractor", HTExtractorRecipe.CODEC, HTExtractorRecipe.STREAM_CODEC)

    @JvmField
    val GRINDER = HTRecipeType<HTGrinderRecipe>("grinder", HTSingleItemRecipe.Serializer(::HTGrinderRecipe))

    @JvmField
    val GROWTH_CHAMBER = HTRecipeType<HTGrowthChamberRecipe>("growth", HTGrowthChamberRecipe.SERIALIZER)

    @JvmField
    val INFUSER = HTRecipeType<HTInfuserRecipe>("infuser", HTInfuserRecipe.CODEC, HTInfuserRecipe.STREAM_CODEC)

    @JvmField
    val LASER_ASSEMBLY =
        HTRecipeType<HTLaserAssemblyRecipe>("laser", HTSingleItemRecipe.Serializer(::HTLaserAssemblyRecipe))

    @JvmField
    val MIXER = HTRecipeType<HTMixerRecipe>("mixer", HTMixerRecipe.CODEC, HTMixerRecipe.STREAM_CODEC)

    @JvmField
    val REFINERY = HTRecipeType<HTRefineryRecipe>("refinery", HTRefineryRecipe.CODEC, HTRefineryRecipe.STREAM_CODEC)

    @JvmField
    val SOLIDIFIER =
        HTRecipeType<HTSolidifierRecipe>("solidifier", HTSolidifierRecipe.CODEC, HTSolidifierRecipe.STREAM_CODEC)

    @JvmField
    val ALL_TYPES: List<HTRecipeType<out HTMachineRecipe>> = listOf(
        ALLOY_FURNACE,
        ASSEMBLER,
        BREWERY,
        COMPRESSOR,
        ENCHANTER,
        EXTRACTOR,
        GRINDER,
        GROWTH_CHAMBER,
        INFUSER,
        LASER_ASSEMBLY,
        MIXER,
        REFINERY,
        SOLIDIFIER,
    )

    @SubscribeEvent
    fun onResourceReloaded(event: AddReloadListenerEvent) {
        ALL_TYPES.forEach(HTRecipeType<*>::setChanged)
    }

    @SubscribeEvent
    fun onRecipesUpdated(event: RecipesUpdatedEvent) {
        ALL_TYPES.forEach { it.reloadCache(event.recipeManager) }
    }
}
