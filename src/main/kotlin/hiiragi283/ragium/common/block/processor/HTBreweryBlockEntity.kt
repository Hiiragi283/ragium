package hiiragi283.ragium.common.block.processor

import hiiragi283.ragium.api.machine.HTMachineEnergyData
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTRecipeTypes
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.inventory.HTBreweryContainerMenu
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.block.state.BlockState

class HTBreweryBlockEntity(pos: BlockPos, state: BlockState) :
    HTMultiItemMachineBlockEntity(
        RagiumBlockEntityTypes.BREWERY,
        pos,
        state,
        HTMachineType.ALCHEMICAL_BREWERY,
        HTRecipeTypes.BREWERY,
    ) {
    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = HTMachineEnergyData.Consume.PRECISION

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? = HTBreweryContainerMenu(
        containerId,
        playerInventory,
        blockPos,
        firstInputSlot,
        secondInputSlot,
        thirdInputSlot,
        outputSlot,
    )

    override fun getDisplayName(): Component = machineType.text.withStyle(ChatFormatting.WHITE)
}
