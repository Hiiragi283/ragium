package hiiragi283.ragium.api.multiblock

import hiiragi283.ragium.api.extension.blockPosText
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponentMap
import net.minecraft.network.chat.Component

interface HTMultiblockController {
    /**
     * マルチブロックのプレビューを表示するかどうか判定します。
     */
    var showPreview: Boolean

    /**
     * マルチブロックのパターンを返します。
     */
    fun getMultiblockMap(): HTMultiblockMap.Relative?

    /**
     * マルチブロックのコントローラのデータを返します。
     */
    fun getDefinition(): HTControllerDefinition?

    /**
     * マルチブロックの判定結果を返します。
     * @param consumer エラーメッセージを受け取るブロック
     */
    fun collectData(consumer: (Component) -> Unit): HTMultiblockData {
        val controller: HTControllerDefinition = getDefinition() ?: return HTMultiblockData.DEFAULT
        if (controller.level.isClientSide) return HTMultiblockData.DEFAULT
        val absoluteMap: HTMultiblockMap.Absolute =
            getMultiblockMap()?.convertAbsolute(controller) ?: return HTMultiblockData.DEFAULT
        if (absoluteMap.isEmpty()) return HTMultiblockData.DEFAULT
        val builder: DataComponentMap.Builder = DataComponentMap.builder()
        for ((pos: BlockPos, component: HTMultiblockComponent) in absoluteMap.entries) {
            if (component.checkState(controller, pos)) {
                component.collectData(controller, pos, builder)
            } else {
                val blockName: Component = component.getBlockName(controller)
                consumer(
                    Component.translatable(RagiumTranslationKeys.MULTI_SHAPE_ERROR, blockName, blockPosText(pos)),
                )
                return HTMultiblockData.FALSE
            }
        }
        consumer(Component.translatable(RagiumTranslationKeys.MULTI_SHAPE_SUCCESS))
        return HTMultiblockData.of(builder)
    }

    /**
     * [collectData]で集めたデータを処理します。
     */
    fun processData(definition: HTControllerDefinition, data: HTMultiblockData) {}
}
