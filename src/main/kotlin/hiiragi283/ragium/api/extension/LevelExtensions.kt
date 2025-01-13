package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.block.entity.HTBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.CommonLevelAccessor
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

//    BlockGetter    //

fun BlockGetter.getHTBlockEntity(pos: BlockPos): HTBlockEntity? = getBlockEntity(pos) as? HTBlockEntity

/**
 * 指定した[pos]に存在する[BlockState]を置き換えようとします。
 * @param doBreak trueの場合は[CommonLevelAccessor.destroyBlock]，それ以外の場合は[CommonLevelAccessor.setBlock]
 * @return サーバー側で置換に成功したらtrue，それ以外の場合はfalse
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

//    BlockEntity    //

/**
 * [BlockEntity.hasLevel]がtrueの場合のみに[action]を実行します。
 * @param T 戻り値のクラス
 * @return [action]を実行した場合はその戻り値を，それ以外の場合はnull
 */
fun <T : Any> BlockEntity.ifPresentWorld(action: (Level) -> T): T? = level?.let(action)
