package hiiragi283.ragium.common.machine.generator

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.extension.validate
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.machine.property.HTMachinePropertyKeys
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
    override var key: HTMachineKey = RagiumMachineKeys.SOLAR_PANEL

    constructor(pos: BlockPos, state: BlockState, key: HTMachineKey, tier: HTMachineTier) : this(pos, state) {
        this.key = key
        this.tier = tier
    }

    override fun interactWithFluidStorage(player: PlayerEntity): Boolean = false

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler? = null

    override val energyFlag: HTEnergyNetwork.Flag = HTEnergyNetwork.Flag.GENERATE

    override fun process(world: World, pos: BlockPos): DataResult<Unit> = DataResult.success(Unit).validate(
        { key.entry.getOrDefault(HTMachinePropertyKeys.GENERATOR_PREDICATE)(world, pos) },
        { "Failed to generate energy!" },
    )
}
