package hiiragi283.ragium.common

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.registry.HTMachineRecipeType
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.internal.HTMaterialRegistryImpl
import hiiragi283.ragium.common.storage.energy.HTEnergyNetworkManagerImpl
import net.minecraft.world.level.block.DispenserBlock
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.fluids.DispenseFluidContainer
import org.slf4j.Logger

@Mod(RagiumAPI.MOD_ID)
class RagiumCommon(eventBus: IEventBus, container: ModContainer, dist: Dist) {
    companion object {
        @JvmStatic
        private val LOGGER: Logger = LogUtils.getLogger()
    }

    init {
        NeoForgeMod.enableMilkFluid()

        eventBus.addListener(::construct)
        eventBus.addListener(::commonSetup)

        RagiumComponentTypes.REGISTER.register(eventBus)

        RagiumFluids.init()
        RagiumFluids.REGISTER.register(eventBus)
        RagiumFluidTypes.REGISTER.register(eventBus)

        RagiumBlocks.init(eventBus)
        RagiumItems.init(eventBus)

        RagiumArmorMaterials.REGISTER.register(eventBus)
        RagiumBlockEntityTypes.REGISTER.register(eventBus)
        RagiumCreativeTabs.REGISTER.register(eventBus)

        RagiumRecipes
        HTMachineRecipeType.init(eventBus)

        HTEnergyNetworkManagerImpl

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
            DispenserBlock.registerBehavior(RagiumItems.CRUDE_OIL_BUCKET, DispenseFluidContainer.getInstance())
        }

        for (addon: RagiumAddon in RagiumAPI.getInstance().getAddons()) {
            addon.onCommonSetup(event)
        }
        LOGGER.info("Loaded common setup!")
    }
}
