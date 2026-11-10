package hiiragi283.ragium.common.attachment

import hiiragi283.lib.registry.HTDeferredRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.transfer.energy.HTEnergyNetwork
import hiiragi283.ragium.common.transfer.energy.HTEnergyNetwork.Companion.onChunkChanged
import net.minecraft.server.level.ServerLevel
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.attachment.IAttachmentHolder
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.level.ChunkEvent
import net.neoforged.neoforge.registries.NeoForgeRegistries
import thedarkcolour.kotlinforforge.neoforge.kotlin.supply
import java.util.function.Supplier

data object RagiumAttachments {
    @JvmField
    val REGISTER: HTDeferredRegister<AttachmentType<*>> = HTDeferredRegister(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, RagiumAPI.MOD_ID)

    @JvmField
    val ENERGY_NETWORK: Supplier<AttachmentType<HTEnergyNetwork>> = REGISTER.register(
        "energy_network",
        supply(
            AttachmentType.builder { holder: IAttachmentHolder ->
                val level: ServerLevel = holder as? ServerLevel ?: error("Network data must be existing in server level")
                HTEnergyNetwork(level)
            }.build(),
        ),
    )

    @JvmStatic
    fun register(eventBus: IEventBus) {
        REGISTER.register(eventBus)

        NeoForge.EVENT_BUS.addListener { event: ChunkEvent.Load -> onChunkChanged(event) }
        NeoForge.EVENT_BUS.addListener { event: ChunkEvent.Unload -> onChunkChanged(event) }
    }
}
