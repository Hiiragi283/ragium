package hiiragi283.ragium.common.data.map

import com.mojang.serialization.MapCodec
import dev.shadowsoffire.hostilenetworks.data.DataModel
import dev.shadowsoffire.hostilenetworks.data.DataModelRegistry
import dev.shadowsoffire.hostilenetworks.data.ModelTier
import dev.shadowsoffire.hostilenetworks.data.ModelTierRegistry
import dev.shadowsoffire.hostilenetworks.item.DataModelItem
import hiiragi283.ragium.api.data.map.HTSubEntityTypeIngredient
import net.minecraft.core.Holder
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import java.util.stream.Stream
import kotlin.jvm.optionals.getOrNull

object HTDataModelEntityIngredient : HTSubEntityTypeIngredient {
    @JvmField
    val CODEC: MapCodec<HTDataModelEntityIngredient> = MapCodec.unit { HTDataModelEntityIngredient }

    override fun type(): MapCodec<HTDataModelEntityIngredient> = CODEC

    override fun getEntityType(stack: ItemStack): EntityType<*>? {
        val modelData: DataModel = DataModelItem.getStoredModel(stack).optional.getOrNull() ?: return null
        val rawTier: Int = DataModelItem.getData(stack)
        val tier: ModelTier = ModelTierRegistry.getByData(modelData, rawTier)
        return when {
            tier.canSim -> modelData.entity()
            else -> null
        }
    }

    override fun getPreviewStack(baseItem: Holder<Item>, entityType: Holder<EntityType<*>>): Stream<ItemStack> = DataModelRegistry.INSTANCE
        .getForEntity(entityType.value())
        ?.map { model ->
            val stack = ItemStack(baseItem)
            DataModelItem.setStoredModel(stack, model)
            stack
        }?.stream() ?: Stream.empty()
}
