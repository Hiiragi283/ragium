package hiiragi283.ragium.common

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.extension.isModLoaded
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTRegisterMaterialEvent
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.internal.HTMachineRegistryImpl
import hiiragi283.ragium.common.internal.HTMaterialRegistryImpl
import hiiragi283.ragium.integration.RagiumEvilIntegration
import hiiragi283.ragium.integration.RagiumMekIntegration
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent
import org.slf4j.Logger

@Mod(RagiumAPI.MOD_ID)
class RagiumCommon(eventBus: IEventBus, container: ModContainer) {
    companion object {
        @JvmField
        val LOGGER: Logger = LogUtils.getLogger()
    }

    init {
        eventBus.addListener(EventPriority.HIGHEST, ::registerMaterial)
        eventBus.addListener(::construct)
        eventBus.addListener(::commonSetup)
        eventBus.addListener(::sendMessage)
        if (isModLoaded("mekanism")) {
            RagiumMekIntegration.init(eventBus)
        }
        if (isModLoaded("evilcraft")) {
            RagiumEvilIntegration.init(eventBus)
        }

        RagiumComponentTypes.REGISTER.register(eventBus)

        HTMachineRegistryImpl.initRegistry()

        RagiumFluids.register(eventBus)
        RagiumBlocks.REGISTER.register(eventBus)
        RagiumEntityTypes.REGISTER.register(eventBus)
        RagiumItems.REGISTER.register(eventBus)

        RagiumBlockEntityTypes.REGISTER.register(eventBus)
        RagiumCreativeTabs.REGISTER.register(eventBus)
        RagiumMenuTypes.REGISTER.register(eventBus)
        RagiumRecipes.SERIALIZER.register(eventBus)
        RagiumRecipes.TYPE.register(eventBus)
        RagiumMachineRecipeConditions.REGISTER.register(eventBus)
        RagiumMultiblockComponentTypes.REGISTER.register(eventBus)

        container.registerConfig(ModConfig.Type.STARTUP, RagiumConfig.SPEC)

        LOGGER.info("Ragium loaded!")
    }

    private fun construct(event: FMLConstructModEvent) {
        HTMaterialRegistryImpl.initRegistry()
    }

    private fun commonSetup(event: FMLCommonSetupEvent) {
        LOGGER.info("Loaded common setup!")
    }

    private fun sendMessage(event: InterModEnqueueEvent) {
        LOGGER.info("Sent IMC Messages!")
    }

    //    Material    //

