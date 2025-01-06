package hiiragi283.ragium.api.event

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.fabricmc.fabric.api.util.TriState
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnReason
import net.minecraft.util.math.BlockPos
import net.minecraft.world.ServerWorldAccess

/**
 * モブのスポーンを判定するイベント
 *
 * [net.minecraft.entity.SpawnRestriction.canSpawn]にフックされています。
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
     * @param entityType スポーンしようとしているエンティティの[EntityType]
     * @param world スポーンしようとしているワールド
     * @param pos スポーンしようとしている座標
     * @param reason スポーンする理由
     * @return [TriState.TRUE]の場合は強制的にスポーン，[TriState.FALSE]の場合はスポーンしない, [TriState.DEFAULT]の場合はデフォルトの判定にゆだねる
     */
    fun canSpawn(
        entityType: EntityType<*>,
        world: ServerWorldAccess,
        pos: BlockPos,
        reason: SpawnReason,
    ): TriState
}
