package hiiragi283.ragium.common.block.machine.consume

import hiiragi283.ragium.api.extension.insert
import hiiragi283.ragium.api.extension.readFluidStorage
import hiiragi283.ragium.api.extension.useTransaction
import hiiragi283.ragium.api.extension.writeFluidStorage
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTFluidSyncable
import hiiragi283.ragium.api.machine.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockBuilder
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockManager
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockPattern
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockPatternProvider
import hiiragi283.ragium.api.storage.HTFluidVariantStack
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.HTTieredFluidStorage
import hiiragi283.ragium.api.util.HTUnitResult
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.screen.HTSmallMachineScreenHandler
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.BiomeTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import net.minecraft.world.biome.Biome

class HTFluidDrillBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntityBase(RagiumBlockEntityTypes.FLUID_DRILL, pos, state),
    HTFluidSyncable,
    HTMultiblockPatternProvider {
    companion object {
        @JvmField
        val FLUID_MAP: Map<TagKey<Biome>, HTFluidVariantStack> = mapOf(
            BiomeTags.IS_END to HTFluidVariantStack(RagiumFluids.NOBLE_GAS.value, FluidConstants.INGOT),
            BiomeTags.IS_NETHER to HTFluidVariantStack(RagiumFluids.CRUDE_OIL.value, FluidConstants.BUCKET),
            BiomeTags.IS_OCEAN to HTFluidVariantStack(RagiumFluids.SALT_WATER.value, FluidConstants.BUCKET),
            BiomeTags.IS_BEACH to HTFluidVariantStack(RagiumFluids.SALT_WATER.value, FluidConstants.BOTTLE),
        )
    }

    override var key: HTMachineKey = RagiumMachineKeys.FLUID_DRILL

    private val settings = HTTieredFluidStorage.Settings(HTStorageIO.OUTPUT, null, this::markDirty, 1)
    private var fluidStorage = HTTieredFluidStorage(tier, settings)

    override fun onTierUpdated(oldTier: HTMachineTier, newTier: HTMachineTier) {
        fluidStorage = HTTieredFluidStorage(newTier, settings)
    }

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        nbt.writeFluidStorage(FLUID_KEY, fluidStorage, wrapperLookup)
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
        nbt.readFluidStorage(FLUID_KEY, fluidStorage, wrapperLookup)
    }

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTSmallMachineScreenHandler(syncId, playerInventory, createContext())

    override fun process(world: World, pos: BlockPos): HTUnitResult = multiblockManager
        .updateValidation(cachedState)
        .flatMap {
            useTransaction { transaction: Transaction ->
                val stack: HTFluidVariantStack = findResource(world.getBiome(pos))
                if (fluidStorage.insert(stack, transaction) == stack.amount) {
                    transaction.commit()
                    world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS)
                    HTUnitResult.success()
                } else {
                    HTUnitResult.errorString { "Failed to insert fluid into Fluid Drill!" }
                }
            }
        }

    private fun findResource(biome: RegistryEntry<Biome>): HTFluidVariantStack {
        for ((tagKey: TagKey<Biome>, stack: HTFluidVariantStack) in FLUID_MAP) {
            if (biome.isIn(tagKey)) {
                return stack
            }
        }
        return HTFluidVariantStack.EMPTY
    }

    override fun interactWithFluidStorage(player: PlayerEntity): Boolean = fluidStorage.interactWithFluidStorage(player)

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant>? = fluidStorage.wrapStorage()

    //    HTFluidSyncable    //

    override fun sendPacket(player: ServerPlayerEntity, handler: HTFluidSyncable.Handler) {
        fluidStorage.sendPacket(player, handler)
    }

    //    HTMultiblockPatternProvider    //

    override val multiblockManager: HTMultiblockManager = HTMultiblockManager(::getWorld, pos, this)

    override fun buildMultiblock(builder: HTMultiblockBuilder) {
        builder
            .addLayer(
                -1..1,
                0,
                1..3,
                HTMultiblockPattern.of(tier.getHull()),
            ).addCross4(
                -1..1,
                1,
                1..3,
                HTMultiblockPattern.of(tier.getGrate()),
            ).addCross4(
                -1..1,
                2,
                1..3,
                HTMultiblockPattern.of(tier.getGrate()),
            )
        builder.add(
            0,
            3,
            2,
            HTMultiblockPattern.of(tier.getGrate()),
        )
        builder.add(
            0,
            4,
            2,
            HTMultiblockPattern.of(tier.getGrate()),
        )
    }
}
