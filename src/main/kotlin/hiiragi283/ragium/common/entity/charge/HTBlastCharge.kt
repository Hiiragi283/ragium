package hiiragi283.ragium.common.entity.charge

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.common.variant.HTChargeVariant
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult

class HTBlastCharge : HTAbstractCharge {
    constructor(entityType: EntityType<out HTAbstractCharge>, level: Level) : super(entityType, level)

    constructor(level: Level, shooter: LivingEntity) : super(HTChargeVariant.BLAST, level, shooter)

    constructor(level: Level, x: Double, y: Double, z: Double) : super(
        HTChargeVariant.BLAST,
        level,
        x,
        y,
        z,
    )

    override fun onHit(level: ServerLevel, result: Either<EntityHitResult, BlockHitResult>) {
        level.explode(this, x, y, z, getPower(), false, Level.ExplosionInteraction.MOB)
    }

    override fun getDefaultItem(): Item = HTChargeVariant.BLAST.asItem()
}
