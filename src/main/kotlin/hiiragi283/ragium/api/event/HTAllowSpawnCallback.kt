package hiiragi283.ragium.api.event

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.fabricmc.fabric.api.util.TriState
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnReason
import net.minecraft.util.math.BlockPos
import net.minecraft.world.ServerWorldAccess

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

    fun canSpawn(
        entityType: EntityType<*>,
        world: ServerWorldAccess,
        pos: BlockPos,
        reason: SpawnReason,
    ): TriState
}
