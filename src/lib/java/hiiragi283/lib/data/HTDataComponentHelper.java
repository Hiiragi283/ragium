package hiiragi283.lib.data;

import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.List;
import java.util.function.Consumer;

/// @author Hiiragi Tsubasa
/// @since 26.1.8
public final class HTDataComponentHelper {

    /// Javaのガバガバジェネリクスを利用して[DataComponentGetter]を無理やり[DataComponentPatch]に変換します。
    ///
    /// @param getter コンポーネントの提供元
    /// @return 無理やり作った[DataComponentPatch]のインスタンス
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static DataComponentPatch createPatch(DataComponentGetter getter) {
        List<TypedDataComponent<?>> list =
                BuiltInRegistries.DATA_COMPONENT_TYPE.stream()
                        .mapMulti(
                                (DataComponentType<?> type,
                                        Consumer<TypedDataComponent<?>> consumer) -> {
                                    Object value = getter.get(type);
                                    if (value != null) {
                                        consumer.accept(new TypedDataComponent(type, value));
                                    }
                                })
                        .toList();
        return DataComponentPatch.builder().set(list).build();
    }
}
