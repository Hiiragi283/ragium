package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.prefix.HTTagPrefixes
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumArmorMaterials {
    @JvmField
    val REGISTER: DeferredRegister<ArmorMaterial> = DeferredRegister.create(Registries.ARMOR_MATERIAL, RagiumAPI.MOD_ID)

    @JvmField
    val DEFAULT: DeferredHolder<ArmorMaterial, ArmorMaterial> = REGISTER.register("default") { id: ResourceLocation ->
        ArmorMaterial(
            mapOf(),
            0,
            SoundEvents.ARMOR_EQUIP_GENERIC,
            Ingredient::of,
            listOf(ArmorMaterial.Layer(id)),
            0f,
            0f,
        )
    }

    @JvmField
    val AZURE_STEEL: DeferredHolder<ArmorMaterial, ArmorMaterial> = REGISTER.register("steel") { id: ResourceLocation ->
        ArmorMaterial(
            mapOf(
                ArmorItem.Type.BOOTS to 3,
                ArmorItem.Type.LEGGINGS to 6,
                ArmorItem.Type.CHESTPLATE to 8,
                ArmorItem.Type.BODY to 8,
                ArmorItem.Type.HELMET to 3,
            ),
            10,
            SoundEvents.ARMOR_EQUIP_IRON,
            { Ingredient.of(HTTagPrefixes.INGOT.createItemTag(RagiumMaterials.AZURE_STEEL)) },
            listOf(ArmorMaterial.Layer(id)),
            1.2f,
            0f,
        )
    }
}
