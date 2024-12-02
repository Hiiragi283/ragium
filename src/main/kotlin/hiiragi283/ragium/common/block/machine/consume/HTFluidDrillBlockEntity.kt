package hiiragi283.ragium.common.block.machine.consume

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.extension.insert
import hiiragi283.ragium.api.extension.useTransaction
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTFluidSyncable
import hiiragi283.ragium.api.machine.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockBuilder
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockComponent
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockController
import hiiragi283.ragium.api.storage.HTMachineFluidStorage
import hiiragi283.ragium.api.storage.HTStorageBuilder
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.HTStorageSide
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.screen.HTSmallMachineScreenHandler
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
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
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import kotlin.collections.iterator

class HTFluidDrillBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntityBase(RagiumBlockEntityTypes.FLUID_DRILL, pos, state),
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

    override fun onTierUpdated(oldTier: HTMachineTier, newTier: HTMachineTier) {
        fluidStorage.update(newTier)
    }

    private var fluidStorage: HTMachineFluidStorage = HTStorageBuilder(1)
        .set(0, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .buildMachineFluidStorage()

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        fluidStorage.writeNbt(nbt, wrapperLookup)
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
        fluidStorage.readNbt(nbt, wrapperLookup, tier)
    }

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTSmallMachineScreenHandler(syncId, playerInventory, packet, createContext())

    override fun process(world: World, pos: BlockPos): DataResult<Unit> {
        if (!updateValidation(cachedState, world, pos)) return DataResult.error { "Invalid multiblock structure found!" }
        useTransaction { transaction: Transaction ->
            return fluidStorage.flatMap(0) { storageIn: SingleFluidStorage ->
                val resource: ResourceAmount<FluidVariant> = findResource(world.getBiome(pos))
                if (storageIn.insert(resource, transaction) == resource.amount) {
                    transaction.commit()
                    world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS)
                    DataResult.success(Unit)
                } else {
                    DataResult.error { "Failed to insert fluid into Fluid Drill!" }
                }
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

    override fun interactWithFluidStorage(player: PlayerEntity): Boolean = fluidStorage.interactByPlayer(player)

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant>? = fluidStorage.createWrapped()

    //    HTFluidSyncable    //

    override fun sendPacket(player: ServerPlayerEntity, sender: (ServerPlayerEntity, Int, FluidVariant, Long) -> Unit) {
        fluidStorage.sendPacket(player, sender, 1)
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
