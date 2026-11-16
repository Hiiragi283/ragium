package hiiragi283.lib.block.entity

import hiiragi283.lib.HTPhysicalSideHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.RegistryAccess
import net.minecraft.world.attribute.EnvironmentAttribute
import net.minecraft.world.attribute.EnvironmentAttributes
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import kotlin.math.PI
import kotlin.math.cos

/**
 * [BlockEntity]に実装されるインターフェースです。
 *
 * 参考 : [Mekanism - ITileWrapper](https://github.com/mekanism/Mekanism/blob/26.3/src/main/java/mekanism/common/tile/interfaces/ITileWrapper.java)
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
    fun getRegistryAccess(): RegistryAccess? = getLevel()?.registryAccess() ?: HTPhysicalSideHelper.getRegistryAccess()

    /**
     * [座標][BlockPos]を取得します。
     */
    fun getBlockPos(): BlockPos

    /**
     * 環境の属性を取得します。
     * @return [getLevel]が`null`の場合は`null`
     * @since 26.1.14
     */
    fun <T : Any> getEnvironmentAttribute(attribute: EnvironmentAttribute<T>): T? =
        getLevel()?.environmentAttributes()?.getValue(attribute, getBlockPos())

    /**
     * 環境の属性を取得します。
     * @return [getLevel]が`null`の場合は[EnvironmentAttribute.defaultValue]の値
     * @since 26.1.14
     */
    fun <T : Any> getEnvironmentAttributeOrDefault(attribute: EnvironmentAttribute<T>): T =
        getEnvironmentAttribute(attribute) ?: attribute.defaultValue()

    /**
     * 太陽の角度を取得します。
     *
     * 参考 : [TechReborn - SolarPanelBlockEntity](https://github.com/TechReborn/TechReborn/blob/26.3/src/main/java/techreborn/blockentity/generator/SolarPanelBlockEntity.java)
     * @since 26.1.14
     * @return `0.0 .. 1.0`で制限された値
     */
    fun getSunAngle(): Double {
        val sunAngle: Double = getEnvironmentAttributeOrDefault(EnvironmentAttributes.SUN_ANGLE) * (PI / 180.0)
        return maxOf(0.0, cos(sunAngle))
    }
}
