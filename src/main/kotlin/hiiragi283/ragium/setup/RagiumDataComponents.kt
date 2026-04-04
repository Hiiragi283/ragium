package hiiragi283.ragium.setup

import com.google.common.primitives.Ints
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.storage.attachments.HTAttachedItems
import hiiragi283.core.common.registry.register.HTDeferredDataComponentRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.item.component.HTLootTicketTargets
import hiiragi283.ragium.api.item.component.HTSpawnerMob
import hiiragi283.ragium.common.item.HTBlueprintItem
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.util.Unit
import org.apache.commons.lang3.math.Fraction
import java.util.function.IntSupplier

object RagiumDataComponents {
    @JvmField
    val REGISTER = HTDeferredDataComponentRegister(Registries.DATA_COMPONENT_TYPE, RagiumAPI.MOD_ID)

    @JvmField
    val CAPACITY_SCALE: DataComponentType<Int> = REGISTER.registerType("capacity_scale", BiCodecs.POSITIVE_INT)

    @JvmStatic
    fun getCapacity(base: IntSupplier, scale: Int): Int = Ints.saturatedCast(base.asInt * scale.toLong())

    @JvmField
    val CHARGE_POWER: DataComponentType<Fraction> = REGISTER.registerType("charge_power", BiCodecs.NON_NEGATIVE_FRACTION)

    @JvmField
    val BLUEPRINT_NUMBER: DataComponentType<Int> = REGISTER.registerType("blueprint_number", HTBlueprintItem.RANGE_CODEC)

    @JvmField
    val CREATIVE_STORAGE: DataComponentType<Unit> = REGISTER.registerFlag("creative_storage")

    @JvmField
    val LOOT_TICKET: DataComponentType<HTLootTicketTargets> = REGISTER.registerType("loot_ticket", HTLootTicketTargets.CODEC)

    @JvmField
    val MACHINE_UPGRADES: DataComponentType<HTAttachedItems> = REGISTER.registerType("machine_upgrades", HTAttachedItems.CODEC)

    @JvmField
    val SPAWNER_MOB: DataComponentType<HTSpawnerMob> = REGISTER.registerType("spawner_mob", HTSpawnerMob.CODEC)
}
