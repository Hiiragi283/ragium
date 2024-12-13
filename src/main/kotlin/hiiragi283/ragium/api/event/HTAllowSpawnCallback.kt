package hiiragi283.ragium.api.event

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.fabricmc.fabric.api.util.TriState
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnReason
import net.minecraft.util.math.BlockPos
import net.minecraft.world.ServerWorldAccess

/**
 * Callback for allowing mob spawn, is hooked before mod spawning at [net.minecraft.entity.SpawnRestriction.canSpawn]
 */
fun interface HTAllowSpawnCallback {
    companion object {
        @JvmField
        val EVENT: Event<HTAllowSpawnCallback> =
            EventFactory.createArrayBacked(HTAllowSpawnCallback::class.java) { listeners: Array<HTAllowSpawnCallback> ->
                HTAllowSpawnCallback { entityType: EntityType<*>, world: ServerWorldAccess, pos: BlockPos, reason: SpawnReason ->
                    var result = TriState.DEFAULT
                    for (listener: HTAllowSpawnCallback in listeners) {
                        result = listener.canSpawn(entityType, world, pos, reason)
                        if (result != TriState.DEFAULT) break
                    }
                    result
                }
            }
    }

    /**
     * @param entityType an [EntityType] which try to spawn
     * @param world a [ServerWorldAccess] which the entity will spawn in
     * @param pos a [BlockPos] which the entity will spawn at
     * @param reason a [SpawnReason] which the entity spawn by
     * @return Force spawning if [TriState.TRUE], Disable spawning if [TriState.FALSE], or use default condition if [TriState.DEFAULT]
     */
    fun canSpawn(
        entityType: EntityType<*>,
        world: ServerWorldAccess,
        pos: BlockPos,
        reason: SpawnReason,
    ): TriState
}
