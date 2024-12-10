package hiiragi283.ragium.common.block.machine.generator

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.util.HTUnitResult
import hiiragi283.ragium.api.world.HTEnergyNetwork
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTSimpleGeneratorBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntityBase(RagiumBlockEntityTypes.SIMPLE_GENERATOR, pos, state) {
    override var key: HTMachineKey = RagiumMachineKeys.SOLAR_GENERATOR

    constructor(pos: BlockPos, state: BlockState, key: HTMachineKey) : this(pos, state) {
        this.key = key
    }

    override fun interactWithFluidStorage(player: PlayerEntity): Boolean = false

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler? = null

    override val energyFlag: HTEnergyNetwork.Flag = HTEnergyNetwork.Flag.GENERATE

    override fun process(world: World, pos: BlockPos): HTUnitResult = HTUnitResult.fromBoolString(
        key.entry.getOrDefault(HTMachinePropertyKeys.GENERATOR_PREDICATE)(world, pos),
    ) { "Failed to generate energy!" }
}
