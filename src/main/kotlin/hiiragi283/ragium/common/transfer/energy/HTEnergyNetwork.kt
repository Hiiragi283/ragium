package hiiragi283.ragium.common.transfer.energy

import hiiragi283.lib.capability.HTEnergyCapabilities
import hiiragi283.ragium.common.attachment.RagiumAttachments
import hiiragi283.ragium.common.block.HTEnergyPipeBlock
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.capabilities.BlockCapabilityCache
import net.neoforged.neoforge.event.level.ChunkEvent
import net.neoforged.neoforge.transfer.energy.EnergyHandler
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal
import net.neoforged.neoforge.transfer.transaction.TransactionContext

class HTEnergyNetwork internal constructor(private val level: ServerLevel) {
    companion object {
        @JvmStatic
        operator fun get(level: ServerLevel): HTEnergyNetwork = level.getData(RagiumAttachments.ENERGY_NETWORK)

        @JvmStatic
        fun onPipeChanged(level: ServerLevel, pos: BlockPos) {
            if (!level.hasData(RagiumAttachments.ENERGY_NETWORK)) return
            val network: HTEnergyNetwork = get(level)
            network.componentByPipe[pos]?.let(network::invalidate)
            for (side: Direction in Direction.entries) {
                network.componentByPipe[pos.relative(side)]?.let(network::invalidate)
            }
        }

        @JvmStatic
        internal fun onChunkChanged(event: ChunkEvent<*>) {
            val level: ServerLevel = event.level as? ServerLevel ?: return
            if (!level.hasData(RagiumAttachments.ENERGY_NETWORK)) return
            val network: HTEnergyNetwork = get(level)
            network.componentsByChunk[event.chunk.pos]?.toList()?.forEach(network::invalidate)
        }
    }

    private val componentByPipe: MutableMap<BlockPos, Component> = hashMapOf()
    private val componentsByChunk: MutableMap<ChunkPos, MutableSet<Component>> = hashMapOf()

    fun forward(inletPos: BlockPos, inletSide: Direction, limit: Int, transaction: TransactionContext): Int {
        val component: Component = componentByPipe[inletPos] ?: discover(inletPos) ?: return 0
        return component.forward(inletPos.relative(inletSide), limit, transaction)
    }

    private fun discover(inletPos: BlockPos): Component? {
        if (loadedState(inletPos)?.block !is HTEnergyPipeBlock) return null
        val members: MutableSet<BlockPos> = linkedSetOf(inletPos)
        val chunks: MutableSet<ChunkPos> = hashSetOf(ChunkPos.containing(inletPos))
        val ports: MutableSet<Port> = linkedSetOf()
        val queue: ArrayDeque<BlockPos> = ArrayDeque<BlockPos>().apply { addLast(inletPos) }
        while (queue.isNotEmpty()) {
            val pos: BlockPos = queue.removeFirst()
            for (side: Direction in Direction.entries) {
                val neighbor: BlockPos = pos.relative(side)
                if (neighbor in members || level.isOutsideBuildHeight(neighbor.y)) continue
                chunks.add(ChunkPos.containing(neighbor))
                val state: BlockState = loadedState(neighbor) ?: continue
                if (state.block is HTEnergyPipeBlock) {
                    members.add(neighbor)
                    queue.addLast(neighbor)
                } else {
                    ports.add(Port(neighbor, side.opposite))
                }
            }
        }

        val component = Component(members, chunks, ports)
        for (pos: BlockPos in members) componentByPipe[pos] = component
        for (chunk: ChunkPos in chunks) componentsByChunk.getOrPut(chunk, ::hashSetOf).add(component)
        return component
    }

    private fun loadedState(pos: BlockPos): BlockState? {
        val chunkPos: ChunkPos = ChunkPos.containing(pos)
        return level.chunkSource.getChunkNow(chunkPos.x, chunkPos.z)?.getBlockState(pos)
    }

    private fun invalidate(component: Component) {
        component.valid = false
        for (pos: BlockPos in component.members) componentByPipe.remove(pos, component)
        for (chunk: ChunkPos in component.chunks) {
            val components: MutableSet<Component> = componentsByChunk[chunk] ?: continue
            components.remove(component)
            if (components.isEmpty()) componentsByChunk.remove(chunk)
        }
    }

    private data class Port(val pos: BlockPos, val side: Direction)

    private inner class Component(val members: Set<BlockPos>, val chunks: Set<ChunkPos>, ports: Set<Port>) : SnapshotJournal<Int>() {
        var valid = true
        private var forwarding = false
        private var portsDirty = true
        private var activePorts: List<Port> = emptyList()
        private var nextPort = 0
        private val caches: Map<Port, BlockCapabilityCache<EnergyHandler, Direction?>> = ports.associateWith { port ->
            HTEnergyCapabilities.createCache(
                level,
                port.pos,
                port.side,
                { valid },
                { portsDirty = true },
            )
        }

        fun forward(sourcePos: BlockPos, limit: Int, transaction: TransactionContext): Int {
            if (!valid || forwarding) return 0
            forwarding = true
            try {
                if (portsDirty) {
                    portsDirty = false
                    activePorts = caches.keys.filter { caches.getValue(it).capability != null }
                }

                val ports: List<Port> = activePorts
                val start: Int = nextPort
                var remaining: Int = limit
                for (offset: Int in ports.indices) {
                    if (remaining == 0 || !valid) break
                    val index: Int = (start + offset) % ports.size
                    val port: Port = ports[index]
                    if (port.pos == sourcePos) continue

                    val handler: EnergyHandler = caches.getValue(port).capability ?: continue
                    val accepted: Int = handler.insert(remaining, transaction)
                    if (accepted > 0) {
                        remaining -= accepted
                        updateSnapshots(transaction)
                        nextPort = index + 1
                    }
                }
                return limit - remaining
            } finally {
                forwarding = false
            }
        }

        override fun createSnapshot(): Int = nextPort

        override fun revertToSnapshot(snapshot: Int) {
            nextPort = snapshot
        }
    }
}
