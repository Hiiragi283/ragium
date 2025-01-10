package hiiragi283.ragium.common.block.machine.consume

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntityBase
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.screen.HTScreenFluidProvider
import hiiragi283.ragium.api.storage.HTFluidVariantStack
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.HTTieredFluidStorage
import hiiragi283.ragium.api.util.HTMachineException
import hiiragi283.ragium.common.screen.HTSmallMachineScreenHandler
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.TagKey
import net.minecraft.screen.ScreenHandler
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import net.minecraft.world.biome.Biome

abstract class HTResourceDrillBlockEntityBase(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntityBase(type, pos, state),
    HTScreenFluidProvider {
    abstract val fluidMap: Map<TagKey<Biome>, HTFluidVariantStack>

    private var fluidStorage = HTTieredFluidStorage(tier, HTStorageIO.OUTPUT, null, this::markDirty, 1)

    final override fun onTierUpdated(oldTier: HTMachineTier, newTier: HTMachineTier) {
        fluidStorage = fluidStorage.updateTier(newTier)
    }

    final override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        nbt.writeFluidStorage(FLUID_KEY, fluidStorage, wrapperLookup)
    }

    final override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
        nbt.readFluidStorage(FLUID_KEY, fluidStorage, wrapperLookup)
    }

    final override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTSmallMachineScreenHandler(syncId, playerInventory, createContext())

    final override fun process(world: World, pos: BlockPos) {
        checkMultiblockOrThrow()
        useTransaction { transaction: Transaction ->
            val stack: HTFluidVariantStack = findResource(world.getBiome(pos))
            if (stack.isEmpty) {
                throw HTMachineException.InsertFluid(false)
            }
            if (fluidStorage.insert(stack, transaction) == stack.amount) {
                transaction.commit()
                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS)
            } else {
                throw HTMachineException.InsertFluid(false)
            }
        }
    }

    private fun findResource(biome: RegistryEntry<Biome>): HTFluidVariantStack {
        for ((tagKey: TagKey<Biome>, stack: HTFluidVariantStack) in fluidMap) {
            if (biome.isIn(tagKey)) {
                return stack
            }
        }
        return HTFluidVariantStack.EMPTY
    }

    final override fun interactWithFluidStorage(player: PlayerEntity): Boolean = fluidStorage.interactWithFluidStorage(player)

    final override fun getFluidStorage(side: Direction?): Storage<FluidVariant>? = fluidStorage.wrapStorage()

    //    HTScreenFluidProvider    //

    final override fun getFluidsToSync(): Map<Int, HTFluidVariantStack> = fluidStorage.getFluidsToSync()
}
