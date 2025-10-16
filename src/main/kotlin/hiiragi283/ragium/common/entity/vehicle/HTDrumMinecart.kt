package hiiragi283.ragium.common.entity.vehicle

import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.api.storage.fluid.HTFluidInteractable
import hiiragi283.ragium.common.block.entity.HTDrumBlockEntity
import hiiragi283.ragium.common.variant.HTDrumVariant
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

sealed class HTDrumMinecart :
    HTMinecart<HTDrumBlockEntity>,
    HTFluidInteractable {
    constructor(entityType: EntityType<*>, level: Level) : super(entityType, level)

    constructor(variant: HTDrumVariant, level: Level, x: Double, y: Double, z: Double) : super(
        variant.entityHolder.get(),
        level,
        x,
        y,
        z,
    )

    protected abstract fun getDrumVariant(): HTDrumVariant

    protected fun getDrumBlockEntityType(): HTDeferredBlockEntityType<HTDrumBlockEntity> = getDrumVariant().blockEntityHolder

    override fun tick() {
        super.tick()
        getDrumBlockEntityType()
            .getTicker(this.level().isClientSide)
            ?.tick(this.level(), this.blockPosition(), this.displayBlockState, bindBlockEntity())
    }

    override fun createBlockEntity(): HTDrumBlockEntity =
        getDrumBlockEntityType().create(BlockPos.ZERO, getDrumVariant().blockHolder.get().defaultBlockState())

    //    HTMinecart    //

    override fun extraInteract(player: Player, hand: InteractionHand): InteractionResult = interactWith(this.level(), player, hand).result()

    override fun getPickResult(): ItemStack = getDrumVariant().minecartItem.toStack()

    override fun getDefaultDisplayBlockState(): BlockState = getDrumVariant().blockHolder.get().defaultBlockState()

    //    HTFluidInteractable    //

    override fun interactWith(level: Level, player: Player, hand: InteractionHand): ItemInteractionResult =
        bindBlockEntity().interactWith(level, player, hand)

    //    Impl    //

    class Small : HTDrumMinecart {
        constructor(entityType: EntityType<*>, level: Level) : super(entityType, level)

        constructor(level: Level, x: Double, y: Double, z: Double) : super(HTDrumVariant.SMALL, level, x, y, z)

        override fun getDrumVariant(): HTDrumVariant = HTDrumVariant.SMALL
    }

    class Medium : HTDrumMinecart {
        constructor(entityType: EntityType<*>, level: Level) : super(entityType, level)

        constructor(level: Level, x: Double, y: Double, z: Double) : super(HTDrumVariant.MEDIUM, level, x, y, z)

        override fun getDrumVariant(): HTDrumVariant = HTDrumVariant.MEDIUM
    }

    class Large : HTDrumMinecart {
        constructor(entityType: EntityType<*>, level: Level) : super(entityType, level)

        constructor(level: Level, x: Double, y: Double, z: Double) : super(HTDrumVariant.LARGE, level, x, y, z)

        override fun getDrumVariant(): HTDrumVariant = HTDrumVariant.LARGE
    }

    class Huge : HTDrumMinecart {
        constructor(entityType: EntityType<*>, level: Level) : super(entityType, level)

        constructor(level: Level, x: Double, y: Double, z: Double) : super(HTDrumVariant.HUGE, level, x, y, z)

        override fun getDrumVariant(): HTDrumVariant = HTDrumVariant.HUGE
    }
}
