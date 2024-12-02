package hiiragi283.ragium.common.block.storage

import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.common.block.entity.HTBlockEntityBase
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumComponentTypes
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
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTCrateBlockEntity(pos: BlockPos, state: BlockState, private var tier: HTMachineTier = HTMachineTier.PRIMITIVE) :
    HTBlockEntityBase(RagiumBlockEntityTypes.CRATE, pos, state),
    SidedStorageBlockEntity {
    private var itemStorage: SingleItemStorage = HTStorageIO.GENERIC.createItemStorage(tier.bucketUnit * 8)

    val previewStack: ItemStack = itemStorage.variant.toStack()

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        itemStorage.writeNbt(nbt, wrapperLookup)
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
        tier = nbt.getTier(TIER_KEY)
        itemStorage = HTStorageIO.GENERIC.createItemStorage(tier.bucketUnit * 8)
        itemStorage.readNbt(nbt, wrapperLookup)
    }

    override fun readComponents(components: ComponentsAccess) {
        components.get(RagiumComponentTypes.CRATE)?.let { (variant: ItemVariant, amount: Long) ->
            itemStorage.variant = variant
            itemStorage.amount = amount
        }
    }

    override fun addComponents(builder: ComponentMap.Builder) {
        builder.add(RagiumComponentTypes.CRATE, itemStorage.resourceAmount)
    }

    override fun onStateReplaced(
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
    }

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
                    val variantIn: ItemVariant = itemStorage.variant
                    val extracted: Long = itemStorage.extract(variantIn, 64, transaction)
                    if (extracted > 0) {
                        transaction.commit()
                        dropStackAt(player, variantIn.toStack(extracted.toInt()))
                        return ActionResult.success(world.isClient)
                    }
                }
            } else {
                val inserted: Long = itemStorage.insert(ItemVariant.of(stack), stack.count.toLong(), transaction)
                if (inserted > 0) {
                    transaction.commit()
                    stack.decrement(inserted.toInt())
                    return ActionResult.success(world.isClient)
                }
            }
        }
        return super.onUse(state, world, pos, player, hit)
    }

    //    SidedStorageBlockEntity    //

    override fun getItemStorage(side: Direction?): Storage<ItemVariant>? = itemStorage
}
