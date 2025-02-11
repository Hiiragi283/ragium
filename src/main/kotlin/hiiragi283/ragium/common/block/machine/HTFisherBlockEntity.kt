package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.capability.HTHandlerSerializer
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.energy.HTMachineEnergyData
import hiiragi283.ragium.api.extension.slotRange
import hiiragi283.ragium.api.item.HTMachineItemHandler
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.FluidTags
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.loot.BuiltInLootTables
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.neoforged.neoforge.common.util.FakePlayerFactory
import net.neoforged.neoforge.items.IItemHandlerModifiable
import net.neoforged.neoforge.items.ItemHandlerHelper
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3

class HTFisherBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.FISHER, pos, state, RagiumMachineKeys.FISHER) {
    private val itemOutput: HTMachineItemHandler = RagiumAPI.getInstance().createItemHandler(9, this::setChanged)

    override val handlerSerializer: HTHandlerSerializer =
        HTHandlerSerializer.ofItem(itemOutput.slotRange.map(itemOutput::createSlot))

    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData =
        HTMachineEnergyData.Consume.DEFAULT

    override fun process(level: ServerLevel, pos: BlockPos) {
        if (!level.getFluidState(pos.below()).`is`(FluidTags.WATER)) throw HTMachineException.FindFluid(false)
        // Apply enchantment
        val stack = ItemStack(Items.FISHING_ROD)
        EnchantmentHelper.setEnchantments(stack, enchantments)
        // Generate loots
        val luck: Int = getEnchantmentLevel(Enchantments.LUCK_OF_THE_SEA)
        val lootKey: ResourceKey<LootTable> = if ((luck / 3f) > level.random.nextFloat()) {
            BuiltInLootTables.FISHING_TREASURE
        } else BuiltInLootTables.FISHING
        val lootParams: LootParams = LootParams
            .Builder(level)
            .withParameter(LootContextParams.ORIGIN, pos.below().toVec3())
            .withParameter(LootContextParams.TOOL, stack)
            .withLuck(
                EnchantmentHelper.getFishingLuckBonus(level, stack, FakePlayerFactory.getMinecraft(level)).toFloat()
            )
            .create(LootContextParamSets.FISHING)
        val lootTable: LootTable = level.server.reloadableRegistries().getLootTable(lootKey)
        val stacks: List<ItemStack> = lootTable.getRandomItems(lootParams)
        // Insert loots
        for (stack: ItemStack in stacks) {
            ItemHandlerHelper.insertItem(itemOutput, stack, false)
        }
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? = null

    override fun interactWithFluidStorage(player: Player): Boolean = false

    override fun getItemHandler(direction: Direction?): IItemHandlerModifiable =
        HTStorageIO.OUTPUT.wrapItemHandler(itemOutput)
}
