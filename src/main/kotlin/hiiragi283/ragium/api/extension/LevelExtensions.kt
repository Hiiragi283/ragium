package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.block.entity.HTBlockEntity
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.CommonLevelAccessor
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.chunk.ChunkAccess
import net.minecraft.world.phys.Vec3

//    BlockGetter    //

fun BlockGetter.getHTBlockEntity(pos: BlockPos): HTBlockEntity? = getBlockEntity(pos) as? HTBlockEntity

fun BlockGetter.getMachineEntity(pos: BlockPos): HTMachineBlockEntity? = getBlockEntity(pos) as? HTMachineBlockEntity

/**
 * 指定した[pos]に存在する[BlockState]を置き換えようとします。
 * @param doBreak `true`の場合は[CommonLevelAccessor.destroyBlock]，それ以外の場合は[CommonLevelAccessor.setBlock]
 * @return サーバー側で置換に成功したら`true`，それ以外の場合は`false`
 */
fun CommonLevelAccessor.replaceBlockState(pos: BlockPos, doBreak: Boolean = false, transform: (BlockState) -> BlockState?): Boolean {
    val stateIn: BlockState = getBlockState(pos)
    return when {
        isClientSide -> false
        else ->
            transform(stateIn)?.let { state: BlockState ->
                if (doBreak) destroyBlock(pos, false)
                setBlock(pos, state, 3)
            } == true
    }
}

//    Level    //

/**
 * [Level]を[ServerLevel]にキャストしようとします。
 * @return [ServerLevel]にキャストできなかった場合は`null`
 */
fun Level?.asServerLevel(): ServerLevel? = this as? ServerLevel

/**
 * 指定した[item]を[entity]の足元にドロップします。
 * @return [ItemEntity]がスポーンした場合は`true`，それ以外の場合は`false`
 */
fun dropStackAt(entity: Entity, item: ItemLike, count: Int = 1): Boolean = dropStackAt(entity, ItemStack(item, count))

/**
 * 指定した[stack]を[entity]の足元にドロップします。
 * @return [ItemEntity]がスポーンした場合は`true`，それ以外の場合は`false`
 */
fun dropStackAt(entity: Entity, stack: ItemStack): Boolean = dropStackAt(entity.level(), entity.position(), stack)

/**
 * 指定した[item]を[pos]にドロップします。
 * @return [ItemEntity]がスポーンした場合は`true`，それ以外の場合は`false`
 */
fun dropStackAt(
    level: Level,
    pos: BlockPos,
    item: ItemLike,
    count: Int = 1,
): Boolean = dropStackAt(level, pos, ItemStack(item, count))

/**
 * 指定した[stack]を[pos]にドロップします。
 * @return [ItemEntity]がスポーンした場合は`true`，それ以外の場合は`false`
 */
fun dropStackAt(level: Level, pos: BlockPos, stack: ItemStack): Boolean =
    dropStackAt(level, pos.x.toDouble() + 0.5, pos.y.toDouble(), pos.z.toDouble() + 0.5, stack)

/**
 * 指定した[stack]を[pos]にドロップします。
 * @return [ItemEntity]がスポーンした場合は`true`，それ以外の場合は`false`
 */
fun dropStackAt(level: Level, pos: Vec3, stack: ItemStack): Boolean = dropStackAt(level, pos.x, pos.y, pos.z, stack)

/**
 * 指定した[stack]を[x]，[y]，[z]にドロップします。
 * @return [ItemEntity]がスポーンした場合は`true`，それ以外の場合は`false`
 */
fun dropStackAt(
    level: Level,
    x: Double,
    y: Double,
    z: Double,
    stack: ItemStack,
): Boolean {
    val itemEntity = ItemEntity(level, x, y, z, stack.copy())
    itemEntity.setPickUpDelay(0)
    return level.addFreshEntity(itemEntity)
}

//    BlockEntity    //

/**
 * [BlockEntity.hasLevel]がtrueの場合のみに[action]を実行します。
 * @param T 戻り値のクラス
 * @return [action]を実行した場合はその戻り値を，それ以外の場合は`null`
 */
fun <T : Any> BlockEntity.ifPresentWorld(action: (Level) -> T): T? = level?.let(action)

/**
 * [BlockEntity]に[CompoundTag]を使用したタグを同期させます。
 */
fun BlockEntity.sendUpdatePacket() {
    val serverLevel: ServerLevel = level.asServerLevel() ?: return
    val packet: Packet<ClientGamePacketListener> = this.updatePacket ?: return
    val chunk: ChunkAccess = serverLevel.getChunk(blockPos)
    serverLevel.chunkSource.chunkMap.getPlayers(chunk.pos, false).forEach { player: ServerPlayer ->
        player.connection.send(packet)
    }
}
