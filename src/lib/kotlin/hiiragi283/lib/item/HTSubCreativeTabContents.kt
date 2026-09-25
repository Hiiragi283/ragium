package hiiragi283.lib.item

import hiiragi283.lib.registry.RegistryKey
import net.minecraft.core.Holder
import net.minecraft.world.flag.FeatureElement
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import java.util.stream.Stream

/**
 * クリエイティブタブに複数の[ItemStack]を追加するためのインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun interface HTSubCreativeTabContents {
    /**
     * 複数の[ItemStack]を追加します。
     * @param baseItem 対象のアイテム
     * @param parameters 登録時のコンテキスト
     * @param output [ItemStack]の登録先
     */
    fun addItems(
        baseItem: Holder<Item>,
        parameters: CreativeModeTab.ItemDisplayParameters,
        output: CreativeModeTab.Output
    )

    /**
     * @since 26.1.7
     */
    fun <T : FeatureElement> CreativeModeTab.ItemDisplayParameters.filteredElements(
        registryKey: RegistryKey<T>
    ): Stream<Holder.Reference<T>> = this.holders()
        .lookup(registryKey)
        .map { it.filterFeatures(this.enabledFeatures()) }
        .stream()
        .flatMap { it.listElements() }

    /**
     * デフォルトの[ItemStack]を追加するか判定します。
     */
    fun shouldAddDefault(): Boolean = true
}
