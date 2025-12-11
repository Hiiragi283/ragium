package hiiragi283.ragium.common.entity.vehicle

import hiiragi283.ragium.common.block.entity.storage.HTTankBlockEntity
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumEntityTypes
import hiiragi283.ragium.util.HTStackSlotHelper
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.OwnableEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import java.util.UUID

class HTTankMinecart :
    HTMinecart<HTTankBlockEntity>,
    OwnableEntity {
    constructor(entityType: EntityType<*>, level: Level) : super(entityType, level)

    constructor(level: Level, x: Double, y: Double, z: Double) : super(
        RagiumEntityTypes.TANK_MINECART.get(),
        level,
        x,
        y,
        z,
    )

    override fun createBlockEntity(): HTTankBlockEntity = RagiumBlockEntityTypes.TANK.create(BlockPos.ZERO, defaultDisplayBlockState)

    //    HTMinecart    //

    override fun extraInteract(player: Player, hand: InteractionHand): InteractionResult =
        if (HTStackSlotHelper.interact(player, hand, player.getItemInHand(hand), bindBlockEntity().tank)) {
            InteractionResult.sidedSuccess(player.level().isClientSide)
        } else {
            InteractionResult.PASS
        }

    override fun getPickResult(): ItemStack {
        TODO("Not yet implemented")
    }

    override fun getDefaultDisplayBlockState(): BlockState = RagiumBlocks.TANK.get().defaultBlockState()

    //    OwnableEntity    //

    override fun getOwnerUUID(): UUID? = bindBlockEntity().getOwner()
}
