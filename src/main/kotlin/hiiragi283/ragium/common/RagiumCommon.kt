package hiiragi283.ragium.common

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.recipe.RagiumRecipes
import hiiragi283.ragium.api.registry.HTDeferredRecipeType
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumCreativeTabs
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.internal.HTMaterialRegistryImpl
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent
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

        RagiumBlocks.init(eventBus)
        RagiumItems.init(eventBus)

        RagiumBlockEntityTypes.REGISTER.register(eventBus)
        RagiumCreativeTabs.REGISTER.register(eventBus)

        RagiumRecipes
        HTDeferredRecipeType.init(eventBus)

        for (addon: RagiumAddon in RagiumAPI.getInstance().getAddons()) {
            addon.onModConstruct(eventBus, dist)
        }

        LOGGER.info("Ragium loaded!")
    }

    private fun construct(event: FMLConstructModEvent) {
        event.enqueueWork(HTMaterialRegistryImpl::initRegistry)
    }

    private fun commonSetup(event: FMLCommonSetupEvent) {
        event.enqueueWork {
            // DispenserBlock.registerBehavior(RagiumItems.CRUDE_OIL_BUCKET, DispenseFluidContainer.getInstance())
            // DispenserBlock.registerBehavior(RagiumItems.HONEY_BUCKET, DispenseFluidContainer.getInstance())
        }

        for (addon: RagiumAddon in RagiumAPI.getInstance().getAddons()) {
            addon.onCommonSetup(event)
        }
        LOGGER.info("Loaded common setup!")
    }
}
