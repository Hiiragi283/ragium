package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.codec.BiCodec
import hiiragi283.ragium.api.codec.BiCodecs
import hiiragi283.ragium.api.item.component.HTIntrinsicEnchantment
import hiiragi283.ragium.api.item.component.HTItemSoundEvent
import hiiragi283.ragium.api.item.component.HTLootTicketTargets
import hiiragi283.ragium.api.item.component.HTTeleportPos
import hiiragi283.ragium.api.registry.HTKeyOrTagEntry
import hiiragi283.ragium.api.registry.HTKeyOrTagHelper
import hiiragi283.ragium.api.registry.impl.HTDeferredDataComponentRegister
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.item.DyeColor
import net.neoforged.neoforge.fluids.SimpleFluidContent

object RagiumDataComponents {
    @JvmField
    val REGISTER = HTDeferredDataComponentRegister(Registries.DATA_COMPONENT_TYPE, RagiumAPI.MOD_ID)

    @JvmField
    val ANTI_GRAVITY: DataComponentType<Boolean> = REGISTER.registerType("anti_gravity", BiCodec.BOOL)

    @JvmField
    val BLAST_POWER: DataComponentType<Float> = REGISTER.registerType("blast_power", BiCodecs.POSITIVE_FLOAT)

    @JvmField
    val COLOR: DataComponentType<DyeColor> = REGISTER.registerType("color", BiCodecs.COLOR)

    @JvmField
    val DRINK_SOUND: DataComponentType<HTItemSoundEvent> = REGISTER.registerType("drinking_sound", HTItemSoundEvent.CODEC)

    @JvmField
    val EAT_SOUND: DataComponentType<HTItemSoundEvent> = REGISTER.registerType("eating_sound", HTItemSoundEvent.CODEC)

    @JvmField
    val ENERGY: DataComponentType<Long> = REGISTER.registerType("energy", BiCodecs.NON_NEGATIVE_LONG)

    @JvmField
    val FLUID_CONTENT: DataComponentType<SimpleFluidContent> = REGISTER.registerType(
        "fluid_content",
        SimpleFluidContent.CODEC,
        SimpleFluidContent.STREAM_CODEC,
    )

    @JvmField
    val IMMUNE_DAMAGE_TYPES: DataComponentType<HTKeyOrTagEntry<DamageType>> = REGISTER.registerType(
        "immune_damage_types",
        HTKeyOrTagHelper.INSTANCE.codec(Registries.DAMAGE_TYPE),
    )

    @JvmField
    val INTRINSIC_ENCHANTMENT: DataComponentType<HTIntrinsicEnchantment> = REGISTER.registerType(
        "intrinsic_enchantment",
        HTIntrinsicEnchantment.CODEC,
    )

    @JvmField
    val LOOT_TICKET: DataComponentType<HTLootTicketTargets> = REGISTER.registerType("loot_ticket", HTLootTicketTargets.CODEC)

    @JvmField
    val TELEPORT_POS: DataComponentType<HTTeleportPos> = REGISTER.registerType("teleport_pos", HTTeleportPos.CODEC)
}
