package hiiragi283.ragium.integration.replication

import com.buuz135.replication.api.IMatterType
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.addon.HTAddon
import hiiragi283.ragium.api.addon.RagiumAddon
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import java.awt.Color

@HTAddon(RagiumConst.REPLICATION)
object RagiumReplicationAddon : RagiumAddon {
    //    Matter    //

    @JvmField
    val MATTER_REGISTER = HTDeferredMatterTypeRegister(RagiumAPI.MOD_ID)

    @JvmField
    val MATTER_RAGIUM: HTDeferredMatterType<IMatterType> =
        MATTER_REGISTER.registerType("ragium", Color(0xff0033), 128)

    @JvmField
    val MATTER_AZURE: HTDeferredMatterType<IMatterType> =
        MATTER_REGISTER.registerType(RagiumConst.AZURE, Color(0x656da1), 128)

    @JvmField
    val MATTER_DEEP: HTDeferredMatterType<IMatterType> =
        MATTER_REGISTER.registerType("deep", Color(0x404d5a), 128)

    //    RagiumAddon    //

    override fun onModConstruct(eventBus: IEventBus, dist: Dist) {
        MATTER_REGISTER.register(eventBus)
    }
}
