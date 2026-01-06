package hiiragi283.ragium.setup

import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.storage.attachments.HTAttachedItems
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.core.common.registry.register.HTDeferredDataComponentRegister
import hiiragi283.core.common.text.HTSimpleTranslation
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.item.component.HTLootTicketTargets
import hiiragi283.ragium.api.item.component.HTSpawnerMob
import net.minecraft.core.GlobalPos
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.DyeColor
import org.apache.commons.lang3.math.Fraction

object RagiumDataComponents {
    @JvmField
    val REGISTER = HTDeferredDataComponentRegister(Registries.DATA_COMPONENT_TYPE, RagiumAPI.MOD_ID)

    @JvmField
    val ANTI_GRAVITY: DataComponentType<Boolean> = REGISTER.registerType("anti_gravity", BiCodec.BOOL)

    @JvmField
    val CHARGE_POWER: DataComponentType<Fraction> = REGISTER.registerType("charge_power", BiCodecs.NON_NEGATIVE_FRACTION)

    @JvmField
    val COLOR: DataComponentType<DyeColor> = REGISTER.registerType("color", VanillaBiCodecs.COLOR)

    @JvmField
    val DESCRIPTION: DataComponentType<HTTranslation> = REGISTER.registerType("description", HTSimpleTranslation.CODEC)

    @JvmField
    val LOCATION: DataComponentType<GlobalPos> = REGISTER.registerType("location", VanillaBiCodecs.GLOBAL_POS)

    @JvmField
    val LOOT_TICKET: DataComponentType<HTLootTicketTargets> = REGISTER.registerType("loot_ticket", HTLootTicketTargets.CODEC)

    @JvmField
    val MACHINE_UPGRADES: DataComponentType<HTAttachedItems> = REGISTER.registerType("machine_upgrades", HTAttachedItems.CODEC)

    @JvmField
    val SPAWNER_MOB: DataComponentType<HTSpawnerMob> = REGISTER.registerType("spawner_mob", HTSpawnerMob.CODEC)
}
