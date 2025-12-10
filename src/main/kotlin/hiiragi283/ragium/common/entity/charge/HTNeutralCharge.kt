package hiiragi283.ragium.common.entity.charge

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.capability.HTItemCapabilities
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.HTChargeType
import hiiragi283.ragium.util.HTItemDropHelper
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult

class HTNeutralCharge : HTAbstractCharge {
    constructor(entityType: EntityType<out HTAbstractCharge>, level: Level) : super(entityType, level)

    constructor(level: Level, shooter: LivingEntity) : super(HTChargeType.STRIKE, level, shooter)

    constructor(level: Level, x: Double, y: Double, z: Double) : super(
        HTChargeType.STRIKE,
        level,
        x,
        y,
        z,
    )

    override fun onHit(level: ServerLevel, result: Either<EntityHitResult, BlockHitResult>) {
        for (entity: Mob in getAffectedEntities<Mob>()) {
            for (slot: HTItemSlot in HTItemCapabilities.getItemSlots(entity, null)) {
                val extracted: ImmutableItemStack =
                    slot.extract(Int.MAX_VALUE, HTStorageAction.EXECUTE, HTStorageAccess.EXTERNAL) ?: continue
                HTItemDropHelper.giveOrDropStack(getOwnerOrSelf(), extracted)
            }
        }
    }

    override fun getDefaultItem(): Item = HTChargeType.STRIKE.asItem()
}
