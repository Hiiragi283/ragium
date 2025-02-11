package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.capability.HTHandlerSerializer
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.energy.HTMachineEnergyData
import hiiragi283.ragium.api.extension.insertOrDrop
import hiiragi283.ragium.api.extension.modifyEnchantment
import hiiragi283.ragium.api.extension.toList
import hiiragi283.ragium.api.item.HTMachineItemHandler
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.common.init.RagiumMachineKeys
import it.unimi.dsi.fastutil.objects.Object2IntMap
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.EnchantedBookItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper

class HTDisenchanterBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(TODO(), pos, state, RagiumMachineKeys.DISENCHANTER) {
    private val ticketInput: HTMachineItemHandler = RagiumAPI.getInstance().createItemHandler(this::setChanged)
    private val toolInput: HTMachineItemHandler = RagiumAPI.getInstance().createItemHandler(this::setChanged)
    private val bookInput: HTMachineItemHandler = RagiumAPI.getInstance().createItemHandler(this::setChanged)
    private val bookOutput: HTMachineItemHandler = RagiumAPI.getInstance().createItemHandler(9, this::setChanged)

    override val handlerSerializer: HTHandlerSerializer = HTHandlerSerializer.ofItem(
        listOf(
            ticketInput.createSlot(0),
            toolInput.createSlot(0),
            bookInput.createSlot(0),
            *bookOutput.allSlots().toTypedArray(),
        ),
    )

    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = HTMachineEnergyData.Consume.PRECISION

    override fun process(level: ServerLevel, pos: BlockPos) {
        // Check tool enchantment
        val tool: ItemStack = toolInput.getStackInSlot(0)
        val toolEnch: ItemEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(tool)
        if (toolEnch.isEmpty) throw HTMachineException.Custom(false, "Target item has no enchantments!")
        // Check ticket cost
        val requiredTickets: Int = toolEnch.entrySet().sumOf(Object2IntMap.Entry<Holder<Enchantment>>::getIntValue)
        if (ticketInput.getStackInSlot(0).count < requiredTickets) {
            throw HTMachineException.Custom(false, "Required tickets is $requiredTickets!")
        }
        // Check book count
        val holders: List<Holder<Enchantment>> = toolEnch.entrySet().map(Object2IntMap.Entry<Holder<Enchantment>>::key)
        val requiredBooks: Int = holders.size
        if (bookInput.getStackInSlot(0).count < requiredBooks) {
            throw HTMachineException.Custom(false, "Required books is $requiredBooks!")
        }
        // Transfer enchantments
        toolEnch.toList().map(EnchantedBookItem::createForEnchantment).forEach { stack: ItemStack ->
            bookOutput.insertOrDrop(level, pos.above(), stack)
        }
        // Decrement tickets
        ticketInput.extractItem(0, requiredTickets, false)
        // Decrement books
        bookInput.extractItem(0, requiredBooks, false)
        // Remove enchantments from tool
        tool.modifyEnchantment { mutable: ItemEnchantments.Mutable -> null }
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? = null

    override fun interactWithFluidStorage(player: Player): Boolean = false

    override fun getItemHandler(direction: Direction?): CombinedInvWrapper = CombinedInvWrapper(
        HTStorageIO.INPUT.wrapItemHandler(ticketInput),
        HTStorageIO.INPUT.wrapItemHandler(bookInput),
        HTStorageIO.OUTPUT.wrapItemHandler(bookOutput),
    )
}
