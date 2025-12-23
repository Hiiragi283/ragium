package hiiragi283.ragium

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.setup.RagiumCreativeTabs
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipeSerializers
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

        RagiumItems.register(eventBus)

        RagiumCreativeTabs.REGISTER.register(eventBus)
        RagiumRecipeSerializers.REGISTER.register(eventBus)

        LOGGER.info("Ragium loaded!")
    }
}
