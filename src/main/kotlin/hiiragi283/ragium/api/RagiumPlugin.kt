package hiiragi283.ragium.api

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.property.HTPropertyHolderBuilder
import java.util.function.BiConsumer
import java.util.function.Function

/**
 * Ragiumのプラグイン向けのインターフェース
 */
interface RagiumPlugin {
    /**
     * プラグインを読み込む優先度
     *
     * 値が小さいほど優先して読み込まれます。
     */
    val priority: Int

    /**
     * プラグインを読み込むかどうか判定します。
     */
    fun shouldLoad(): Boolean = true

    /**
     * [HTMachineKey]を登録します。
     * @param consumer [HTMachineKey]をそのタイプを渡すブロック
     */
    fun registerMachine(consumer: BiConsumer<HTMachineKey, HTMachineType>) {}

    /**
     * 機械のプロパティを設定します。
     */
    fun setupMachineProperties(helper: Function<HTMachineKey, HTPropertyHolderBuilder>) {}
}
