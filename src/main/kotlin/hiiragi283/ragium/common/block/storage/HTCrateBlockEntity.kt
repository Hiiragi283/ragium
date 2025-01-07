package hiiragi283.ragium.common.block.storage

import hiiragi283.ragium.api.block.entity.HTCrateBlockEntityBase
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineTierProvider
import hiiragi283.ragium.api.storage.HTItemVariantStack
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.network.HTCratePreviewPayload
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.item.base.SingleItemStorage
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.block.BlockState
import net.minecraft.component.ComponentMap
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.MiningToolItem
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTCrateBlockEntity(pos: BlockPos, state: BlockState, override val tier: HTMachineTier) :
    HTCrateBlockEntityBase(RagiumBlockEntityTypes.CRATE, pos, state),
    HTMachineTierProvider {
    constructor(pos: BlockPos, state: BlockState) : this(pos, state, state.tier)

    private inner class ItemStorage(val tier: HTMachineTier) : SingleItemStorage() {
        override fun onFinalCommit() {
            ifPresentWorld { world: World ->
                if (!world.isClient) {
                    sendPacket(HTCratePreviewPayload(pos, variantStack))
                }
            }
        }

        override fun getCapacity(variant: ItemVariant): Long = tier.crateCapacity
    }

    override val itemStorage: SingleItemStorage = ItemStorage(tier)
    override val showCount: Boolean
        get() = !itemStorage.isEmpty

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        itemStorage.writeNbt(nbt, wrapperLookup)
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
        itemStorage.readNbt(nbt, wrapperLookup)
    }

    override fun readComponents(components: ComponentsAccess) {
        components.get(RagiumComponentTypes.CRATE)?.let { (variant: ItemVariant, amount: Long) ->
            itemStorage.variant = variant
            itemStorage.amount = amount
        }
    }

    override fun addComponents(builder: ComponentMap.Builder) {
        val stack: HTItemVariantStack = itemStorage.variantStack
        if (stack.isNotEmpty) {
            builder.add(RagiumComponentTypes.CRATE, stack)
        }
    }

    /*override fun onStateReplaced(
        state: BlockState,
        world: World,
        pos: BlockPos,
        newState: BlockState,
        moved: Boolean,
    ) {
        ItemScatterer.spawn(
            world,
            pos.x.toDouble(),
            pos.y.toDouble(),
            pos.z.toDouble(),
            itemStorage.variant.toStack(itemStorage.amount.toInt()),
        )
        super.onStateReplaced(state, world, pos, newState, moved)
    }*/

    override fun onRightClicked(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult {
        val stack: ItemStack = player.getStackInMainHand()
        if (!stack.isEmpty) {
            val copied: ItemStack = stack.copy()
            // insert holding stack
            insertStack(stack)
            // insert from inventory
            for ((_: Int, stackIn: ItemStack) in player.inventory.asMap()) {
                if (itemStorage.isFilledMax) break
                if (ItemStack.areItemsAndComponentsEqual(stackIn, copied)) {
                    insertStack(stackIn)
                }
            }
            return ActionResult.success(world.isClient)
        }
        return super.onRightClicked(state, world, pos, player, hit)
    }

    private fun insertStack(stack: ItemStack) {
        useTransaction { transaction: Transaction ->
            val inserted: Long = itemStorage.insert(ItemVariant.of(stack), stack.count.toLong(), transaction)
            if (inserted > 0) {
                stack.decrement(inserted.toInt())
                transaction.commit()
            }
        }
    }

    override fun onLeftClicked(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
    ) {
        if (itemStorage.isEmpty) return
        val stack: ItemStack = player.getStackInMainHand()
        if (stack.item is MiningToolItem) return
        if (player.isSneaking) {
            // drop 64 items
            extractStack(player, 64)
        } else {
            // drop 1 item
            extractStack(player, 1)
        }
    }

    private fun extractStack(player: PlayerEntity, maxAmount: Long) {
        useTransaction { transaction: Transaction ->
            val variantIn: ItemVariant = itemStorage.variant
            val extracted: Long = itemStorage.extractSelf(maxAmount, transaction)
            if (extracted > 0) {
                dropStackAt(player, variantIn.toStack(extracted.toInt()))
                transaction.commit()
            }
        }
    }

    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos): Int =
        StorageUtil.calculateComparatorOutput(itemStorage)
}
