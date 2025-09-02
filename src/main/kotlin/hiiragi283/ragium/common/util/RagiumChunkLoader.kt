package hiiragi283.ragium.common.util

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.block.entity.device.HTDimensionalAnchorBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.world.chunk.LoadingValidationCallback
import net.neoforged.neoforge.common.world.chunk.RegisterTicketControllersEvent
import net.neoforged.neoforge.common.world.chunk.TicketController
import net.neoforged.neoforge.common.world.chunk.TicketHelper
import net.neoforged.neoforge.common.world.chunk.TicketSet
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent
import net.neoforged.neoforge.event.server.ServerStoppingEvent

/**
 * @see [appeng.server.services.ChunkLoadingService]
 */
@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object RagiumChunkLoader : LoadingValidationCallback {
    private var isValid: Boolean = false
    private val controller = TicketController(RagiumAPI.id("chunk_loader"), this)

    override fun validateTickets(level: ServerLevel, ticketHelper: TicketHelper) {
        ticketHelper.blockTickets.forEach { (pos: BlockPos, _: TicketSet) ->
            val blockEntity: BlockEntity? = level.getBlockEntity(pos)
            if (blockEntity !is HTDimensionalAnchorBlockEntity) {
                ticketHelper.removeAllTickets(pos)
            }
        }
    }

    @JvmStatic
    fun forceChunk(level: ServerLevel, pos: BlockPos, chunkPos: ChunkPos): Boolean =
        isValid && controller.forceChunk(level, pos, chunkPos.x, chunkPos.z, true, true)

    @JvmStatic
    fun releaseChunk(level: ServerLevel, pos: BlockPos, chunkPos: ChunkPos): Boolean =
        isValid && controller.forceChunk(level, pos, chunkPos.x, chunkPos.z, false, true)

    //    Event    //

    @JvmStatic
    fun registerController(event: RegisterTicketControllersEvent) {
        event.register(controller)
    }

    @SubscribeEvent
    fun onServerAboutToStart(event: ServerAboutToStartEvent) {
        this.isValid = true
    }

    @SubscribeEvent
    fun onServerStopping(event: ServerStoppingEvent) {
        this.isValid = false
    }
}
