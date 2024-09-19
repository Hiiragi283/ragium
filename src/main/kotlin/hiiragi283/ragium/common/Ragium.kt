package hiiragi283.ragium.common

import hiiragi283.ragium.common.data.HTHardModeResourceCondition
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.recipe.HTMachineRecipe
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import net.minecraft.world.gen.GenerationStep
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Ragium : ModInitializer {
    const val MOD_ID = "ragium"
    const val MOD_NAME = "Ragium"

    @JvmStatic
    fun id(path: String): Identifier = Identifier.of(MOD_ID, path)

    @JvmField
    val logger: Logger = LoggerFactory.getLogger(MOD_NAME)

    @JvmStatic
    inline fun log(action: Logger.() -> Unit) {
        logger.action()
    }

    @JvmStatic
    var config: RagiumConfig = RagiumConfig()
        private set

    override fun onInitialize() {
        log { info("Registering game objects...") }

        AutoConfig.register(RagiumConfig::class.java, ::GsonConfigSerializer)
        config = AutoConfig.getConfigHolder(RagiumConfig::class.java).get()

        RagiumComponentTypes
        RagiumBlockEntityTypes
        RagiumBlocks
        RagiumItems

        RagiumAdvancementCriteria
        HTMachineRecipe.Serializer

        registerModifications()

        RagiumItemGroup.init()
        RagiumBlocks.addSupportedBlocks()
        RagiumCauldronBehaviors.init()
        HTHardModeResourceCondition.init()
        RagiumEnergyProviders.init()

        RagiumItems.registerEvents()

        log { info("Ragium initialized!") }
    }

    private fun registerModifications() {
        BiomeModifications.addFeature(
            BiomeSelectors.foundInOverworld(),
            GenerationStep.Feature.UNDERGROUND_ORES,
            RegistryKey.of(RegistryKeys.PLACED_FEATURE, Ragium.id("ore_raginite")),
        )
    }
}
