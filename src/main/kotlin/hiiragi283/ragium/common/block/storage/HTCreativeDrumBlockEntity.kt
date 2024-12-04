package hiiragi283.ragium.common.block.storage

import hiiragi283.ragium.api.storage.HTCreativeStorage
import hiiragi283.ragium.common.block.entity.HTBlockEntityBase
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity
import net.fabricmc.fabric.impl.transfer.VariantCodecs
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtOps
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

@Suppress("UnstableApiUsage")
class HTCreativeDrumBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntityBase(RagiumBlockEntityTypes.CREATIVE_DRUM, pos, state),
    SidedStorageBlockEntity {
    private val fluidStorage: HTCreativeStorage<FluidVariant> = HTCreativeStorage.ofFluid()

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        VariantCodecs.FLUID_CODEC
            .encodeStart(NbtOps.INSTANCE, fluidStorage.resource)
            .result()
            .ifPresent { nbt.put("variant", it) }
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
        VariantCodecs.FLUID_CODEC
            .parse(NbtOps.INSTANCE, nbt.get("variant"))
            .result()
            .ifPresent { fluidStorage.resource = it }
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult {
        val handStorage: Storage<FluidVariant> = ContainerItemContext
            .forPlayerInteraction(player, Hand.MAIN_HAND)
            .find(FluidStorage.ITEM) ?: return super.onUse(state, world, pos, player, hit)
        val firstView: StorageView<FluidVariant> = handStorage.nonEmptyViews().firstOrNull() ?: return ActionResult.PASS
        fluidStorage.resource = firstView.resource
        return ActionResult.success(world.isClient)
    }

    //    SidedStorageBlockEntity    //

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant>? = fluidStorage
}
