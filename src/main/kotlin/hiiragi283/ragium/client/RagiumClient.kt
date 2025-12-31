package hiiragi283.ragium.client

import com.mojang.logging.LogUtils
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.client.HTSimpleFluidExtensions
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.client.gui.screen.HTDryerScreen
import hiiragi283.ragium.client.gui.screen.HTMelterScreen
import hiiragi283.ragium.client.gui.screen.HTPyrolyzerScreen
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.level.ItemLike
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
import net.neoforged.neoforge.client.gui.ConfigurationScreen
import net.neoforged.neoforge.client.gui.IConfigScreenFactory
import net.neoforged.neoforge.client.model.DynamicFluidContainerModel
import org.slf4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.LOADING_CONTEXT
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import java.awt.Color

@Mod(value = RagiumAPI.MOD_ID, dist = [Dist.CLIENT])
object RagiumClient {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    init {
        val eventBus: IEventBus = MOD_BUS

        eventBus.addListener(::registerItemColors)
        eventBus.addListener(::registerClientExtensions)
        eventBus.addListener(::registerScreens)

        LOADING_CONTEXT.activeContainer.registerExtensionPoint(
            IConfigScreenFactory::class.java,
            IConfigScreenFactory(::ConfigurationScreen),
        )

        LOGGER.info("Hiiragi-Core loaded on client side!")
    }

    @JvmStatic
    private fun registerItemColors(event: RegisterColorHandlersEvent.Item) {
        // Buckets
        val bucketColor = DynamicFluidContainerModel.Colors()
        for (item: ItemLike in RagiumFluids.REGISTER.asItemSequence()) {
            event.register(bucketColor, item)
        }
        // Potion Drop
        event.register(
            { stack: ItemStack, tint: Int ->
                if (tint == 0) {
                    stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).color
                } else {
                    -1
                }
            },
            RagiumItems.POTION_DROP,
        )
        LOGGER.info("Registered item colors!")
    }

    @JvmStatic
    private fun registerClientExtensions(event: RegisterClientExtensionsEvent) {
        event.dull(RagiumFluids.SLIME, Color(0x66cc66))
        event.molten(RagiumFluids.GELLED_EXPLOSIVE, Color(0x339933))
        event.dull(RagiumFluids.CRUDE_BIO, Color(0x336600))
        event.dull(RagiumFluids.ETHANOL, Color(0xccffcc))
        event.clear(RagiumFluids.BIOFUEL, Color(0x99cc00))

        event.dull(RagiumFluids.CRUDE_OIL, Color(0x333333))
        event.dull(RagiumFluids.NAPHTHA, Color(0xff6633))
        event.clear(RagiumFluids.FUEL, Color(0xcc3300))
        event.dull(RagiumFluids.LUBRICANT, Color(0xff9900))
        event.molten(RagiumFluids.DESTABILIZED_RAGINITE, Color(0xff0033))

        event.clear(RagiumFluids.COOLANT, Color(0x009999))
        event.dull(RagiumFluids.CREOSOTE, Color(0x663333))
    }

    private fun registerScreens(event: RegisterMenuScreensEvent) {
        event.register(RagiumMenuTypes.DRYER.get(), ::HTDryerScreen)
        event.register(RagiumMenuTypes.MELTER.get(), ::HTMelterScreen)
        event.register(RagiumMenuTypes.PYROLYZER.get(), ::HTPyrolyzerScreen)

        LOGGER.info("Registered screens!")
    }

    //    Extensions    //

    private fun RegisterClientExtensionsEvent.clear(content: HTFluidContent<*, *, *>, color: Color) {
        this.registerFluidType(HTSimpleFluidExtensions.clear(color), content.getFluidType())
    }

    private fun RegisterClientExtensionsEvent.dull(content: HTFluidContent<*, *, *>, color: Color) {
        this.registerFluidType(HTSimpleFluidExtensions.dull(color), content.getFluidType())
    }

    private fun RegisterClientExtensionsEvent.molten(content: HTFluidContent<*, *, *>, color: Color) {
        this.registerFluidType(HTSimpleFluidExtensions.molten(color), content.getFluidType())
    }
}
