package hiiragi283.ragium.common

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.internal.HTMaterialRegistryImpl
import hiiragi283.ragium.common.internal.RagiumConfig
import net.createmod.ponder.foundation.PonderIndex
import net.minecraft.world.level.block.DispenserBlock
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent
import net.neoforged.neoforge.fluids.DispenseFluidContainer
import org.slf4j.Logger

@Mod(RagiumAPI.MOD_ID)
class RagiumCommon(eventBus: IEventBus, container: ModContainer, dist: Dist) {
    companion object {
        @JvmStatic
        private val LOGGER: Logger = LogUtils.getLogger()
    }

    init {
        eventBus.addListener(::construct)
        eventBus.addListener(::commonSetup)
        eventBus.addListener(::clientSetup)

        RagiumComponentTypes.REGISTER.register(eventBus)

        RagiumFluids.init()
        RagiumFluids.REGISTER.register(eventBus)
        RagiumFluidTypes.REGISTER.register(eventBus)

        RagiumBlocks.init(eventBus)
        RagiumEntityTypes.REGISTER.register(eventBus)
        RagiumItems.REGISTER.register(eventBus)

        RagiumArmorMaterials.REGISTER.register(eventBus)
        RagiumBlockEntityTypes.REGISTER.register(eventBus)
        RagiumCreativeTabs.REGISTER.register(eventBus)
        RagiumMenuTypes.REGISTER.register(eventBus)

        for (addon: RagiumAddon in RagiumAPI.getInstance().getAddons()) {
            addon.onModConstruct(eventBus, dist)
        }

        container.registerConfig(ModConfig.Type.STARTUP, RagiumConfig.SPEC)

        LOGGER.info("Ragium loaded!")
    }

    private fun construct(event: FMLConstructModEvent) {
        event.enqueueWork(HTMaterialRegistryImpl::initRegistry)
    }

    private fun commonSetup(event: FMLCommonSetupEvent) {
        DispenserBlock.registerBehavior(RagiumItems.CRUDE_OIL_BUCKET, DispenseFluidContainer.getInstance())
        DispenserBlock.registerBehavior(RagiumItems.HONEY_BUCKET, DispenseFluidContainer.getInstance())

        for (addon: RagiumAddon in RagiumAPI.getInstance().getAddons()) {
            addon.onCommonSetup(event)
        }
        LOGGER.info("Loaded common setup!")
    }

    private fun clientSetup(event: FMLClientSetupEvent) {
        PonderIndex.addPlugin(RagiumPonderPlugin)

        for (addon: RagiumAddon in RagiumAPI.getInstance().getAddons()) {
            addon.onClientSetup(event)
        }

        LOGGER.info("Loaded client setup!")
    }
}
