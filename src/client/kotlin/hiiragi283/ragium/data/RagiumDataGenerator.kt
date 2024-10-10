package hiiragi283.ragium.data

import hiiragi283.ragium.api.RagiumAPI
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.enchantment.Enchantment
import net.minecraft.item.Item
import net.minecraft.registry.Registerable
import net.minecraft.registry.RegistryBuilder
import net.minecraft.registry.RegistryEntryLookup
import net.minecraft.registry.RegistryKeys

object RagiumDataGenerator : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
        val pack: FabricDataGenerator.Pack = fabricDataGenerator.createPack()
        // server
        pack.addProvider(::RagiumAdvancementProvider)
        pack.addProvider(::RagiumBlockLootProvider)
        pack.addProvider(::RagiumEnchantmentProvider)
        pack.addProvider(::RagiumEntityLootProvider)
        pack.addProvider(::RagiumVanillaRecipeProvider)
        pack.addProvider(::RagiumMachineRecipeProvider)
        RagiumTagProviders.init(pack)
        // client
        pack.addProvider(::RagiumModelProvider)
        RagiumLangProviders.init(pack)

        RagiumAPI.log { info("Ragium data generation is done!") }
    }

    override fun buildRegistry(registryBuilder: RegistryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.ENCHANTMENT) { registerable: Registerable<Enchantment> ->
            val enchantmentLookup: RegistryEntryLookup<Enchantment> = registerable.getRegistryLookup(RegistryKeys.ENCHANTMENT)
            val itemLookup: RegistryEntryLookup<Item> = registerable.getRegistryLookup(RegistryKeys.ITEM)

            RagiumEnchantmentProvider.registerEnchantments(registerable::register, enchantmentLookup, itemLookup)
        }
    }
}
