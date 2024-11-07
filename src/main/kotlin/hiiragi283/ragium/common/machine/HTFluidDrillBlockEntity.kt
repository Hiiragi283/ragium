package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.extension.insert
import hiiragi283.ragium.api.extension.useTransaction
import hiiragi283.ragium.api.fluid.HTMachineFluidStorage
import hiiragi283.ragium.api.inventory.HTSidedInventory
import hiiragi283.ragium.api.inventory.HTStorageBuilder
import hiiragi283.ragium.api.inventory.HTStorageIO
import hiiragi283.ragium.api.inventory.HTStorageSide
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTProcessorBlockEntityBase
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockBuilder
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockComponent
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockController
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.screen.HTSimpleMachineScreenHandler
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.BiomeTags
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome

class HTFluidDrillBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntityBase(RagiumBlockEntityTypes.FLUID_DRILL, pos, state),
    HTMultiblockController {
    override var key: HTMachineKey = RagiumMachineKeys.FLUID_DRILL

    constructor(pos: BlockPos, state: BlockState, tier: HTMachineTier) : this(pos, state) {
        this.tier = tier
    }

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler? =
        HTSimpleMachineScreenHandler(syncId, playerInventory, packet, createContext())

    override fun processRecipe(world: World, pos: BlockPos): Boolean {
        val biome: RegistryEntry<Biome> = world.getBiome(pos)
        val resource: ResourceAmount<FluidVariant> = when {
            biome.isIn(BiomeTags.IS_END) -> ResourceAmount(FluidVariant.of(RagiumFluids.NOBLE_GAS.value), FluidConstants.INGOT)
            biome.isIn(BiomeTags.IS_NETHER) -> ResourceAmount(FluidVariant.of(RagiumFluids.CRUDE_OIL.value), FluidConstants.BUCKET)
            biome.isIn(BiomeTags.IS_OCEAN) -> ResourceAmount(FluidVariant.of(RagiumFluids.SALT_WATER.value), FluidConstants.BUCKET)
            else -> null ?: return false
        }
        useTransaction { transaction: Transaction ->
            val inserted: Long = fluidStorage.get(0).insert(resource, transaction)
            if (inserted > 0) {
                transaction.commit()
                return true
            } else {
                transaction.abort()
                return false
            }
        }
    }

    override val fluidStorage: HTMachineFluidStorage = HTStorageBuilder(1)
        .set(0, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .buildFluidStorage()

    override val parent: HTSidedInventory = HTStorageBuilder(0).buildSided()

    //    HTMultiblockController    //

    override var showPreview: Boolean = false

    override fun buildMultiblock(builder: HTMultiblockBuilder) {
        builder
            .addHollow(
                -1..1,
                0,
                1..3,
                HTMultiblockComponent.of(tier.getHull()),
            ).addCross4(
                -1..1,
                1,
                1..3,
                HTMultiblockComponent.of(Blocks.WAXED_COPPER_GRATE),
            ).addCross4(
                -1..1,
                2,
                1..3,
                HTMultiblockComponent.of(Blocks.WAXED_COPPER_GRATE),
            )
        builder.add(
            0,
            3,
            2,
            HTMultiblockComponent.of(Blocks.WAXED_COPPER_GRATE),
        )
        builder.add(
            0,
            4,
            2,
            HTMultiblockComponent.of(Blocks.WAXED_COPPER_GRATE),
        )
    }
}
