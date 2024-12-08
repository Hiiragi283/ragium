package hiiragi283.ragium.common.block.machine.consume

import hiiragi283.ragium.api.extension.getAroundPos
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTFluidSyncable
import hiiragi283.ragium.api.machine.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.api.storage.HTStorageBuilder
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.HTStorageSide
import hiiragi283.ragium.api.util.HTUnitResult
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.recipe.HTMachineRecipeProcessor
import hiiragi283.ragium.common.screen.HTSimpleMachineScreenHandler
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.FluidTags
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTRockGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntityBase(RagiumBlockEntityTypes.ROCK_GENERATOR, pos, state),
    HTFluidSyncable {
    override var key: HTMachineKey = RagiumMachineKeys.ROCK_GENERATOR

    constructor(pos: BlockPos, state: BlockState, tier: HTMachineTier) : this(pos, state) {
        this.tier = tier
    }

    val inventory: SidedInventory = HTStorageBuilder(5)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(1, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(2, HTStorageIO.INTERNAL, HTStorageSide.NONE)
        .set(3, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .set(4, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .buildInventory()

    val fluidStorage: HTMachineFluidStorage = HTStorageBuilder(2)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(1, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .buildMachineFluidStorage(tier)
        .setCallback(this@HTRockGeneratorBlockEntity::markDirty)

    val processor = HTMachineRecipeProcessor(
        inventory,
        intArrayOf(0, 1),
        intArrayOf(3, 4),
        2,
        fluidStorage,
        intArrayOf(0),
        intArrayOf(1),
    )

    override fun asInventory(): SidedInventory = inventory

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        fluidStorage.writeNbt(nbt, wrapperLookup)
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
        fluidStorage.readNbt(nbt, wrapperLookup, tier)
    }

    override fun onTierUpdated(oldTier: HTMachineTier, newTier: HTMachineTier) {
        fluidStorage.update(newTier)
    }

    override fun interactWithFluidStorage(player: PlayerEntity): Boolean = fluidStorage.interactByPlayer(player)

    override fun process(world: World, pos: BlockPos): HTUnitResult = when {
        pos
            .getAroundPos { posIn: BlockPos -> world.getFluidState(posIn).isIn(FluidTags.WATER) }
            .isEmpty() -> HTUnitResult.errorString { "Require one water source at least around Rock Generator!" }

        pos
            .getAroundPos { posIn: BlockPos -> world.getFluidState(posIn).isIn(FluidTags.LAVA) }
            .isEmpty() -> HTUnitResult.errorString { "Require one lava source at least around Rock Generator!" }

        else -> processor.process(world, key, tier)
    }

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTSimpleMachineScreenHandler(syncId, playerInventory, packet, createContext())

    //    HTFluidSyncable    //

    override fun sendPacket(player: ServerPlayerEntity, sender: (ServerPlayerEntity, Int, FluidVariant, Long) -> Unit) {
        fluidStorage.sendPacket(player, sender)
    }

    //    SidedStorageBlockEntity    //

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant> = fluidStorage.createWrapped()
}
