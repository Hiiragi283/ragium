package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTBlockEntity
import hiiragi283.ragium.api.capability.HTHandlerSerializer
import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.extension.modifyEnchantment
import hiiragi283.ragium.api.extension.toList
import hiiragi283.ragium.api.item.HTMachineItemHandler
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import it.unimi.dsi.fastutil.objects.Object2IntMap
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.network.chat.Component
import net.minecraft.resources.RegistryOps
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.EnchantedBookItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

class HTDisenchantingTableBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntity(RagiumBlockEntityTypes.DISENCHANTING_TABLE, pos, state),
    MenuProvider {
    private val ticketInput: HTMachineItemHandler = RagiumAPI.getInstance().createItemHandler(this::setChanged)
    private val toolInput: HTMachineItemHandler = RagiumAPI.getInstance().createItemHandler(this::setChanged)
    private val bookInput: HTMachineItemHandler = RagiumAPI.getInstance().createItemHandler(this::setChanged)

    private val serializer: HTHandlerSerializer = HTHandlerSerializer.ofItem(
        listOf(
            ticketInput.createSlot(0),
            toolInput.createSlot(0),
            bookInput.createSlot(0),
        ),
    )
    private var isActive: Boolean = false

    override fun writeNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        serializer.writeNbt(nbt, dynamicOps)
    }

    override fun readNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        serializer.readNbt(nbt, dynamicOps)
    }

    override fun neighborChanged(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        neighborBlock: Block,
        neighborPos: BlockPos,
        movedByPiston: Boolean,
    ) {
        val hasSignal: Boolean = level.hasNeighborSignal(pos)
        if (hasSignal != isActive) {
            if (hasSignal) {
                process(level, pos)
            }
            isActive = hasSignal
        }
    }

    private fun process(level: Level, pos: BlockPos) {
        // Check tool enchantment
        val tool: ItemStack = toolInput.getStackInSlot(0)
        val toolEnch: ItemEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(tool)
        if (toolEnch.isEmpty) return dropAll(level, pos)
        // Check ticket cost
        val requiredTickets: Int = toolEnch.entrySet().sumOf(Object2IntMap.Entry<Holder<Enchantment>>::getIntValue)
        if (ticketInput.getStackInSlot(0).count < requiredTickets) return dropAll(level, pos)
        // Check book count
        val holders: List<Holder<Enchantment>> = toolEnch.entrySet().map(Object2IntMap.Entry<Holder<Enchantment>>::key)
        val requiredBooks: Int = holders.size
        if (bookInput.getStackInSlot(0).count < requiredBooks) return dropAll(level, pos)
        // Transfer enchantments
        toolEnch.toList().map(EnchantedBookItem::createForEnchantment).forEach { stack: ItemStack ->
            dropStackAt(level, pos.above(), stack)
        }
        // Decrement tickets
        ticketInput.extractItem(0, requiredTickets, false)
        // Decrement books
        bookInput.extractItem(0, requiredBooks, false)
        // Remove enchantments from tool
        tool.modifyEnchantment { mutable: ItemEnchantments.Mutable -> null }
    }

    private fun dropAll(level: Level, pos: BlockPos) {
        serializer.dropItems(level, pos.above())
    }

    override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean,
    ) {
        serializer.dropItems(level, pos)
    }

    //    MenuProvider    //

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? = null

    override fun getDisplayName(): Component {
        TODO("Not yet implemented")
    }
}
