package hiiragi283.ragium.common.machine.consume

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.storage.HTStorageBuilder
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.HTStorageSide
import hiiragi283.ragium.api.world.HTEnergyNetwork
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTRockGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntityBase(RagiumBlockEntityTypes.ROCK_GENERATOR, pos, state) {
    override var key: HTMachineKey = RagiumMachineKeys.ROCK_GENERATOR

    constructor(pos: BlockPos, state: BlockState, tier: HTMachineTier) : this(pos, state) {
        this.tier = tier
    }

    private val inventory: SidedInventory = HTStorageBuilder(2)
        .set(0, HTStorageIO.INTERNAL, HTStorageSide.NONE)
        .set(1, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .buildSided()

    override fun interactWithFluidStorage(player: PlayerEntity): Boolean = false

    override fun getRequiredEnergy(world: World, pos: BlockPos): DataResult<Pair<HTEnergyNetwork.Flag, Long>> =
        tier.createEnergyResult(HTEnergyNetwork.Flag.CONSUME)

    override fun process(world: World, pos: BlockPos): DataResult<Unit> {
        TODO("Not yet implemented")
    }

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory?, player: PlayerEntity?): ScreenHandler? {
        TODO("Not yet implemented")
    }
}
