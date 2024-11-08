package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.extension.modifyStack
import hiiragi283.ragium.api.extension.toStorage
import hiiragi283.ragium.api.extension.useTransaction
import hiiragi283.ragium.api.inventory.*
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTFluidSyncable
import hiiragi283.ragium.api.machine.block.HTGeneratorBlockEntityBase
import hiiragi283.ragium.api.recipe.HTItemResult
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.screen.HTSteamGeneratorScreenHandler
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorageUtil
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.base.FilteringStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.ItemTags
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTSteamGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTGeneratorBlockEntityBase(RagiumBlockEntityTypes.STEAM_GENERATOR, pos, state),
    HTDelegatedInventory.Sided,
    HTFluidSyncable {
    /*companion object {
        @JvmField
        val HEAT_LOOKUP: BlockApiLookup<Long, Direction?> = BlockApiLookup.get(
            RagiumAPI.id("heat_sided"),
            Long::class.java,
            Direction::class.java,
        )

        @JvmStatic
        fun getHeatPower(world: World, pos: BlockPos, direction: Direction?): Long = max(0, HEAT_LOOKUP.find(world, pos, direction) ?: 0)

        @JvmStatic
        fun registerHeat(power: Long, vararg blocks: Block) {
            HEAT_LOOKUP.registerForBlocks({ _: World, _: BlockPos, _: BlockState, _: BlockEntity?, direction: Direction? ->
                if (direction == Direction.UP) power else 0
            }, *blocks)
        }

        init {
            registerHeat(100, Blocks.FIRE)
            registerHeat(100, Blocks.SOUL_FIRE)
            registerHeat(100, Blocks.CAMPFIRE)
            registerHeat(100, Blocks.SOUL_CAMPFIRE)
            registerHeat(100, Blocks.LAVA)
            registerHeat(100, Blocks.MAGMA_BLOCK)

            HEAT_LOOKUP.registerForBlockEntity({ blockEntity: HTFireboxBlockEntity, direction: Direction? ->
                if (direction == Direction.UP) {
                    if (blockEntity.isBurning) 100 else 0
                } else {
                    0
                }
            }, RagiumBlockEntityTypes.FIREBOX)
        }
    }*/

    override var key: HTMachineKey = RagiumMachineKeys.STEAM_GENERATOR

    constructor(pos: BlockPos, state: BlockState, tier: HTMachineTier) : this(pos, state) {
        this.tier = tier
    }

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        fluidStorage.writeNbt(nbt, wrapperLookup)
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
        fluidStorage.readNbt(nbt, wrapperLookup)
    }

    override fun interactWithFluidStorage(player: PlayerEntity): Boolean =
        FluidStorageUtil.interactWithFluidStorage(fluidStorage, player, Hand.MAIN_HAND)

    override fun generateEnergy(world: World, pos: BlockPos): Long {
        val fuelStack: ItemStack = getStack(0)
        if (fuelStack.isIn(ItemTags.COALS)) {
            useTransaction { transaction: Transaction ->
                val extracted: Long =
                    fluidStorage.extract(FluidVariant.of(Fluids.WATER), FluidConstants.BUCKET, transaction)
                if (extracted == FluidConstants.BUCKET) {
                    transaction.commit()
                    fuelStack.decrement(1)
                    modifyStack(1, HTItemResult(RagiumContents.Dusts.ASH)::merge)
                    return tier.recipeCost
                } else {
                    transaction.abort()
                }
            }
        }
        return 0
    }

    override fun onSucceeded(world: World, pos: BlockPos) {
        world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS)
    }

    //    SidedStorageBlockEntity    //

    override val parent: HTSidedInventory = HTStorageBuilder(2)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(1, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .buildSided()

    override fun markDirty() {
        super<HTGeneratorBlockEntityBase>.markDirty()
    }

    private val fluidStorage: SingleFluidStorage = object : SingleFluidStorage() {
        override fun getCapacity(variant: FluidVariant): Long = tier.tankCapacity

        override fun canInsert(variant: FluidVariant): Boolean = variant == FluidVariant.of(Fluids.WATER)
    }

    override fun getItemStorage(side: Direction?): Storage<ItemVariant> = parent.toStorage(side)

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant> = FilteringStorage.insertOnlyOf(fluidStorage)

    //    HTFluidSyncable    //

    override fun sendPacket(player: ServerPlayerEntity, sender: (ServerPlayerEntity, Int, FluidVariant, Long) -> Unit) {
        sender(player, 0, fluidStorage.variant, fluidStorage.amount)
    }

    //    ExtendedScreenHandlerFactory    //

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler? =
        HTSteamGeneratorScreenHandler(
            syncId,
            playerInventory,
            packet,
            createContext(),
        )
}
