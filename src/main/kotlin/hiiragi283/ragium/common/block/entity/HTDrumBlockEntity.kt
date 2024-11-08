package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.extension.fluidStorageOf
import hiiragi283.ragium.api.extension.getTier
import hiiragi283.ragium.api.extension.putTier
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorageUtil
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.component.ComponentMap
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTDrumBlockEntity(pos: BlockPos, state: BlockState, private var tier: HTMachineTier = HTMachineTier.PRIMITIVE) :
    HTBlockEntityBase(RagiumBlockEntityTypes.DRUM, pos, state),
    SidedStorageBlockEntity {
    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        nbt.putTier(TIER_KEY, tier)
        fluidStorage.writeNbt(nbt, wrapperLookup)
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
        tier = nbt.getTier(TIER_KEY)
        fluidStorage = fluidStorageOf(tier.tankCapacity)
        fluidStorage.readNbt(nbt, wrapperLookup)
    }

    override fun readComponents(components: ComponentsAccess) {
        components.get(RagiumComponentTypes.DRUM)?.let { fluidStorage = it }
    }

    override fun addComponents(builder: ComponentMap.Builder) {
        builder.add(RagiumComponentTypes.DRUM, fluidStorage)
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult = when {
        FluidStorageUtil.interactWithFluidStorage(fluidStorage, player, Hand.MAIN_HAND) ->
            ActionResult.success(world.isClient)

        else -> ActionResult.PASS
    }

    //    SidedStorageBlockEntity    //

    private var fluidStorage: SingleFluidStorage = fluidStorageOf(tier.tankCapacity)

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant> = fluidStorage
}
