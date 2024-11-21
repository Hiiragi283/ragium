package hiiragi283.ragium.api.machine.block

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.property.HTMachinePropertyKeys
import hiiragi283.ragium.api.world.HTEnergyNetwork
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class HTGeneratorBlockEntityBase(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntityBase(type, pos, state) {
    //    Simple    //

    class Simple(pos: BlockPos, state: BlockState) : HTGeneratorBlockEntityBase(RagiumBlockEntityTypes.SIMPLE_GENERATOR, pos, state) {
        override var key: HTMachineKey = RagiumMachineKeys.SOLAR_PANEL

        constructor(pos: BlockPos, state: BlockState, key: HTMachineKey, tier: HTMachineTier) : this(pos, state) {
            this.key = key
            this.tier = tier
        }

        override fun interactWithFluidStorage(player: PlayerEntity): Boolean = false

        override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler? = null

        override fun getRequiredEnergy(world: World, pos: BlockPos): DataResult<Pair<HTEnergyNetwork.Flag, Long>> =
            DataResult.success(HTEnergyNetwork.Flag.GENERATE to tier.recipeCost)

        override fun process(world: World, pos: BlockPos): Boolean =
            key.entry.getOrDefault(HTMachinePropertyKeys.GENERATOR_PREDICATE)(world, pos)
    }
}
