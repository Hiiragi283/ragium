package hiiragi283.ragium.api.block

import hiiragi283.ragium.api.data.HTCatalystConversion
import net.minecraft.Util
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.neoforged.neoforge.registries.datamaps.DataMapType

class HTCatalystBlock(val dataMapType: DataMapType<Block, HTCatalystConversion>, properties: Properties) :
    Block(properties.lightLevel { state: BlockState -> if (state.getValue(IS_ACTIVE)) 15 else 0 }.randomTicks()) {
    companion object {
        @JvmField
        val IS_ACTIVE: BooleanProperty = HTBlockStateProperties.IS_ACTIVE

        @JvmStatic
        fun create(dataMapType: DataMapType<Block, HTCatalystConversion>): (Properties) -> HTCatalystBlock =
            { prop: Properties -> HTCatalystBlock(dataMapType, prop) }
    }

    init {
        registerDefaultState(stateDefinition.any().setValue(IS_ACTIVE, false))
    }

    override fun randomTick(
        state: BlockState,
        level: ServerLevel,
        pos: BlockPos,
        random: RandomSource,
    ) {
        if (random.nextInt(5) != 0) return
        val direction: Direction = Util.getRandom(Direction.entries, random)
        val posTo: BlockPos = pos.relative(direction)
        val stateTo: BlockState = level.getBlockState(posTo)
        val conversion: HTCatalystConversion = stateTo.blockHolder.getData(dataMapType) ?: return
        conversion.convert(level, posTo)
    }

    // override fun isRandomlyTicking(state: BlockState): Boolean = state.getValue(IS_ACTIVE)

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(IS_ACTIVE)
    }
}
