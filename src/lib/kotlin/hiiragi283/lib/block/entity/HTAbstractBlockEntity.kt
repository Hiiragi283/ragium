package hiiragi283.lib.block.entity

import hiiragi283.lib.HTPhysicalSideHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.RegistryAccess
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
     * [レジストリへのアクセス][RegistryAccess]を取得します。
     */
    fun getRegistryAccess(): RegistryAccess = getLevel()?.registryAccess() ?: HTPhysicalSideHelper.getRegistryAccess()

    /**
     * [座標][BlockPos]を取得します。
     */
    fun getBlockPos(): BlockPos
}
