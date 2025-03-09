package hiiragi283.ragium.common.entity

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.RagiumDataMaps
import hiiragi283.ragium.common.init.RagiumEntityTypes
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult

class HTNapalmDynamite : HTRangedEntityDynamite<LivingEntity> {
    constructor(type: EntityType<out HTNapalmDynamite>, level: Level) : super(type, level)

    constructor(level: Level, shooter: LivingEntity) : super(
        RagiumEntityTypes.NAPALM_DYNAMITE.get(),
        level,
        shooter,
    )

    override fun getDefaultItem(): Item = RagiumItems.NAPALM_DYNAMITE.get()

    override fun onHitBlock(result: BlockHitResult) {
        super.onHitBlock(result)
        val level: Level = level()
        if (!level.isClientSide) {
            val hitPos: BlockPos = result.blockPos
            val range: Int = RagiumAPI.getInstance().getEffectRange(item)
            BlockPos
                .betweenClosedStream(
                    hitPos.x - range,
                    hitPos.y - range,
                    hitPos.z - range,
                    hitPos.x + range,
                    hitPos.y + range,
                    hitPos.z + range,
                ).forEach { posIn: BlockPos ->
                    level
                        .getBlockState(posIn)
                        .blockHolder
                        .getData(RagiumDataMaps.NAPALM)
                        ?.updateState(level, posIn)
                }
            discard()
        }
    }

    override val entityClass: Class<LivingEntity> = LivingEntity::class.java

    override fun forEachEntity(entity: LivingEntity) {
        entity.igniteForSeconds(60f)
    }
}
