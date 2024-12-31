package hiiragi283.ragium.common.block.storage

import hiiragi283.ragium.api.block.HTBlockEntityBase
import hiiragi283.ragium.api.data.HTNbtCodecs
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineTierProvider
import hiiragi283.ragium.api.storage.HTItemVariantStack
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.network.HTCratePreviewPayload
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.item.base.SingleItemStorage
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.block.BlockState
import net.minecraft.component.ComponentMap
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTCrateBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntityBase(RagiumBlockEntityTypes.CRATE, pos, state),
    SidedStorageBlockEntity,
    HTMachineTierProvider {
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

    override var tier: HTMachineTier = HTMachineTier.PRIMITIVE
        private set

    constructor(pos: BlockPos, state: BlockState, tier: HTMachineTier) : this(pos, state) {
        this.tier = tier
        this.itemStorage = ItemStorage(tier)
    }

    var itemStorage: SingleItemStorage = ItemStorage(tier)
        private set

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        HTNbtCodecs.MACHINE_TIER.writeTo(nbt, tier)
        itemStorage.writeNbt(nbt, wrapperLookup)
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
        HTNbtCodecs.MACHINE_TIER.readAndSet(nbt, this::tier)
        itemStorage = ItemStorage(tier)
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

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult {
        val stack: ItemStack = player.getStackInActiveHand()
        useTransaction { transaction: Transaction ->
            if (stack.isEmpty) {
                if (!itemStorage.isResourceBlank) {
                    val variant: ItemVariant = itemStorage.variant
                    val extracted: Long = itemStorage.extractSelf(64, transaction)
                    if (extracted > 0) {
                        dropStackAt(player, variant.toStack(extracted.toInt()))
                        transaction.commit()
                        return ActionResult.success(world.isClient)
                    }
                }
            } else {
                val inserted: Long = itemStorage.insert(ItemVariant.of(stack), stack.count.toLong(), transaction)
                if (inserted > 0) {
                    stack.decrement(inserted.toInt())
                    transaction.commit()
                    return ActionResult.success(world.isClient)
                }
            }
        }
        return super.onUse(state, world, pos, player, hit)
    }

    //    SidedStorageBlockEntity    //

    override fun getItemStorage(side: Direction?): Storage<ItemVariant>? = itemStorage
}
