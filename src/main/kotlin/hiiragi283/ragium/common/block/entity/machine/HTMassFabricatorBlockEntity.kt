package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.recipe.handler.HTProgressHandler
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.core.api.storage.HTStorageAccess
import hiiragi283.core.api.storage.HTStorageAction
import hiiragi283.core.support.recipe.handler.HTItemOutputHandler
import hiiragi283.core.support.storage.item.HTBasicItemSlot
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import hiiragi283.ragium.common.block.entity.machine.base.HTItemToItemBlockEntity
import hiiragi283.ragium.common.recipe.viewer.RagiumRecipeViewerTypes
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState

class HTMassFabricatorBlockEntity(pos: BlockPos, state: BlockState) : HTItemToItemBlockEntity(RagiumBlockEntityTypes.MASS_FABRICATOR.get(), pos, state) {
    override fun createInputSlot(listener: HTContentListener): HTBasicItemSlot = HTBasicItemSlot.input(
        listener,
        canInsert = { RagiumDataMapTypes.getMatterPoint(it) > 0 },
    )

    override fun getViewerTypes(): Iterable<HTRecipeViewerType<*>> = listOf(RagiumRecipeViewerTypes.MASS_FABRICATING)

    //    Processing    //

    private var storedPoints: Int = 0

    override fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // 入力スロットのアイテムを消費してポイントをためる
        if (!inputSlot.isEmpty()) {
            val stackIn: ItemStack = inputSlot.getStack()
            val pointIn: Int = RagiumDataMapTypes.getTotalMatterPoint(stackIn)
            if (pointIn > 0) {
                storedPoints += pointIn
                inputSlot.extract(stackIn.count, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
                return true
            } else {
                return false
            }
        }
        // ポイントが一定数以上なら電力を消費してマターを生産する
        return super.onUpdateMachine(level, pos, state)
    }

    private inner class ProgressHandlerImpl : HTProgressHandler<ItemStack>() {
        private val outputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(outputSlot) }

        override fun findRecipe(level: ServerLevel, pos: BlockPos): ItemStack = RagiumItems.RAGI_MATTER.toStack()

        override fun canComplete(level: ServerLevel, pos: BlockPos, recipe: ItemStack): Boolean = outputHandler.canInsert(recipe)

        override fun getMaxProgress(recipe: ItemStack): Int = updateAndGetProgress(20 * 60)

        override fun getProgress(level: ServerLevel, pos: BlockPos): Int = handler.consume()

        override fun onComplete(level: ServerLevel, pos: BlockPos, recipe: ItemStack) {
            // output
            outputHandler.insert(recipe)
            // input
            storedPoints -= RagiumConst.MAX_MATTER_POINT
            // sound
            playSound(SoundEvents.RESPAWN_ANCHOR_CHARGE)
        }
    }

    override fun createHandler(): HTProgressHandler<*> = ProgressHandlerImpl()

    override fun getConfig(): HTEnergyConfig = RagiumConfig.COMMON.machine.massFabricator
}
