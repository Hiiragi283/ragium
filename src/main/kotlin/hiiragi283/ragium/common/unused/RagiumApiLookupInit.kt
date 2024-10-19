package hiiragi283.ragium.common.unused

object RagiumApiLookupInit {
    /*@JvmStatic
    private fun initHeat() {
        HEAT.registerForBlocks(provideStatic(true), RagiumContents.CREATIVE_SOURCE)

        HEAT.registerForBlocks({ _: World, _: BlockPos, _: BlockState, _: BlockEntity?, direction: Direction? ->
            direction == Direction.UP
        }, Blocks.FIRE)

        HEAT.registerForBlocks({ _: World, _: BlockPos, state: BlockState, _: BlockEntity?, direction: Direction? ->
            direction == Direction.UP && state.getOrDefault(Properties.LIT, false)
        }, Blocks.CAMPFIRE)
    }

    @JvmStatic
    private fun initBlazingHeat() {
        BLAZING_HEAT.registerForBlocks(provideStatic(true), RagiumContents.CREATIVE_SOURCE)

        BLAZING_HEAT.registerForBlocks({ _: World, _: BlockPos, _: BlockState, _: BlockEntity?, direction: Direction? ->
            direction == Direction.UP
        }, Blocks.SOUL_FIRE)

        BLAZING_HEAT.registerForBlocks({ _: World, _: BlockPos, state: BlockState, _: BlockEntity?, direction: Direction? ->
            direction == Direction.UP && state.getOrDefault(Properties.LIT, false)
        }, Blocks.SOUL_CAMPFIRE)
    }*/

    @JvmStatic
    private fun initElectric() {
        /*ENERGY.registerForBlocks({ world: World, pos: BlockPos, state: BlockState, _: BlockEntity?, direction: Direction? ->
            if (direction != null) {
                val axis: Direction.Axis = state.get(Properties.AXIS)
                if (direction.axis == axis) {
                    val posTo: BlockPos = pos.offset(direction.opposite)
                    val stateTo: BlockState = world.getBlockState(posTo)
                    val blockEntityTo: BlockEntity? = world.getBlockEntity(posTo)
                    ENERGY.find(world, posTo, stateTo, blockEntityTo, direction)
                }
            }
            null
        }, RagiumContents.CABLE)

        ENERGY.registerForBlocks({ world: World, pos: BlockPos, state: BlockState, _: BlockEntity?, _: Direction? ->
            val facing: Direction = state.get(Properties.FACING)
            val posTo: BlockPos = pos.offset(facing)
            val stateTo: BlockState = world.getBlockState(posTo)
            val blockEntityTo: BlockEntity? = world.getBlockEntity(posTo)
            ENERGY.find(world, posTo, stateTo, blockEntityTo, facing.opposite)
        }, RagiumContents.GEAR_BOX)*/
    }
}
