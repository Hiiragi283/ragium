package hiiragi283.ragium.common.block.entity.dynamo

import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.storage.item.HTFilteredItemHandler
import hiiragi283.ragium.api.storage.item.HTItemFilter
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.common.inventory.HTSingleItemMenu
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.energy.IEnergyStorage

class HTStirlingDynamoBlockEntity(pos: BlockPos, state: BlockState) :
    HTDynamoBlockEntity(RagiumBlockEntityTypes.STIRLING_DYNAMO, pos, state) {
    override val inventory: HTItemHandler = HTItemStackHandler(1, this::setChanged)
    override val energyUsage: Int = RagiumConfig.COMMON.basicMachineEnergyUsage.get()

    private var lastBurnTime = 0

    override fun serverTickPre(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        network: IEnergyStorage,
    ): TriState {
        // 初回だけ燃料を判定する
        val burnTime: Int = if (usedEnergy == 0) {
            val stack: ItemStack = inventory.getStackInSlot(0)
            val burnTime: Int = stack.getBurnTime(null)
            inventory.consumeStackInSlot(0, 1, false)
            burnTime
        } else {
            lastBurnTime
        }
        if (burnTime <= 0) return TriState.FALSE
        // 燃焼時間を確認する
        if (this.lastBurnTime != burnTime) {
            this.lastBurnTime = burnTime
            this.requiredEnergy = energyUsage * burnTime
        }

        // エネルギーを生産する
        if (usedEnergy < requiredEnergy) {
            usedEnergy += network.receiveEnergy(energyUsage, false)
        }
        if (usedEnergy < requiredEnergy) return TriState.DEFAULT
        usedEnergy -= requiredEnergy
        return TriState.TRUE
    }

    override fun getItemHandler(direction: Direction?): HTFilteredItemHandler = HTFilteredItemHandler(inventory, HTItemFilter.INSERT_ONLY)

    //    Menu    //

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? = HTSingleItemMenu(
        containerId,
        playerInventory,
        blockPos,
        createDefinition(inventory),
    )
}
