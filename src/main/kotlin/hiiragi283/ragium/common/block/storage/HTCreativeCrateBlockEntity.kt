package hiiragi283.ragium.common.block.storage

import hiiragi283.ragium.api.block.HTBlockEntityBase
import hiiragi283.ragium.api.data.HTNbtCodecs
import hiiragi283.ragium.api.extension.getStackInMainHand
import hiiragi283.ragium.api.storage.HTCreativeStorage
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTCreativeCrateBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntityBase(RagiumBlockEntityTypes.CREATIVE_CRATE, pos, state),
    SidedStorageBlockEntity {
    val itemStorage: HTCreativeStorage<ItemVariant> = HTCreativeStorage.ofItem()

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        HTNbtCodecs.ITEM_VARIANT.writeTo(nbt, itemStorage.resource)
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
        HTNbtCodecs.ITEM_VARIANT.readAndSet(nbt, itemStorage::setResource)
    }

    override fun onRightClicked(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult {
        val stack: ItemStack = player.getStackInMainHand()
        if (stack.isEmpty) {
            itemStorage.resource = ItemVariant.blank()
        } else {
            itemStorage.resource = ItemVariant.of(stack)
        }
        return ActionResult.success(world.isClient)
    }

    //    SidedStorageBlockEntity    //

    override fun getItemStorage(side: Direction?): Storage<ItemVariant>? = itemStorage
}
