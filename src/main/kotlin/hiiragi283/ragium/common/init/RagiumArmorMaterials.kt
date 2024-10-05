package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tags.RagiumItemTags
import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterial
import net.minecraft.recipe.Ingredient
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents

object RagiumArmorMaterials {
    @JvmField
    val STEEL: RegistryEntry.Reference<ArmorMaterial> = register(
        "steel",
        mapOf(
            ArmorItem.Type.HELMET to 2,
            ArmorItem.Type.CHESTPLATE to 6,
            ArmorItem.Type.LEGGINGS to 5,
            ArmorItem.Type.BOOTS to 2,
            ArmorItem.Type.BODY to 5,
        ),
        10,
        SoundEvents.ITEM_ARMOR_EQUIP_IRON,
        1.0f,
        0.0f,
    ) { Ingredient.fromTag(RagiumItemTags.STEEL_INGOTS) }

    @JvmStatic
    private fun register(
        name: String,
        defenceMap: Map<ArmorItem.Type, Int>,
        enchantability: Int,
        equipSound: RegistryEntry<SoundEvent>,
        toughness: Float,
        kResistance: Float,
        repairment: () -> Ingredient,
    ): RegistryEntry.Reference<ArmorMaterial> = RagiumAPI.id(name).let {
        Registry.registerReference(
            Registries.ARMOR_MATERIAL,
            it,
            ArmorMaterial(
                defenceMap,
                enchantability,
                equipSound,
                repairment,
                listOf(ArmorMaterial.Layer(it)),
                toughness,
                kResistance,
            ),
        )
    }
}
