package hiiragi283.ragium.common.variant

import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.registry.impl.HTDeferredEntityType
import hiiragi283.ragium.api.registry.impl.HTSimpleDeferredItem
import hiiragi283.ragium.api.variant.HTVariantKey
import hiiragi283.ragium.common.entity.charge.HTAbstractCharge
import hiiragi283.ragium.common.entity.charge.HTBlastCharge
import hiiragi283.ragium.common.entity.charge.HTConfusingCharge
import hiiragi283.ragium.common.entity.charge.HTFishingCharge
import hiiragi283.ragium.common.entity.charge.HTTeleportCharge
import hiiragi283.ragium.setup.RagiumEntityTypes
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.ThrowableItemProjectile
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level

enum class HTChargeVariant(private val enPattern: String, private val jaPattern: String) :
    HTVariantKey,
    HTItemHolderLike {
    FISHING("Fishing %s", "フィッシング%s"),
    BLAST("Blast %s", "ブラスト%s"),
    TELEPORT("Teleport %s", "テレポート%s"),
    CONFUSING("Confusing %s", "コンフュージョン%s"),
    ;

    // Item
    fun getItem(): HTSimpleDeferredItem = RagiumItems.CHARGES[this]!!

    fun getShootSound(): SoundEvent = when (this) {
        FISHING -> SoundEvents.FISHING_BOBBER_THROW
        BLAST -> SoundEvents.WITHER_SHOOT
        TELEPORT -> SoundEvents.ENDER_PEARL_THROW
        CONFUSING -> SoundEvents.ELDER_GUARDIAN_CURSE
    }

    override fun asItem(): Item = getItem().get()

    override fun getId(): ResourceLocation = getItem().id

    // Entity
    fun getEntityType(): HTDeferredEntityType<out HTAbstractCharge> = RagiumEntityTypes.CHARGES[this]!!

    fun createCharge(level: Level, shooter: LivingEntity): ThrowableItemProjectile = when (this) {
        FISHING -> HTFishingCharge(level, shooter)
        BLAST -> HTBlastCharge(level, shooter)
        TELEPORT -> HTTeleportCharge(level, shooter)
        CONFUSING -> HTConfusingCharge(level, shooter)
    }

    fun createCharge(
        level: Level,
        x: Double,
        y: Double,
        z: Double,
    ): ThrowableItemProjectile = when (this) {
        FISHING -> HTFishingCharge(level, x, y, z)
        BLAST -> HTBlastCharge(level, x, y, z)
        TELEPORT -> HTTeleportCharge(level, x, y, z)
        CONFUSING -> HTConfusingCharge(level, x, y, z)
    }

    override fun translate(type: HTLanguageType, value: String): String = when (type) {
        HTLanguageType.EN_US -> enPattern
        HTLanguageType.JA_JP -> jaPattern
    }.replace("%s", value)

    override fun variantName(): String = this.name.lowercase()
}
