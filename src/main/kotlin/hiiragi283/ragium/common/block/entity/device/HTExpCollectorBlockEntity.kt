package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.storage.fluid.HTFilteredFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidFilter
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.common.block.entity.HTTickAwareBlockEntity
import hiiragi283.ragium.common.inventory.HTFluidCollectorMenu
import hiiragi283.ragium.common.storage.fluid.HTFluidTank
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.ExperienceOrb
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.structure.BoundingBox
import net.minecraft.world.phys.AABB
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.wrapper.EmptyItemHandler

class HTExpCollectorBlockEntity(pos: BlockPos, state: BlockState) :
    HTTickAwareBlockEntity(RagiumBlockEntityTypes.EXP_COLLECTOR, pos, state),
    MenuProvider {
    private val tank = HTFluidTank(Int.MAX_VALUE, this::setChanged)

    override fun writeNbt(writer: HTNbtCodec.Writer) {
        writer.write(RagiumConstantValues.TANK, tank)
    }

    override fun readNbt(reader: HTNbtCodec.Reader) {
        reader.read(RagiumConstantValues.TANK, tank)
    }

    //    Ticking    //

    override fun onServerTick(level: ServerLevel, pos: BlockPos, state: BlockState): TriState {
        // 20 tickごとに実行する
        if (!canProcess()) return TriState.DEFAULT
        // 自動搬出する
        exportFluids(level, pos)
        // 範囲内のExp Orbを取得する
        val range: Int = RagiumConfig.COMMON.entityCollectorRange.get()
        val expOrbs: List<ExperienceOrb> = level.getEntitiesOfClass(
            ExperienceOrb::class.java,
            AABB.of(
                BoundingBox(
                    blockPos.x - range,
                    blockPos.y - range,
                    blockPos.z - range,
                    blockPos.x + range,
                    blockPos.y + range,
                    blockPos.z + range,
                ),
            ),
        )
        if (expOrbs.isEmpty()) return TriState.DEFAULT
        // それぞれのExp Orbに対して回収を行う
        for (entity: ExperienceOrb in expOrbs) {
            val fluidAmount: Int = entity.value * RagiumConfig.COMMON.expCollectorMultiplier.get()
            val stack: FluidStack = RagiumFluidContents.EXPERIENCE.toStack(fluidAmount)
            if (tank.canFill(stack, true)) {
                tank.fill(stack, IFluidHandler.FluidAction.EXECUTE)
                entity.discard()
            }
        }
        return TriState.TRUE
    }

    override val maxTicks: Int = 20

    override fun getFluidHandler(direction: Direction?): IFluidHandler? = HTFilteredFluidHandler(listOf(tank), HTFluidFilter.DRAIN_ONLY)

    //    MenuProvider    //

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTFluidCollectorMenu =
        HTFluidCollectorMenu(containerId, playerInventory, blockPos, createDefinition(EmptyItemHandler.INSTANCE))

    override fun getDisplayName(): Component = blockState.block.name
}
