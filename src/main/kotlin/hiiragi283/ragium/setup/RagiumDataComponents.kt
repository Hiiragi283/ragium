package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.item.component.HTFluidContents
import hiiragi283.ragium.api.item.component.HTIntrinsicEnchantment
import hiiragi283.ragium.api.item.component.HTItemContents
import hiiragi283.ragium.api.item.component.HTItemSoundEvent
import hiiragi283.ragium.api.item.component.HTLootTicketTargets
import hiiragi283.ragium.api.item.component.HTMachineUpgrade
import hiiragi283.ragium.api.item.component.HTSpawnerMob
import hiiragi283.ragium.api.item.component.HTStackContents
import hiiragi283.ragium.api.item.component.HTTeleportPos
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.registry.HTKeyOrTagEntry
import hiiragi283.ragium.api.registry.HTKeyOrTagHelper
import hiiragi283.ragium.api.registry.impl.HTDeferredDataComponentRegister
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.BiCodecs
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.text.HTSimpleTranslation
import hiiragi283.ragium.api.text.HTTranslation
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.bus.api.IEventBus
import org.apache.commons.lang3.math.Fraction

object RagiumDataComponents {
    @JvmField
    val REGISTER = HTDeferredDataComponentRegister(Registries.DATA_COMPONENT_TYPE, RagiumAPI.MOD_ID)

    @JvmStatic
    fun init(eventBus: IEventBus) {
        REGISTER.addAlias("blast_power", "charge_power")

        REGISTER.register(eventBus)
    }

    @JvmField
    val ANTI_GRAVITY: DataComponentType<Boolean> = REGISTER.registerType("anti_gravity", BiCodec.BOOL)

    @JvmField
    val CHARGE_POWER: DataComponentType<Fraction> = REGISTER.registerType("charge_power", BiCodecs.FRACTION)

    @JvmField
    val COLOR: DataComponentType<DyeColor> = REGISTER.registerType("color", VanillaBiCodecs.COLOR)

    @JvmField
    val DAMAGE_RESISTANT: DataComponentType<HTKeyOrTagEntry<DamageType>> =
        REGISTER.registerType("damage_resistant", HTKeyOrTagHelper.INSTANCE.codec(Registries.DAMAGE_TYPE))

    @JvmField
    val DESCRIPTION: DataComponentType<HTTranslation> = REGISTER.registerType("description", HTSimpleTranslation.CODEC)

    @JvmField
    val DRINK_SOUND: DataComponentType<HTItemSoundEvent> = REGISTER.registerType("drinking_sound", HTItemSoundEvent.CODEC)

    @JvmField
    val EAT_SOUND: DataComponentType<HTItemSoundEvent> = REGISTER.registerType("eating_sound", HTItemSoundEvent.CODEC)

    @JvmField
    val INTRINSIC_ENCHANTMENT: DataComponentType<HTIntrinsicEnchantment> =
        REGISTER.registerType("intrinsic_enchantment", HTIntrinsicEnchantment.CODEC)

    @JvmField
    val IS_ACTIVE: DataComponentType<Boolean> = REGISTER.registerType("is_active", BiCodec.BOOL)

    @JvmField
    val LOOT_TICKET: DataComponentType<HTLootTicketTargets> = REGISTER.registerType("loot_ticket", HTLootTicketTargets.CODEC)

    @JvmField
    val REPAIRABLE: DataComponentType<HTItemIngredient> = REGISTER.registerType("repairable", HTItemIngredient.CODEC)

    @JvmField
    val SPAWNER_MOB: DataComponentType<HTSpawnerMob> = REGISTER.registerType("spawner_mob", HTSpawnerMob.CODEC)

    @JvmField
    val TELEPORT_POS: DataComponentType<HTTeleportPos> = REGISTER.registerType("teleport_pos", HTTeleportPos.CODEC)

    //    Machine    //

    @JvmField
    val MACHINE_UPGRADE: DataComponentType<HTMachineUpgrade> = REGISTER.registerType("machine_upgrade", HTMachineUpgrade.CODEC)

    @JvmField
    val MACHINE_UPGRADES: DataComponentType<HTItemContents> =
        REGISTER.registerType("machine_upgrades", HTStackContents.codec(ImmutableItemStack.CODEC))

    @JvmField
    val MACHINE_UPGRADE_FILTER: DataComponentType<HTKeyOrTagEntry<BlockEntityType<*>>> =
        REGISTER.registerType("machine_upgrade/filter", HTKeyOrTagHelper.INSTANCE.codec(Registries.BLOCK_ENTITY_TYPE))

    //    Storage    //

    @JvmField
    val ENERGY: DataComponentType<Int> = REGISTER.registerType("energy", BiCodecs.NON_NEGATIVE_INT)

    @JvmField
    val FLUID_CONTENT: DataComponentType<HTFluidContents> =
        REGISTER.registerType("fluid_content", HTStackContents.codec(ImmutableFluidStack.CODEC))

    @JvmField
    val ITEM_CONTENT: DataComponentType<HTItemContents> =
        REGISTER.registerType("item_content", HTStackContents.codec(ImmutableItemStack.CODEC))
}
