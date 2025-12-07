package hiiragi283.ragium.api.recipe.ingredient

import hiiragi283.ragium.api.data.map.HTSubEntityTypeIngredient
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import hiiragi283.ragium.api.registry.getHolderDataMap
import hiiragi283.ragium.api.serialization.codec.MapBiCodec
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.SpawnEggItem
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import net.neoforged.neoforge.common.crafting.IngredientType
import java.util.stream.Stream

data class HTEntityTypeIngredient(private val holderSet: HolderSet<EntityType<*>>) : ICustomIngredient {
    companion object {
        @JvmField
        val CODEC: MapBiCodec<RegistryFriendlyByteBuf, HTEntityTypeIngredient> = VanillaBiCodecs
            .holderSet(Registries.ENTITY_TYPE)
            .fieldOf("entities")
            .xmap(::HTEntityTypeIngredient, HTEntityTypeIngredient::holderSet)

        @JvmStatic
        fun of(holderSet: HolderSet<EntityType<*>>): Ingredient = HTEntityTypeIngredient(holderSet).toVanilla()
    }

    override fun test(stack: ItemStack): Boolean {
        // Custom Matching
        val matches: Boolean? = stack.itemHolder
            .getData(RagiumDataMapTypes.SUB_ENTITY_INGREDIENT)
            ?.getEntityType(stack)
            ?.`is`(holderSet)
        if (matches != null) return matches
        // Default Matching
        for (holder: Holder<EntityType<*>> in holderSet) {
            val egg: SpawnEggItem = SpawnEggItem.byId(holder.value()) ?: continue
            if (stack.`is`(egg)) {
                return true
            }
        }
        return false
    }

    override fun getItems(): Stream<ItemStack> = buildList {
        for (holder: Holder<EntityType<*>> in holderSet) {
            val entityType: EntityType<*> = holder.value()
            // Spawn Egg
            SpawnEggItem.byId(entityType)?.let(::ItemStack)?.let(this::add)
            // Custom Stacks
            BuiltInRegistries.ITEM
                .getHolderDataMap(RagiumDataMapTypes.SUB_ENTITY_INGREDIENT)
                .forEach { (holderIn: Holder<Item>, ingredient: HTSubEntityTypeIngredient) ->
                    ingredient
                        .getPreviewStack(holderIn, holder)
                        .filter { stack: ItemStack -> !stack.isEmpty }
                        .forEach(this::add)
                }
        }
    }.stream()

    override fun isSimple(): Boolean = false

    override fun getType(): IngredientType<*> = RagiumIngredientTypes.ENTITY_TYPE.get()
}
