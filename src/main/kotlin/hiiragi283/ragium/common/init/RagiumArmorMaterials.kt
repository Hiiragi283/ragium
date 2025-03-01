package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
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
    val DEFAULT: DeferredHolder<ArmorMaterial, ArmorMaterial> = REGISTER.register("default") { _: ResourceLocation ->
        ArmorMaterial(
            mapOf(),
            0,
            SoundEvents.ARMOR_EQUIP_GENERIC,
            Ingredient::of,
            listOf(),
            0f,
            0f,
        )
    }

    @JvmField
    val EMBER_ALLOY: DeferredHolder<ArmorMaterial, ArmorMaterial> = REGISTER.register("bronze") { id: ResourceLocation ->
        ArmorMaterial(
            mapOf(
                ArmorItem.Type.BOOTS to 2,
                ArmorItem.Type.LEGGINGS to 6,
                ArmorItem.Type.CHESTPLATE to 7,
                ArmorItem.Type.BODY to 7,
                ArmorItem.Type.HELMET to 3,
            ),
            15,
            SoundEvents.ARMOR_EQUIP_IRON,
            { HTTagPrefix.INGOT.createIngredient(RagiumMaterials.EMBER_ALLOY) },
            listOf(ArmorMaterial.Layer(id)),
            0.5f,
            0f,
        )
    }

    @JvmField
    val STEEL: DeferredHolder<ArmorMaterial, ArmorMaterial> = REGISTER.register("steel") { id: ResourceLocation ->
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
            { HTTagPrefix.INGOT.createIngredient(CommonMaterials.STEEL) },
            listOf(ArmorMaterial.Layer(id)),
            1.2f,
            0f,
        )
    }
}
