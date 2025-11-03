package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.registry.impl.HTDeferredRecipeType
import hiiragi283.ragium.api.variant.HTEquipmentMaterial
import hiiragi283.ragium.impl.data.map.HTCrushingMaterialRecipeData
import hiiragi283.ragium.impl.data.map.HTRawSmeltingMaterialRecipeData
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.registries.RegisterEvent

object RagiumMiscRegister {
    @JvmStatic
    fun register(event: RegisterEvent) {
        // Armor Material
        event.register(Registries.ARMOR_MATERIAL) { helper ->
            register(helper, RagiumEquipmentMaterials.AZURE_STEEL)
            register(helper, RagiumEquipmentMaterials.DEEP_STEEL)
        }
        // Recipe Type
        event.register(Registries.RECIPE_TYPE) { helper ->
            register(helper, RagiumRecipeTypes.SAWMILL)
            // Machine
            register(helper, RagiumRecipeTypes.ALLOYING)
            register(helper, RagiumRecipeTypes.COMPRESSING)
            register(helper, RagiumRecipeTypes.CRUSHING)
            register(helper, RagiumRecipeTypes.ENCHANTING)
            register(helper, RagiumRecipeTypes.EXTRACTING)
            register(helper, RagiumRecipeTypes.FLUID_TRANSFORM)
            register(helper, RagiumRecipeTypes.MELTING)
            register(helper, RagiumRecipeTypes.PLANTING)
            register(helper, RagiumRecipeTypes.SIMULATING)
            register(helper, RagiumRecipeTypes.WASHING)
        }
        // Material Recipe Type
        event.register(RagiumAPI.MATERIAL_RECIPE_TYPE_KEY) { helper ->
            helper.register(RagiumAPI.id("crushing"), HTCrushingMaterialRecipeData.CODEC)
            helper.register(RagiumAPI.id("raw_smelting"), HTRawSmeltingMaterialRecipeData.CODEC)
        }
    }

    @JvmStatic
    private fun register(helper: RegisterEvent.RegisterHelper<ArmorMaterial>, material: HTEquipmentMaterial) {
        val id: ResourceLocation = RagiumAPI.id(material.asMaterialName())
        helper.register(
            id,
            ArmorMaterial(
                ArmorItem.Type.entries.associateWith(material::getArmorDefence),
                material.enchantmentValue,
                material.getEquipSound(),
                { material.repairIngredient },
                listOf(ArmorMaterial.Layer(id)),
                material.getToughness(),
                material.getKnockbackResistance(),
            ),
        )
    }

    @JvmStatic
    private fun <R : Recipe<*>> register(helper: RegisterEvent.RegisterHelper<RecipeType<*>>, holder: HTDeferredRecipeType<*, R>) {
        helper.register(holder.id, RecipeType.simple<R>(holder.id))
    }
}
