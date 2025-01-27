package hiiragi283.ragium.common

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.internal.HTMachineRegistryImpl
import hiiragi283.ragium.common.internal.HTMaterialRegistryImpl
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent
import org.slf4j.Logger

@Mod(RagiumAPI.MOD_ID)
class RagiumCommon(eventBus: IEventBus, container: ModContainer) {
    companion object {
        @JvmField
        val LOGGER: Logger = LogUtils.getLogger()
    }

    init {
        eventBus.addListener(::construct)

        RagiumComponentTypes.REGISTER.register(eventBus)

        RagiumMachineKeys
        RagiumMaterialKeys

        HTMachineRegistryImpl.registerBlocks()

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
        event.enqueueWork(HTMachineRegistryImpl::modifyProperties)
        event.enqueueWork(HTMaterialRegistryImpl::initRegistry)
    }
}
