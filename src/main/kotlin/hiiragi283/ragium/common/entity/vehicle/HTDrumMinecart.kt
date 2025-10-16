package hiiragi283.ragium.common.entity.vehicle

import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.common.block.entity.HTDrumBlockEntity
import hiiragi283.ragium.common.variant.HTDrumVariant
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

sealed class HTDrumMinecart : HTMinecart<HTDrumBlockEntity> {
    constructor(variant: HTDrumVariant, entityType: EntityType<*>, level: Level) : super(entityType, level) {
        this.variant = variant
        this.blockEntityHolder = variant.blockEntityHolder
    }

    constructor(variant: HTDrumVariant, level: Level, x: Double, y: Double, z: Double) : super(
        variant.entityHolder.get(),
        level,
        x,
        y,
        z,
    ) {
        this.variant = variant
        this.blockEntityHolder = variant.blockEntityHolder
    }

    private val variant: HTDrumVariant
    private val blockEntityHolder: HTDeferredBlockEntityType<HTDrumBlockEntity>

    override fun tick() {
        super.tick()
        blockEntityHolder
            .getTicker(this.level().isClientSide)
            ?.tick(this.level(), this.blockPosition(), this.displayBlockState, this.blockEntity)
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): HTDrumBlockEntity = blockEntityHolder.create(pos, state)

    //    HTMinecart    //

    override fun getPickResult(): ItemStack = RagiumItems.DRUM_MINECARTS[variant]!!.toStack()

    override fun getDefaultDisplayBlockState(): BlockState = variant.blockHolder.get().defaultBlockState()

    //    Impl    //

    class Small : HTDrumMinecart {
        constructor(entityType: EntityType<*>, level: Level) : super(HTDrumVariant.SMALL, entityType, level)

        constructor(level: Level, x: Double, y: Double, z: Double) : super(HTDrumVariant.SMALL, level, x, y, z)
    }

    class Medium : HTDrumMinecart {
        constructor(entityType: EntityType<*>, level: Level) : super(HTDrumVariant.MEDIUM, entityType, level)

        constructor(level: Level, x: Double, y: Double, z: Double) : super(HTDrumVariant.MEDIUM, level, x, y, z)
    }

    class Large : HTDrumMinecart {
        constructor(entityType: EntityType<*>, level: Level) : super(HTDrumVariant.LARGE, entityType, level)

        constructor(level: Level, x: Double, y: Double, z: Double) : super(HTDrumVariant.LARGE, level, x, y, z)
    }

    class Huge : HTDrumMinecart {
        constructor(entityType: EntityType<*>, level: Level) : super(HTDrumVariant.HUGE, entityType, level)

        constructor(level: Level, x: Double, y: Double, z: Double) : super(HTDrumVariant.HUGE, level, x, y, z)
    }
}
