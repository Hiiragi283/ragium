package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeBase
import hiiragi283.ragium.api.recipe.base.HTMultiItemRecipe
import hiiragi283.ragium.api.recipe.base.HTRecipeType
import hiiragi283.ragium.api.recipe.base.HTSingleItemRecipe
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RecipesUpdatedEvent
import net.neoforged.neoforge.event.AddReloadListenerEvent

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object HTRecipeTypes {
    @JvmField
    val ASSEMBLER = HTRecipeType<HTAssemblerRecipe>(
        HTMachineType.ASSEMBLER,
        HTMultiItemRecipe.Serializer(::HTAssemblerRecipe),
    )

    @JvmField
    val BLAST_FURNACE = HTRecipeType<HTBlastFurnaceRecipe>(
        HTMachineType.BLAST_FURNACE,
        HTMultiItemRecipe.Serializer(::HTBlastFurnaceRecipe),
    )

    @JvmField
    val BREWERY = HTRecipeType<HTBreweryRecipe>(
        HTMachineType.ALCHEMICAL_BREWERY,
        HTBreweryRecipe.CODEC,
        HTBreweryRecipe.STREAM_CODEC,
    )

    @JvmField
    val COMPRESSOR = HTRecipeType<HTCompressorRecipe>(
        HTMachineType.COMPRESSOR,
        HTSingleItemRecipe.Serializer(::HTCompressorRecipe),
    )

    @JvmField
    val ENCHANTER = HTRecipeType<HTEnchanterRecipe>(
        HTMachineType.ARCANE_ENCHANTER,
        HTEnchanterRecipe.CODEC,
        HTEnchanterRecipe.STREAM_CODEC,
    )

    @JvmField
    val EXTRACTOR = HTRecipeType<HTExtractorRecipe>(
        HTMachineType.EXTRACTOR,
        HTExtractorRecipe.CODEC,
        HTExtractorRecipe.STREAM_CODEC,
    )

    @JvmField
    val GRINDER = HTRecipeType<HTGrinderRecipe>(
        HTMachineType.GRINDER,
        HTSingleItemRecipe.Serializer(::HTGrinderRecipe),
    )

    @JvmField
    val GROWTH_CHAMBER = HTRecipeType<HTGrowthChamberRecipe>(
        HTMachineType.GROWTH_CHAMBER,
        HTGrowthChamberRecipe.CODEC,
        HTGrowthChamberRecipe.STREAM_CODEC,
    )

    @JvmField
    val INFUSER = HTRecipeType<HTInfuserRecipe>(
        HTMachineType.INFUSER,
        HTInfuserRecipe.CODEC,
        HTInfuserRecipe.STREAM_CODEC,
    )

    @JvmField
    val LASER_ASSEMBLY = HTRecipeType<HTLaserAssemblyRecipe>(
        HTMachineType.LASER_ASSEMBLY,
        HTSingleItemRecipe.Serializer(::HTLaserAssemblyRecipe),
    )

    @JvmField
    val MIXER = HTRecipeType<HTMixerRecipe>(
        HTMachineType.MIXER,
        HTMixerRecipe.CODEC,
        HTMixerRecipe.STREAM_CODEC,
    )

    @JvmField
    val REFINERY = HTRecipeType<HTRefineryRecipe>(
        HTMachineType.REFINERY,
        HTRefineryRecipe.CODEC,
        HTRefineryRecipe.STREAM_CODEC,
    )

    @JvmField
    val SOLIDIFIER = HTRecipeType<HTSolidifierRecipe>(
        HTMachineType.SOLIDIFIER,
        HTSolidifierRecipe.CODEC,
        HTSolidifierRecipe.STREAM_CODEC,
    )

    @JvmField
    val ALL_TYPES: List<HTRecipeType<out HTMachineRecipeBase>> = listOf(
        ASSEMBLER,
        BLAST_FURNACE,
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
