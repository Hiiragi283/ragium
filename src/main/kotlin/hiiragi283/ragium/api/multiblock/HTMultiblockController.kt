package hiiragi283.ragium.api.multiblock

import hiiragi283.ragium.api.extension.blockPosText
import hiiragi283.ragium.api.extension.emptyConsumer2
import hiiragi283.ragium.api.property.HTPropertyHolderBuilder
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.core.BlockPos
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
     * マルチブロックのコントローラを返します。
     */
    fun getController(): HTControllerDefinition?

    /**
     * マルチブロックの判定結果を返します。
     * @param consumer エラーメッセージを受け取るブロック
     */
    fun collectData(consumer: (Component) -> Unit = emptyConsumer2()): HTMultiblockData {
        val controller: HTControllerDefinition = getController() ?: return HTMultiblockData.DEFAULT
        if (controller.level.isClientSide) return HTMultiblockData.DEFAULT
        val absoluteMap: HTMultiblockMap.Absolute =
            getMultiblockMap()?.convertAbsolute(controller) ?: return HTMultiblockData.DEFAULT
        if (absoluteMap.isEmpty()) return HTMultiblockData.DEFAULT
        val builder = HTPropertyHolderBuilder()
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
        return HTMultiblockData.of(builder.build())
    }

    /**
     * [collectData]で集めたデータを処理します。
     */
    fun processData(data: HTMultiblockData) {}
}
