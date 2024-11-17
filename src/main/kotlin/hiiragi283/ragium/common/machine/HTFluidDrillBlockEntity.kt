package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.extension.fluidStorageOf
import hiiragi283.ragium.api.extension.insert
import hiiragi283.ragium.api.extension.useTransaction
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTConsumerBlockEntityBase
import hiiragi283.ragium.api.machine.block.HTFluidSyncable
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockBuilder
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockComponent
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockController
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.screen.HTFluidDrillScreenHandler
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorageUtil
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.base.FilteringStorage
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount
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
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import net.minecraft.world.biome.Biome

class HTFluidDrillBlockEntity(pos: BlockPos, state: BlockState) :
    HTConsumerBlockEntityBase(RagiumBlockEntityTypes.FLUID_DRILL, pos, state),
    HTFluidSyncable,
    HTMultiblockController {
    companion object {
        @JvmField
        val FLUID_MAP: Map<TagKey<Biome>, ResourceAmount<FluidVariant>> = mapOf(
            BiomeTags.IS_END to ResourceAmount(FluidVariant.of(RagiumFluids.NOBLE_GAS.value), FluidConstants.INGOT),
            BiomeTags.IS_NETHER to ResourceAmount(FluidVariant.of(RagiumFluids.CRUDE_OIL.value), FluidConstants.BUCKET),
            BiomeTags.IS_OCEAN to ResourceAmount(FluidVariant.of(RagiumFluids.SALT_WATER.value), FluidConstants.BUCKET),
            BiomeTags.IS_BEACH to ResourceAmount(FluidVariant.of(RagiumFluids.SALT_WATER.value), FluidConstants.BOTTLE),
        )
    }

    override var key: HTMachineKey = RagiumMachineKeys.FLUID_DRILL

    constructor(pos: BlockPos, state: BlockState, tier: HTMachineTier) : this(pos, state) {
        this.tier = tier
    }

    private var fluidStorage: SingleFluidStorage = fluidStorageOf(tier.tankCapacity)

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        fluidStorage.writeNbt(nbt, wrapperLookup)
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
        fluidStorage = fluidStorageOf(tier.tankCapacity)
        fluidStorage.readNbt(nbt, wrapperLookup)
    }

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTFluidDrillScreenHandler(syncId, playerInventory, packet, createContext())

    override fun consumeEnergy(world: World, pos: BlockPos): Boolean {
        if (!isValid(cachedState, world, pos)) return false
        useTransaction { transaction: Transaction ->
            val inserted: Long = fluidStorage.insert(findResource(world.getBiome(pos)), transaction)
            if (inserted > 0) {
                transaction.commit()
                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS)
                return true
            } else {
                transaction.abort()
                return false
            }
        }
    }

    private fun findResource(biome: RegistryEntry<Biome>): ResourceAmount<FluidVariant> {
        for ((tagKey: TagKey<Biome>, resource: ResourceAmount<FluidVariant>) in FLUID_MAP) {
            if (biome.isIn(tagKey)) {
                return resource
            }
        }
        return ResourceAmount(FluidVariant.of(RagiumFluids.AIR.value), FluidConstants.BUCKET)
    }

    override fun interactWithFluidStorage(player: PlayerEntity): Boolean =
        FluidStorageUtil.interactWithFluidStorage(FilteringStorage.extractOnlyOf(fluidStorage), player, Hand.MAIN_HAND)

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant>? = FilteringStorage.extractOnlyOf(fluidStorage)

    //    HTFluidSyncable    //

    override fun sendPacket(player: ServerPlayerEntity, sender: (ServerPlayerEntity, Int, FluidVariant, Long) -> Unit) {
        sender(player, 0, fluidStorage.resource, fluidStorage.amount)
    }

    //    HTMultiblockController    //

    override var showPreview: Boolean = false

    override fun buildMultiblock(builder: HTMultiblockBuilder) {
        builder
            .addLayer(
                -1..1,
                0,
                1..3,
                HTMultiblockComponent.Simple(tier.getHull()),
            ).addCross4(
                -1..1,
                1,
                1..3,
                HTMultiblockComponent.Simple(tier.getGrate()),
            ).addCross4(
                -1..1,
                2,
                1..3,
                HTMultiblockComponent.Simple(tier.getGrate()),
            )
        builder.add(
            0,
            3,
            2,
            HTMultiblockComponent.Simple(tier.getGrate()),
        )
        builder.add(
            0,
            4,
            2,
            HTMultiblockComponent.Simple(tier.getGrate()),
        )
    }
}
