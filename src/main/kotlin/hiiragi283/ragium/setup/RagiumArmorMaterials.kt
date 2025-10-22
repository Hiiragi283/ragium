package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.registry.HTDeferredRegister
import hiiragi283.ragium.common.material.HTItemMaterialVariant
import hiiragi283.ragium.common.material.RagiumMaterialType
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.crafting.Ingredient
import java.util.function.Supplier

object RagiumArmorMaterials {
    @JvmField
    val REGISTER: HTDeferredRegister<ArmorMaterial> = HTDeferredRegister(Registries.ARMOR_MATERIAL, RagiumAPI.MOD_ID)

    @JvmStatic
    private fun register(
        name: String,
        enchValue: Int,
        sound: Holder<SoundEvent>,
        repairment: Supplier<Ingredient>,
        toughness: Float,
        knockback: Float,
        vararg defences: Pair<ArmorItem.Type, Int>,
    ): Holder<ArmorMaterial> = REGISTER.register(name) { id: ResourceLocation ->
        ArmorMaterial(
            mapOf(*defences),
            enchValue,
            sound,
            repairment,
            listOf(ArmorMaterial.Layer(id)),
            toughness,
            knockback,
        )
    }

    @JvmField
    val DEFAULT: Holder<ArmorMaterial> = register(
        "default",
        0,
        SoundEvents.ARMOR_EQUIP_GENERIC,
        Ingredient::of,
        0f,
        0f,
    )

    @JvmField
    val AZURE_STEEL: Holder<ArmorMaterial> = register(
        RagiumConst.AZURE_STEEL,
        10,
        SoundEvents.ARMOR_EQUIP_IRON,
        { HTItemMaterialVariant.INGOT.toIngredient(RagiumMaterialType.AZURE_STEEL) },
        1.2f,
        0f,
        ArmorItem.Type.BOOTS to 3,
        ArmorItem.Type.LEGGINGS to 6,
        ArmorItem.Type.CHESTPLATE to 8,
        ArmorItem.Type.BODY to 8,
        ArmorItem.Type.HELMET to 3,
    )

    @JvmField
    val DEEP_STEEL: Holder<ArmorMaterial> = register(
        RagiumConst.DEEP_STEEL,
        15,
        SoundEvents.ARMOR_EQUIP_NETHERITE,
        { HTItemMaterialVariant.INGOT.toIngredient(RagiumMaterialType.DEEP_STEEL) },
        3f,
        0.1f,
        ArmorItem.Type.BOOTS to 3,
        ArmorItem.Type.LEGGINGS to 6,
        ArmorItem.Type.CHESTPLATE to 8,
        ArmorItem.Type.BODY to 11,
        ArmorItem.Type.HELMET to 3,
    )
}
