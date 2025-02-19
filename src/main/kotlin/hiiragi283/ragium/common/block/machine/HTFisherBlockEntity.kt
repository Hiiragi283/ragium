package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.capability.HTHandlerSerializer
import hiiragi283.ragium.api.energy.HTMachineEnergyData
import hiiragi283.ragium.api.extension.moveNextOrDrop
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
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
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3

class HTFisherBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.FISHER, pos, state, HTMachineType.FISHER) {
    override val handlerSerializer: HTHandlerSerializer = HTHandlerSerializer.EMPTY

    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = HTMachineEnergyData.Consume.DEFAULT

    override fun process(level: ServerLevel, pos: BlockPos) {
        if (!level.getFluidState(pos.below()).`is`(FluidTags.WATER)) {
            throw HTMachineException.Custom(
                true,
                "Failed to find water source below!",
            )
        }
        // Apply enchantment
        val rodStack = ItemStack(Items.FISHING_ROD)
        EnchantmentHelper.setEnchantments(rodStack, enchantments)
        // Generate loots
        val luck: Int = getEnchantmentLevel(Enchantments.LUCK_OF_THE_SEA)
        val lootKey: ResourceKey<LootTable> = if ((luck / 3f) > level.random.nextFloat()) {
            BuiltInLootTables.FISHING_TREASURE
        } else {
            BuiltInLootTables.FISHING
        }
        val lootParams: LootParams = LootParams
            .Builder(level)
            .withParameter(LootContextParams.ORIGIN, pos.below().toVec3())
            .withParameter(LootContextParams.TOOL, rodStack)
            .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, owner)
            .withLuck(
                EnchantmentHelper.getFishingLuckBonus(level, rodStack, FakePlayerFactory.getMinecraft(level)).toFloat(),
            ).create(LootContextParamSets.FISHING)
        val lootTable: LootTable = level.server.reloadableRegistries().getLootTable(lootKey)
        val stacks: List<ItemStack> = lootTable.getRandomItems(lootParams)
        // Insert loots
        for (stackIn: ItemStack in stacks) {
            moveNextOrDrop(level, pos, stackIn)
        }
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? = null

    override fun interactWithFluidStorage(player: Player): Boolean = false
}
