package hiiragi283.ragium.common.entity.charge

import hiiragi283.ragium.common.variant.HTChargeVariant
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.level.storage.loot.BuiltInLootTables
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.minecraft.world.phys.HitResult

class HTFishingCharge : HTAbstractCharge {
    constructor(entityType: EntityType<out HTFishingCharge>, level: Level) : super(entityType, level)

    constructor(level: Level, shooter: LivingEntity) : super(HTChargeVariant.FISHING, level, shooter)

    constructor(level: Level, x: Double, y: Double, z: Double) : super(
        HTChargeVariant.FISHING,
        level,
        x,
        y,
        z,
    )

    override fun onHit(result: HitResult) {
        super.onHit(result)
        val level: Level = level()
        if (level is ServerLevel) {
            if (this.isInWater) {
                val params: LootParams = LootParams
                    .Builder(level)
                    .withParameter(LootContextParams.ORIGIN, this.position())
                    .withParameter(LootContextParams.TOOL, ItemStack(Items.FISHING_ROD))
                    .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, owner)
                    .create(LootContextParamSets.FISHING)
                val lootTable: LootTable = level.server.reloadableRegistries().getLootTable(BuiltInLootTables.FISHING)
                repeat(getPower().toInt()) {
                    lootTable.getRandomItems(params).forEach(this::spawnAtLocation)
                }
            }
            discard()
        }
    }

    override fun getDefaultItem(): Item = HTChargeVariant.FISHING.asItem()
}
