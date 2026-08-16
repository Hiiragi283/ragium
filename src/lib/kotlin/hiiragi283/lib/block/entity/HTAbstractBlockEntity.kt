package hiiragi283.lib.block.entity

import hiiragi283.lib.HTPhysicalSideHelper
import hiiragi283.lib.util.HTTextResult
import hiiragi283.lib.util.flatMap
import hiiragi283.lib.util.flatMapLeft
import hiiragi283.lib.util.toTextResult
import net.minecraft.core.BlockPos
import net.minecraft.core.RegistryAccess
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity

/**
 * [BlockEntity]に実装されるインターフェースです。
 *
 * 参考 : [Mekanism - ITileWrapper](https://github.com/mekanism/Mekanism/blob/26.1/src/main/java/mekanism/common/tile/interfaces/ITileWrapper.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTAbstractBlockEntity {
    /**
     * レベルを取得します。
     * @return レベルがない場合は`null`
     */
    fun getLevel(): Level?

    /**
     * レベルを取得します。
     */
    fun getLevelResult(): HTTextResult<Level> = getLevel().toTextResult { "Block entity at ${getBlockPos()} is not bounded to level" }

    /**
     * サーバーレベルを取得します。
     */
    fun getServerLevel(): HTTextResult<ServerLevel> = getLevelResult().flatMap { level: Level -> (level as? ServerLevel).toTextResult { "Block entity at ${getBlockPos()} does not exist in server-side" } }

    /**
     * [レジストリへのアクセス][RegistryAccess]を取得します。
     */
    fun getRegistryAccess(): HTTextResult<RegistryAccess> = getLevelResult().map(Level::registryAccess).flatMapLeft { HTPhysicalSideHelper.getRegistryAccess() }

    /**
     * [座標][BlockPos]を取得します。
     */
    fun getBlockPos(): BlockPos
}
