package hiiragi283.ragium.common.entity.vehicle

import hiiragi283.ragium.api.storage.fluid.HTFluidInteractable
import hiiragi283.ragium.common.block.entity.storage.HTDrumBlockEntity
import hiiragi283.ragium.common.tier.HTDrumTier
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.OwnableEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import java.util.UUID

sealed class HTDrumMinecart :
    HTMinecart<HTDrumBlockEntity>,
    HTFluidInteractable,
    OwnableEntity {
    constructor(entityType: EntityType<*>, level: Level) : super(entityType, level)

    constructor(tier: HTDrumTier, level: Level, x: Double, y: Double, z: Double) : super(
        tier.getEntityType().get(),
        level,
        x,
        y,
        z,
    )

    protected abstract fun getDrumTier(): HTDrumTier

    /*override fun tick() {
        super.tick()
        getDrumBlockEntityType()
            .getTicker(this.level().isClientSide)
            ?.tick(this.level(), this.blockPosition(), this.displayBlockState, bindBlockEntity())
    }*/

    override fun createBlockEntity(): HTDrumBlockEntity = getDrumTier().getBlockEntityType().create(BlockPos.ZERO, defaultDisplayBlockState)

    //    HTMinecart    //

    override fun extraInteract(player: Player, hand: InteractionHand): InteractionResult = interactWith(this.level(), player, hand).result()

    override fun getPickResult(): ItemStack = getDrumTier().getMinecartItem().toStack()

    override fun getDefaultDisplayBlockState(): BlockState = getDrumTier().getBlock().get().defaultBlockState()

    //    HTFluidInteractable    //

    override fun interactWith(level: Level, player: Player, hand: InteractionHand): ItemInteractionResult =
        bindBlockEntity().interactWith(level, player, hand)

    //    OwnableEntity    //

    override fun getOwnerUUID(): UUID? = bindBlockEntity().getOwner()

    //    Impl    //

    class Small : HTDrumMinecart {
        constructor(entityType: EntityType<*>, level: Level) : super(entityType, level)

        constructor(level: Level, x: Double, y: Double, z: Double) : super(HTDrumTier.SMALL, level, x, y, z)

        override fun getDrumTier(): HTDrumTier = HTDrumTier.SMALL
    }

    class Medium : HTDrumMinecart {
        constructor(entityType: EntityType<*>, level: Level) : super(entityType, level)

        constructor(level: Level, x: Double, y: Double, z: Double) : super(HTDrumTier.MEDIUM, level, x, y, z)

        override fun getDrumTier(): HTDrumTier = HTDrumTier.MEDIUM
    }

    class Large : HTDrumMinecart {
        constructor(entityType: EntityType<*>, level: Level) : super(entityType, level)

        constructor(level: Level, x: Double, y: Double, z: Double) : super(HTDrumTier.LARGE, level, x, y, z)

        override fun getDrumTier(): HTDrumTier = HTDrumTier.LARGE
    }

    class Huge : HTDrumMinecart {
        constructor(entityType: EntityType<*>, level: Level) : super(entityType, level)

        constructor(level: Level, x: Double, y: Double, z: Double) : super(HTDrumTier.HUGE, level, x, y, z)

        override fun getDrumTier(): HTDrumTier = HTDrumTier.HUGE
    }

    class Creative : HTDrumMinecart {
        constructor(entityType: EntityType<*>, level: Level) : super(entityType, level)

        constructor(level: Level, x: Double, y: Double, z: Double) : super(HTDrumTier.CREATIVE, level, x, y, z)

        override fun getDrumTier(): HTDrumTier = HTDrumTier.CREATIVE
    }
}
