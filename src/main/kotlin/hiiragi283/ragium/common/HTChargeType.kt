package hiiragi283.ragium.common

import hiiragi283.ragium.api.data.lang.HTLangName
import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.math.fraction
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.registry.impl.HTDeferredEntityType
import hiiragi283.ragium.api.registry.impl.HTSimpleDeferredItem
import hiiragi283.ragium.api.text.HTTranslation
import hiiragi283.ragium.common.entity.charge.HTAbstractCharge
import hiiragi283.ragium.common.entity.charge.HTBlastCharge
import hiiragi283.ragium.common.entity.charge.HTConfusionCharge
import hiiragi283.ragium.common.entity.charge.HTFishingCharge
import hiiragi283.ragium.common.entity.charge.HTNeutralCharge
import hiiragi283.ragium.common.entity.charge.HTStrikeCharge
import hiiragi283.ragium.common.entity.charge.HTTeleportCharge
import hiiragi283.ragium.common.text.RagiumCommonTranslation
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumEntityTypes
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.StringRepresentable
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.ThrowableItemProjectile
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import org.apache.commons.lang3.math.Fraction

enum class HTChargeType(private val enPattern: String, private val jaPattern: String) :
    StringRepresentable,
    HTLangName,
    HTItemHolderLike {
    BLAST("Blast", "ブラスト"),
    STRIKE("Strike", "ストライク"),
    NEUTRAL("Neutralize", "ニュートラライズ"),
    FISHING("Fishing", "フィッシング"),
    TELEPORT("Teleport", "テレポート"),
    CONFUSION("Confusion", "コンフュージョン"),
    ;

    companion object {
        @JvmField
        val DEFAULT_POWER: Fraction = fraction(4)

        @JvmStatic
        fun getPower(stack: ItemStack): Fraction = stack.getOrDefault(RagiumDataComponents.CHARGE_POWER, DEFAULT_POWER)
    }

    // Item
    fun getItem(): HTSimpleDeferredItem = RagiumItems.CHARGES[this]!!

    fun getShootSound(): SoundEvent = when (this) {
        BLAST -> SoundEvents.WITHER_SHOOT
        STRIKE -> SoundEvents.TRIDENT_THUNDER.value()
        NEUTRAL -> SoundEvents.BUNDLE_DROP_CONTENTS
        FISHING -> SoundEvents.FISHING_BOBBER_THROW
        TELEPORT -> SoundEvents.ENDER_PEARL_THROW
        CONFUSION -> SoundEvents.ELDER_GUARDIAN_CURSE
    }

    fun getTranslation(): HTTranslation = when (this) {
        BLAST -> RagiumCommonTranslation.BLAST_CHARGE
        STRIKE -> RagiumCommonTranslation.STRIKE_CHARGE
        NEUTRAL -> RagiumCommonTranslation.NEUTRAL_CHARGE
        FISHING -> RagiumCommonTranslation.FISHING_CHARGE
        TELEPORT -> RagiumCommonTranslation.TELEPORT_CHARGE
        CONFUSION -> RagiumCommonTranslation.CONFUSING_CHARGE
    }

    override fun asItem(): Item = getItem().get()

    override fun getId(): ResourceLocation = getItem().id

    // Entity
    fun getEntityType(): HTDeferredEntityType<out HTAbstractCharge> = RagiumEntityTypes.CHARGES[this]!!

    fun createCharge(level: Level, shooter: LivingEntity): ThrowableItemProjectile = when (this) {
        BLAST -> HTBlastCharge(level, shooter)
        STRIKE -> HTStrikeCharge(level, shooter)
        NEUTRAL -> HTNeutralCharge(level, shooter)
        FISHING -> HTFishingCharge(level, shooter)
        TELEPORT -> HTTeleportCharge(level, shooter)
        CONFUSION -> HTConfusionCharge(level, shooter)
    }

    fun createCharge(
        level: Level,
        x: Double,
        y: Double,
        z: Double,
    ): ThrowableItemProjectile = when (this) {
        STRIKE -> HTStrikeCharge(level, x, y, z)
        FISHING -> HTFishingCharge(level, x, y, z)
        NEUTRAL -> HTNeutralCharge(level, x, y, z)
        BLAST -> HTBlastCharge(level, x, y, z)
        TELEPORT -> HTTeleportCharge(level, x, y, z)
        CONFUSION -> HTConfusionCharge(level, x, y, z)
    }

    override fun getTranslatedName(type: HTLanguageType): String = when (type) {
        HTLanguageType.EN_US -> enPattern
        HTLanguageType.JA_JP -> jaPattern
    }

    override fun getSerializedName(): String = name.lowercase()
}