    fun registerMaterial(event: HTRegisterMaterialEvent) {
        // Alloy
        event.register(RagiumMaterialKeys.DEEP_STEEL, HTMaterialType.ALLOY)
        event.register(RagiumMaterialKeys.NETHERITE, HTMaterialType.ALLOY)
        event.register(RagiumMaterialKeys.RAGI_ALLOY, HTMaterialType.ALLOY)
        event.register(RagiumMaterialKeys.RAGI_STEEL, HTMaterialType.ALLOY)
        event.register(RagiumMaterialKeys.REFINED_RAGI_STEEL, HTMaterialType.ALLOY)
        event.register(RagiumMaterialKeys.STEEL, HTMaterialType.ALLOY)

        event.register(RagiumMaterialKeys.BRASS, HTMaterialType.ALLOY)
        event.register(RagiumMaterialKeys.BRONZE, HTMaterialType.ALLOY)
        event.register(RagiumMaterialKeys.ELECTRUM, HTMaterialType.ALLOY)
        event.register(RagiumMaterialKeys.INVAR, HTMaterialType.ALLOY)
        // Dust
        event.register(RagiumMaterialKeys.ALKALI, HTMaterialType.DUST)
        event.register(RagiumMaterialKeys.ASH, HTMaterialType.DUST)
        event.register(RagiumMaterialKeys.CARBON, HTMaterialType.DUST)
        event.register(RagiumMaterialKeys.WOOD, HTMaterialType.DUST)
        // Gem
        event.register(RagiumMaterialKeys.AMETHYST, HTMaterialType.GEM)
        event.register(RagiumMaterialKeys.CINNABAR, HTMaterialType.GEM)
        event.register(RagiumMaterialKeys.COAL, HTMaterialType.GEM)
        event.register(RagiumMaterialKeys.CRYOLITE, HTMaterialType.GEM)
        event.register(RagiumMaterialKeys.DIAMOND, HTMaterialType.GEM)
        event.register(RagiumMaterialKeys.EMERALD, HTMaterialType.GEM)
        event.register(RagiumMaterialKeys.FLUORITE, HTMaterialType.GEM)
        event.register(RagiumMaterialKeys.LAPIS, HTMaterialType.GEM)

        event.register(RagiumMaterialKeys.NETHERITE_SCRAP, HTMaterialType.GEM)
        event.register(RagiumMaterialKeys.PERIDOT, HTMaterialType.GEM)
        event.register(RagiumMaterialKeys.QUARTZ, HTMaterialType.GEM)
        event.register(RagiumMaterialKeys.RAGI_CRYSTAL, HTMaterialType.GEM)
        event.register(RagiumMaterialKeys.RUBY, HTMaterialType.GEM)
        event.register(RagiumMaterialKeys.SAPPHIRE, HTMaterialType.GEM)
        // Metal
        event.register(RagiumMaterialKeys.ALUMINUM, HTMaterialType.METAL)
        event.register(RagiumMaterialKeys.COPPER, HTMaterialType.METAL)
        event.register(RagiumMaterialKeys.GOLD, HTMaterialType.METAL)
        event.register(RagiumMaterialKeys.IRON, HTMaterialType.METAL)

        event.register(RagiumMaterialKeys.RAGIUM, HTMaterialType.METAL)
        event.register(RagiumMaterialKeys.ECHORIUM, HTMaterialType.METAL)
        event.register(RagiumMaterialKeys.FIERIUM, HTMaterialType.METAL)
        event.register(RagiumMaterialKeys.DRAGONIUM, HTMaterialType.METAL)

        event.register(RagiumMaterialKeys.IRIDIUM, HTMaterialType.METAL)
        event.register(RagiumMaterialKeys.LEAD, HTMaterialType.METAL)
        event.register(RagiumMaterialKeys.NICKEL, HTMaterialType.METAL)
        event.register(RagiumMaterialKeys.PLATINUM, HTMaterialType.METAL)
        event.register(RagiumMaterialKeys.PLUTONIUM, HTMaterialType.METAL)
        event.register(RagiumMaterialKeys.SILVER, HTMaterialType.METAL)
        event.register(RagiumMaterialKeys.TIN, HTMaterialType.METAL)
        event.register(RagiumMaterialKeys.TITANIUM, HTMaterialType.METAL)
        event.register(RagiumMaterialKeys.TUNGSTEN, HTMaterialType.METAL)
        event.register(RagiumMaterialKeys.URANIUM, HTMaterialType.METAL)
        event.register(RagiumMaterialKeys.ZINC, HTMaterialType.METAL)
        // Mineral
        event.register(RagiumMaterialKeys.BAUXITE, HTMaterialType.MINERAL)
        event.register(RagiumMaterialKeys.CRUDE_RAGINITE, HTMaterialType.MINERAL)
        event.register(RagiumMaterialKeys.NITER, HTMaterialType.MINERAL)
        event.register(RagiumMaterialKeys.RAGINITE, HTMaterialType.MINERAL)
        event.register(RagiumMaterialKeys.REDSTONE, HTMaterialType.MINERAL)
        event.register(RagiumMaterialKeys.SALT, HTMaterialType.MINERAL)
        event.register(RagiumMaterialKeys.SULFUR, HTMaterialType.MINERAL)

        event.register(RagiumMaterialKeys.GALENA, HTMaterialType.MINERAL)
        event.register(RagiumMaterialKeys.PYRITE, HTMaterialType.MINERAL)
        event.register(RagiumMaterialKeys.SPHALERITE, HTMaterialType.MINERAL)
    }
}
