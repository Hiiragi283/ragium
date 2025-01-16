package hiiragi283.ragium.api.extension

import net.minecraft.core.BlockPos
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity

//    ContainerLevelAccess    //

/**
 * [ContainerLevelAccess]を返します。
 * @return [BlockEntity.hasLevel]がfalseの場合は[ContainerLevelAccess.NULL]
 */
fun BlockEntity?.createMenuAccess(): ContainerLevelAccess =
    this?.ifPresentWorld { ContainerLevelAccess.create(it, blockPos) } ?: ContainerLevelAccess.NULL

/**
 * [ContainerLevelAccess.evaluate]の戻り値を展開します。
 * @return この[ContainerLevelAccess]が空の場合はnull
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
fun <T : Any> ContainerLevelAccess.getOrNull(getter: (Level, BlockPos) -> T?): T? = evaluate(getter, null)

/**
 * この[ContainerLevelAccess]から[BlockEntity]を取得します。
 */
fun ContainerLevelAccess.getBlockEntity(): BlockEntity? = getOrNull(Level::getBlockEntity)
