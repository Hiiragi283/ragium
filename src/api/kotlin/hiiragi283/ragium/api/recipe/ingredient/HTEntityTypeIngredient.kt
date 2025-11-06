package hiiragi283.ragium.api.recipe.ingredient

import com.enderio.base.api.soul.Soul
import com.enderio.base.common.init.EIODataComponents
import com.enderio.base.common.init.EIOItems
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.item.createItemStack
import hiiragi283.ragium.api.serialization.codec.MapBiCodec
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.SpawnEggItem
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.fml.ModList
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import net.neoforged.neoforge.common.crafting.IngredientType
import java.util.stream.Stream

@ConsistentCopyVisibility
data class HTEntityTypeIngredient private constructor(private val holderSet: HolderSet<EntityType<*>>) : ICustomIngredient {
    companion object {
        @JvmField
        val CODEC: MapBiCodec<RegistryFriendlyByteBuf, HTEntityTypeIngredient> = VanillaBiCodecs
            .holderSet(Registries.ENTITY_TYPE)
            .fieldOf("entities")
            .xmap(::HTEntityTypeIngredient, HTEntityTypeIngredient::holderSet)

        @JvmField
        val TYPE: IngredientType<HTEntityTypeIngredient> = IngredientType(CODEC.codec, CODEC.streamCodec)

        @Suppress("DEPRECATION")
        @JvmStatic
        fun of(vararg entityTypes: EntityType<*>): Ingredient = of(HolderSet.direct(EntityType<*>::builtInRegistryHolder, *entityTypes))

        @JvmStatic
        fun of(holderSet: HolderSet<EntityType<*>>): Ingredient = HTEntityTypeIngredient(holderSet).toVanilla()
    }

    override fun test(stack: ItemStack): Boolean {
        for (holder: Holder<EntityType<*>> in holderSet) {
            val egg: SpawnEggItem = SpawnEggItem.byId(holder.value()) ?: continue
            if (stack.`is`(egg)) {
                return true
            }
        }
        if (ModList.get().isLoaded(RagiumConst.EIO_BASE)) {
            val matchSoul: Boolean? = stack.get(EIODataComponents.SOUL)?.entityType()?.`is`(holderSet)
            if (matchSoul == true) {
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
            // Soul vial if Ender IO loaded
            if (ModList.get().isLoaded(RagiumConst.EIO_BASE)) {
                add(createItemStack(EIOItems.SOUL_VIAL, EIODataComponents.SOUL, Soul.of(entityType)))
            }
        }
    }.stream()

    override fun isSimple(): Boolean = false

    override fun getType(): IngredientType<*> = TYPE
}
