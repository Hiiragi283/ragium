package hiiragi283.ragium.common.block.storage

import hiiragi283.ragium.api.block.HTBlockEntityBase
import hiiragi283.ragium.api.data.HTNbtCodecs
import hiiragi283.ragium.api.extension.interactWithFluidStorage
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineTierProvider
import hiiragi283.ragium.api.storage.HTFluidVariantStack
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumComponentTypes
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
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTDrumBlockEntity(pos: BlockPos, state: BlockState, override var tier: HTMachineTier = HTMachineTier.PRIMITIVE) :
    HTBlockEntityBase(RagiumBlockEntityTypes.DRUM, pos, state),
    SidedStorageBlockEntity,
    HTMachineTierProvider {
    private var fluidStorage: HTMachineFluidStorage = HTMachineFluidStorage
        .Builder(1)
        .generic(0)
        .build(this)

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        HTNbtCodecs.MACHINE_TIER.writeTo(nbt, tier)
        fluidStorage.writeNbt(nbt, wrapperLookup)
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
        HTNbtCodecs.MACHINE_TIER.readAndSet(nbt, this::tier)
        fluidStorage.readNbt(nbt, wrapperLookup, tier)
    }

    override fun readComponents(components: ComponentsAccess) {
        fluidStorage.map(0) { storageIn: SingleFluidStorage ->
            components.get(RagiumComponentTypes.DRUM)?.let { (variant: FluidVariant, amount: Long) ->
                storageIn.variant = variant
                storageIn.amount = amount
            }
        }
    }

    override fun addComponents(builder: ComponentMap.Builder) {
        val stack: HTFluidVariantStack = fluidStorage.getVariantStack(0)
        if (stack.isNotEmpty) {
            builder.add(RagiumComponentTypes.DRUM, stack)
        }
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult = when {
        fluidStorage.interactWithFluidStorage(player) -> ActionResult.success(world.isClient)
        else -> ActionResult.PASS
    }

    //    SidedStorageBlockEntity    //

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant> = fluidStorage
}
