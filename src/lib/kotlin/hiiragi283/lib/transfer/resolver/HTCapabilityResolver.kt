package hiiragi283.lib.transfer.resolver

import net.minecraft.core.Direction

/**
 * 向きに応じたキャパビリティを取得するインターフェースです。
 *
 * 参照 : [Mekanism - ICapabilityResolver](https://github.com/mekanism/Mekanism/blob/26.3/src/main/java/mekanism/common/capabilities/resolver/ICapabilityResolver.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
interface HTCapabilityResolver {
    /**
     * 指定された引数からキャパビリティを取得します。
     * @param T キャパビリティのクラス
     * @param side アクセスする面
     * @return 見つからない場合は`null`
     */
    fun <T : Any> resolve(side: Direction?): T?
}
