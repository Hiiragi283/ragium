package hiiragi283.ragium.api.block.entity

import hiiragi283.ragium.api.extension.dropStacks
import hiiragi283.ragium.api.fluid.HTMachineFluidTank
import hiiragi283.ragium.api.recipe.HTMachineRecipeBase
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.FluidUtil
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemStackHandler
import java.util.function.Supplier

/**
 * [HTMachineRecipeBase]を処理する[HTMachineBlockEntity]
 */
abstract class HTRecipeProcessorBlockEntity(type: Supplier<out BlockEntityType<*>>, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(type, pos, state) {
    protected abstract val itemHandler: ItemStackHandler

    protected abstract val tanks: Array<out HTMachineFluidTank>

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        tag.put(ITEM_KEY, itemHandler.serializeNBT(registries))
        HTMachineFluidTank.writeToNBT(tanks, tag, registries)
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        itemHandler.deserializeNBT(registries, tag.getCompound(ITEM_KEY))
        HTMachineFluidTank.readFromNBT(tanks, tag, registries)
    }

    override fun updateEnchantments(newEnchantments: ItemEnchantments) {
        super.updateEnchantments(newEnchantments)
        for (tank: HTMachineFluidTank in tanks) {
            tank.capacity = getEnchantmentLevel(Enchantments.UNBREAKING) * 8 * FluidType.BUCKET_VOLUME
        }
    }

    final override fun process(level: ServerLevel, pos: BlockPos) {
        checkMultiblockOrThrow()
    }

    final override fun interactWithFluidStorage(player: Player): Boolean {
        val heldItem: ItemStack = player.getItemInHand(InteractionHand.MAIN_HAND)
        if (heldItem.isEmpty) return false
        val playerInv: IItemHandler = player.getCapability(Capabilities.ItemHandler.ENTITY) ?: return false
        // try to extract
        for (tank: HTMachineFluidTank in tanks) {
            if (FluidUtil.tryEmptyContainerAndStow(heldItem, tank, playerInv, Int.MAX_VALUE, player, true).isSuccess) return true
        }
        // try to insert
        for (tank: HTMachineFluidTank in tanks.reversed()) {
            if (FluidUtil.tryFillContainerAndStow(heldItem, tank, playerInv, Int.MAX_VALUE, player, true).isSuccess) return true
        }
        return false
    }

    override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean,
    ) {
        super.onRemove(state, level, pos, newState, movedByPiston)
        itemHandler.dropStacks(level, pos)
    }
}
