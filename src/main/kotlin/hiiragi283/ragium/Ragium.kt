package hiiragi283.ragium

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumCreativeTabs
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod
import org.slf4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(RagiumAPI.MOD_ID)
object Ragium {
    @JvmStatic
    private val LOGGER: Logger = LogUtils.getLogger()

    init {
        val eventBus: IEventBus = MOD_BUS

        RagiumDataComponents.REGISTER.register(eventBus)

        RagiumFluids.register(eventBus)
        RagiumBlocks.register(eventBus)
        RagiumItems.register(eventBus)

        RagiumCreativeTabs.REGISTER.register(eventBus)
        RagiumRecipeSerializers.REGISTER.register(eventBus)
        RagiumRecipeTypes.REGISTER.register(eventBus)

        LOGGER.info("Ragium loaded!")
    }
}
