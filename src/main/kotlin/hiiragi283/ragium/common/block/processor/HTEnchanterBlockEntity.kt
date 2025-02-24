package hiiragi283.ragium.common.block.processor

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.capability.HTHandlerSerializer
import hiiragi283.ragium.api.capability.energy.HTMachineEnergyData
import hiiragi283.ragium.api.capability.item.HTMachineItemHandler
import hiiragi283.ragium.api.machine.HTMachineType
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.block.EnchantingTableBlock
import net.minecraft.world.level.block.state.BlockState

class HTEnchanterBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(TODO(), pos, state, HTMachineType.ARCANE_ENCHANTER) {
    private val itemInput: HTMachineItemHandler = RagiumAPI.getInstance().createItemHandler(3, this::setChanged)
    private val itemOutput: HTMachineItemHandler = RagiumAPI.getInstance().createItemHandler(this::setChanged)

    override val handlerSerializer: HTHandlerSerializer = HTHandlerSerializer.ofItem(
        listOf(
            itemInput.createSlot(0),
            itemInput.createSlot(1),
            itemInput.createSlot(2),
            itemOutput.createSlot(0),
        ),
    )

    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = HTMachineEnergyData.Consume.PRECISION

    override fun process(level: ServerLevel, pos: BlockPos) {
        val enchantPower: Double = EnchantingTableBlock.BOOKSHELF_OFFSETS
            .filter { posTo: BlockPos -> EnchantingTableBlock.isValidBookShelf(level, pos, posTo) }
            .map(pos::offset)
            .sumOf { posTo: BlockPos -> level.getBlockState(posTo).getEnchantPowerBonus(level, posTo).toDouble() }
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? = null

    override fun interactWithFluidStorage(player: Player): Boolean = false
}
