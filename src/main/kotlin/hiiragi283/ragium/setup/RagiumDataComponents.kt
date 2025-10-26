package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.item.component.HTDamageResistant
import hiiragi283.ragium.api.item.component.HTIntrinsicEnchantment
import hiiragi283.ragium.api.item.component.HTItemContents
import hiiragi283.ragium.api.item.component.HTItemSoundEvent
import hiiragi283.ragium.api.item.component.HTLootTicketTargets
import hiiragi283.ragium.api.item.component.HTRepairable
import hiiragi283.ragium.api.item.component.HTTeleportPos
import hiiragi283.ragium.api.registry.HTKeyOrTagEntry
import hiiragi283.ragium.api.registry.HTKeyOrTagHelper
import hiiragi283.ragium.api.registry.impl.HTDeferredDataComponentRegister
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.BiCodecs
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.item.DyeColor

object RagiumDataComponents {
    @JvmField
    val REGISTER = HTDeferredDataComponentRegister(Registries.DATA_COMPONENT_TYPE, RagiumAPI.MOD_ID)

    @JvmField
    val ANTI_GRAVITY: DataComponentType<Boolean> = REGISTER.registerType("anti_gravity", BiCodec.BOOL)

    @JvmField
    val BLAST_POWER: DataComponentType<Float> = REGISTER.registerType("blast_power", BiCodecs.POSITIVE_FLOAT)

    @JvmField
    val COLOR: DataComponentType<DyeColor> = REGISTER.registerType("color", VanillaBiCodecs.COLOR)

    @JvmField
    val DAMAGE_RESISTANT: DataComponentType<HTDamageResistant> = REGISTER.registerType("damage_resistant", HTDamageResistant.CODEC)

    @JvmField
    val DRINK_SOUND: DataComponentType<HTItemSoundEvent> = REGISTER.registerType("drinking_sound", HTItemSoundEvent.CODEC)

    @JvmField
    val EAT_SOUND: DataComponentType<HTItemSoundEvent> = REGISTER.registerType("eating_sound", HTItemSoundEvent.CODEC)

    @JvmField
    val ENERGY: DataComponentType<Int> = REGISTER.registerType("energy", BiCodecs.NON_NEGATIVE_INT)

    @JvmField
    val FLUID_CONTENT: DataComponentType<ImmutableFluidStack> = REGISTER.registerType("fluid_content", ImmutableFluidStack.CODEC)

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
    val ITEM_CONTENT: DataComponentType<HTItemContents> = REGISTER.registerType("item_content", HTItemContents.CODEC)

    @JvmField
    val LOOT_TICKET: DataComponentType<HTLootTicketTargets> = REGISTER.registerType("loot_ticket", HTLootTicketTargets.CODEC)

    @JvmField
    val REPAIRABLE: DataComponentType<HTRepairable> = REGISTER.registerType("repairable", HTRepairable.CODEC)

    @JvmField
    val TELEPORT_POS: DataComponentType<HTTeleportPos> = REGISTER.registerType("teleport_pos", HTTeleportPos.CODEC)
}
