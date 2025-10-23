package hiiragi283.ragium.common.block.entity.machine

import com.mojang.authlib.GameProfile
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.getItemStack
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.common.storage.holder.HTSimpleItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.variant.HTMachineVariant
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.GameType
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.neoforged.neoforge.common.CommonHooks
import net.neoforged.neoforge.common.util.FakePlayer
import net.neoforged.neoforge.common.util.FakePlayerFactory
import net.neoforged.neoforge.event.EventHooks

class HTBlockBreakerBlockEntity(pos: BlockPos, state: BlockState) : HTConsumerBlockEntity(HTMachineVariant.BLOCK_BREAKER, pos, state) {
    lateinit var toolSlot: HTItemSlot

    override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder {
        toolSlot = HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(1))
        return HTSimpleItemSlotHolder(this, listOf(toolSlot), listOf())
    }

    override fun openGui(player: Player, title: Component): InteractionResult =
        RagiumMenuTypes.SINGLE_ITEM.openMenu(player, title, this, ::writeExtraContainerData)

    override fun onUpdateServer(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        network: HTEnergyBattery,
    ): Boolean {
        // 採掘用のFake Playerを用意する
        val player: FakePlayer = FakePlayerFactory.get(level, GameProfile(getOwner(), getOwnerName()))
        val inventory: Inventory = player.inventory
        val toolStack: ItemStack = toolSlot.getItemStack()
        inventory.items[inventory.selected] = toolStack
        // 採掘対象のブロックを取得する
        val front: Direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING)
        val posTo: BlockPos = pos.relative(front)
        val stateTo: BlockState = level.getBlockState(posTo)
        // 採掘速度が0未満の場合はスキップ
        if (stateTo.getDestroySpeed(level, posTo) < 0) {
            return false
        }
        // 採掘できない場合はスキップ
        if (!stateTo.canHarvestBlock(level, posTo, player)) {
            return false
        }
        // イベントがキャンセルされた場合はスキップ
        if (CommonHooks.fireBlockBreak(level, GameType.SURVIVAL, player, posTo, stateTo).isCanceled) {
            return false
        }
        // エネルギーを消費する
        usedEnergy += network.extractEnergy(energyUsage, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        if (usedEnergy < getModifiedEnergy(energyUsage * 20)) return false
        usedEnergy = 0
        // ブロックを採掘する
        val blockTo: Block = stateTo.block
        val newStateTo: BlockState = blockTo.playerWillDestroy(level, posTo, stateTo, player)
        val toolStack1: ItemStack = toolStack.copy()
        val canHarvest: Boolean = newStateTo.canHarvestBlock(level, posTo, player)
        toolStack.mineBlock(level, newStateTo, posTo, player)
        val removed: Boolean =
            newStateTo.onDestroyedByPlayer(level, posTo, player, canHarvest, level.getFluidState(posTo))
        if (removed) {
            newStateTo.block.destroy(level, posTo, newStateTo)
        }
        if (canHarvest && removed) {
            blockTo.playerDestroy(level, player, posTo, newStateTo, level.getBlockEntity(posTo), toolStack1)
        }
        if (toolStack.isEmpty && !toolStack1.isEmpty) {
            EventHooks.onPlayerDestroyItem(player, toolStack1, InteractionHand.MAIN_HAND)
        }
        return false
    }
}
