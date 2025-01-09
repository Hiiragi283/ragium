package hiiragi283.ragium.api.multiblock

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.blockPosText
import hiiragi283.ragium.api.property.HTPropertyHolderBuilder
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

interface HTControllerHolder {
    companion object {
        @JvmField
        val LOOKUP: BlockApiLookup<HTControllerHolder, Void?> =
            BlockApiLookup.get(RagiumAPI.id("controller_holder"), HTControllerHolder::class.java, Void::class.java)

        init {
            LOOKUP.registerFallback { _: World, _: BlockPos, _: BlockState, blockEntity: BlockEntity?, _: Void? ->
                (blockEntity as? HTControllerHolder)
            }
        }
    }

    var showPreview: Boolean

    fun getMultiblockMap(): HTMultiblockMap.Relative?

    fun getController(): HTControllerDefinition?

    /**
     * からマルチブロックの判定結果を返します。
     * @param consumer エラーメッセージを受け取る対象
     */
    fun collectData(consumer: (Text) -> Unit = {}): HTMultiblockData {
        val controller: HTControllerDefinition = getController() ?: return HTMultiblockData.DEFAULT
        if (controller.world.isClient) return HTMultiblockData.DEFAULT
        val absoluteMap: HTMultiblockMap.Absolute =
            getMultiblockMap()?.convertAbsolute(controller) ?: return HTMultiblockData.DEFAULT
        if (absoluteMap.isEmpty()) return HTMultiblockData.DEFAULT
        val builder = HTPropertyHolderBuilder()
        for ((pos: BlockPos, component: HTMultiblockComponent) in absoluteMap.entries) {
            if (component.checkState(controller, pos)) {
                component.collectData(controller, pos, builder)
            } else {
                val blockName: Text = component.getBlockName(controller)
                consumer(
                    Text.translatable(RagiumTranslationKeys.MULTI_SHAPE_ERROR, blockName, blockPosText(pos)).formatted(Formatting.RED),
                )
                return HTMultiblockData.FALSE
            }
        }
        consumer(Text.translatable(RagiumTranslationKeys.MULTI_SHAPE_SUCCESS))
        return HTMultiblockData.of(builder)
    }

    fun processData(data: HTMultiblockData) {}
}
