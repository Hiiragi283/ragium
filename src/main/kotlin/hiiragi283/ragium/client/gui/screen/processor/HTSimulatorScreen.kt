package hiiragi283.ragium.client.gui.screen.processor

import hiiragi283.ragium.client.gui.component.base.HTBasicFluidWidget
import hiiragi283.ragium.common.block.entity.processor.HTSimulatorBlockEntity
import hiiragi283.ragium.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class HTSimulatorScreen(menu: HTBlockEntityContainerMenu<HTSimulatorBlockEntity>, inventory: Inventory, title: Component) :
    HTSingleFluidProcessorScreen<HTSimulatorBlockEntity>(menu, inventory, title) {
    @Suppress("DEPRECATION")
    override fun init() {
        super.init()
        createFakeSlot(
            {
                val level: Level = blockEntity.level ?: return@createFakeSlot ItemStack.EMPTY
                val pos: BlockPos = blockEntity.blockPos.below()

                val state: BlockState = level.getBlockState(pos)
                val stack: ItemStack = state.block.getCloneItemStack(level, pos, state)
                level.getBlockEntity(pos)?.saveToItem(stack, level.registryAccess())
                stack
            },
            HTSlotHelper.getSlotPosX(2),
            HTSlotHelper.getSlotPosY(2),
        )
    }

    override fun createFluidWidget(): HTBasicFluidWidget =
        createFluidSlot(blockEntity.outputTank, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(2))
}
