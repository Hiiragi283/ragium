package hiiragi283.ragium.client

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.machineTier
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.client.screen.HTMachineContainerScreen
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumMenuTypes
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.item.ItemProperties
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
import org.slf4j.Logger

@EventBusSubscriber(modid = RagiumAPI.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
object RagiumClient {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    @SubscribeEvent
    fun registerClientExtensions(event: RegisterClientExtensionsEvent) {
        RagiumFluids.entries.forEach { fluid: RagiumFluids ->
            event.registerFluidType(fluid, fluid.typeHolder)
        }

        LOGGER.info("Registered client extensions!")
    }

    @SubscribeEvent
    private fun registerMenu(event: RegisterMenuScreensEvent) {
        event.register(RagiumMenuTypes.DEFAULT_MACHINE.get(), ::HTMachineContainerScreen)
        event.register(RagiumMenuTypes.LARGE_MACHINE.get(), ::HTMachineContainerScreen)

        LOGGER.info("Registered machine screens!")
    }

    @SubscribeEvent
    fun clientSetup(event: FMLClientSetupEvent) {
        ItemProperties.registerGeneric(
            RagiumAPI.id("machine_tier"),
        ) { stack: ItemStack, level: ClientLevel?, player: LivingEntity?, seed: Int ->
            when (stack.machineTier) {
                HTMachineTier.PRIMITIVE -> 0.2f
                HTMachineTier.SIMPLE -> 0.4f
                HTMachineTier.BASIC -> 0.6f
                HTMachineTier.ADVANCED -> 0.8f
                HTMachineTier.ELITE -> 1f
            }
        }

        LOGGER.info("Loaded client setup!")
    }
}
